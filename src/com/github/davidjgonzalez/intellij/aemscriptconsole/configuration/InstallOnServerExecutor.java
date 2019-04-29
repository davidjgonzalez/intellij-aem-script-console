package com.github.davidjgonzalez.intellij.aemscriptconsole.configuration;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.xerces.impl.dv.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.PACKAGE_MANAGER_API;

public class InstallOnServerExecutor {
    private static final String DOWNLOAD_URL = "https://github.com/Adobe-Consulting-Services/acs-aem-tools/releases/download/acs-aem-tools-1.0.2/acs-aem-tools-content-1.0.2-min.zip";

    public static InputStream downloadPackage() throws IOException, ServletException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(DOWNLOAD_URL);

        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);


            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
                return IOUtils.toBufferedInputStream(response.getEntity().getContent());
            } else {
                throw new ServletException(("Unable to download the package from GitHub.com"));
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


    public static String installOnServer(String host, String user, String password, InputStream inputStream) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpPost httpPost = new HttpPost(host + PACKAGE_MANAGER_API);

        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encode((user + ":" + password).getBytes()));

        final HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("strict", "true")
                .addTextBody("install", "true")
                .addBinaryBody("file", inputStream, ContentType.APPLICATION_OCTET_STREAM, "acs-aem-tools--intellij-aem-script-console.zip")
                .build();

        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpPost);

            EntityUtils.consume(response.getEntity());

            switch (response.getStatusLine().getStatusCode()) {
                case HttpServletResponse.SC_OK:
                    return Messages.message("install-on-server.package-install.success");
                case HttpServletResponse.SC_UNAUTHORIZED:
                    return Messages.message("install-on-server.package-install.invalid-credentials");
                default:
                    return Messages.message("install-on-server.package-install.generic-error");
            }
        } catch (Exception e) {
            if (StringUtils.contains(e.getMessage(), "Connection refused")) {
                return Messages.message("install-on-server.package-install.aem-unavailable");
            } else {
                return Messages.message("install-on-server.error-prefix") + e.getMessage();
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

