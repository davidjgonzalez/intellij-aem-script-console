package com.github.davidjgonzalez.intellij.aemscriptconsole.configuration;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.xerces.impl.dv.util.Base64;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.SCRIPT_EXECUTOR_PATH;

public class TestConnectionExecutor {

    public static String testConnection(String host, String user, String password) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(host + SCRIPT_EXECUTOR_PATH);

        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encode((user + ":" + password).getBytes()));

        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);

            switch (response.getStatusLine().getStatusCode()) {
                case HttpServletResponse.SC_OK:
                    return Messages.message("test-connection.success");
                case HttpServletResponse.SC_UNAUTHORIZED:
                    return Messages.message("test-connection.invalid-credentials");
                case HttpServletResponse.SC_NOT_FOUND:
                    return Messages.message("test-connection.missing-servlet");
                default:
                    return Messages.message("test-connection.generic-error");
            }

        } catch (Exception e) {
            if (StringUtils.contains(e.getMessage(), "Connection refused")) {
                return Messages.message("test-connection.aem-unavailable");
            } else {
                return Messages.message("test-connection.error-prefix") + e.getMessage();
            }
        } finally {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
                response.close();
            }

            if (httpClient != null) {
                httpClient.close();
            }

        }
    }
}

