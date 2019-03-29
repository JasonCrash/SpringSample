package hello.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestClientConfig {
	@Bean(name = "restTemplate")
	public static RestTemplate restTemplate() {
        try {
			TrustManager trustmanager = new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			};
			
			SSLContext context = SSLContext.getInstance("TLSv1.2");
			context.init(null, new TrustManager[] { trustmanager }, null);
			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(context,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			CloseableHttpClient httpClient = HttpClients.custom()
			        .setSSLSocketFactory(factory)
			        .build();

			HttpComponentsClientHttpRequestFactory requestFactory =
			        new HttpComponentsClientHttpRequestFactory();

			requestFactory.setHttpClient(httpClient);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			restTemplate.setInterceptors(Collections.singletonList(new UserAgentInterceptor()));
			
			return restTemplate;
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return null;
    }
}

class UserAgentInterceptor implements ClientHttpRequestInterceptor{
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return execution.execute(request, body);
	}
}
