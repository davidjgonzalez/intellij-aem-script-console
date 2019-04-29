package com.github.davidjgonzalez.intellij.aemscriptconsole.execution;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Constants;
import com.github.davidjgonzalez.intellij.aemscriptconsole.configuration.RunConfigurationImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.xerces.impl.dv.util.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.SCRIPT_EXECUTOR_PATH;

public class ScriptConsoleExecutor {

    private final ScriptConsoleScript script;
    private final RunConfigurationImpl runConfiguration;
    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private HttpPost httpPost;


    public ScriptConsoleExecutor(ScriptConsoleScript script, RunConfigurationImpl runConfiguration) {
        this.script = script;
        this.runConfiguration = runConfiguration;
    }

    public void destroy() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }

        if (httpPost != null) {
            httpPost.abort();
        }
    }

    public ScriptConsoleOutput execute() throws IOException {
        httpPost = new HttpPost(runConfiguration.getHost() + SCRIPT_EXECUTOR_PATH);

        httpPost.setHeader(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64.encode((runConfiguration.getUser() + ":" + runConfiguration.getPassword()).getBytes()));
        httpPost.setHeader(HttpHeaders.REFERER, "http://localhost:4502");
        httpPost.setHeader(HttpHeaders.USER_AGENT, "Apache-HttpClient/intellij+" + Constants.PLUGIN_NAME);
        httpPost.setHeader("Cookie", "wcmmode=disabled;");

        final List<NameValuePair> nvps = new ArrayList<>();

        nvps.add(new BasicNameValuePair(Constants.REQUEST_PARAM_SCRIPT_DATA, script.getScriptData()));
        nvps.add(new BasicNameValuePair(Constants.REQUEST_PARAM_SCRIPT_EXTENSION, translateExtension(script.getExtension())));

        if (StringUtils.isNotBlank(script.getResourcePath())) {
            nvps.add(new BasicNameValuePair(Constants.REQUEST_PARAM_SCRIPT_RESOURCE, script.getResourcePath()));
        }

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        final ScriptConsoleOutput output = new ScriptConsoleOutput(script, runConfiguration);
        final CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            output.finish(response.getStatusLine().getStatusCode(),
                    IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));

            EntityUtils.consume(response.getEntity());

            return output;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private String translateExtension(String extension) {
        switch (extension) {
            case "js":
                return "ecma";
            default:
                return extension;
        }
    }
}
