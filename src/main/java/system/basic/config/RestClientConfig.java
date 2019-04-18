package system.basic.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
    private HttpPollProperties httpPoolProperties;

	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		HttpComponentsClientHttpRequestFactory requestFactory =
		        new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(closeableHttpClient());
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		restTemplate.setInterceptors(Collections.singletonList(new ClientHttpRequestInterceptor(){
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				HttpHeaders headers = request.getHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return execution.execute(request, body);
			}	
		}));		
		return restTemplate;

    }	
	
	@Bean
	public CloseableHttpClient closeableHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return HttpClients.custom()
				.setConnectionManager(poolingHttpClientConnectionManager())
				.setRetryHandler(new HttpRequestRetryHandler() {
					@Override
					public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
						if (executionCount >= httpPoolProperties.getRetryCount()) {
				            // Do not retry if over max retry count
				        	//log.info("HttpRequestRetryHandlerretry exception: " + exception.toString());
				        	//log.info("HttpRequestRetryHandlerretry over max retry count.");
				            return false;
				        }
				        HttpClientContext clientContext = HttpClientContext.adapt(context);
				        HttpRequest request = (HttpRequest) clientContext.getRequest();
				        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				        if (idempotent) {
				            // Retry if the request is considered idempotent
				        	//log.info("HttpRequestRetryHandlerretry exception: " + exception.toString());
				            //log.info("HttpRequestRetryHandlerretry will sleep " + SLEEP_MS + "ms and retry.");
				            try {
				                Thread.sleep(httpPoolProperties.getRetryWait());
				            } catch (InterruptedException e) {
				                // TODO Auto-generated catch block
				                e.printStackTrace();
				            }
				            return true;
				        }
				        //log.info("HttpRequestRetryHandlerretry exception: " + exception.toString());
				        //log.info("HttpRequestRetryHandlerretry will NOT retry.");
				        return false;
					}
				})
				.setDefaultRequestConfig(requestConfig())
				.build();
	}
	
	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = new SSLContextBuilder().setProtocol(httpPoolProperties.getProtocal()).loadTrustMaterial(null, new TrustStrategy() {
			 public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			     return true;
			 }
		    }).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { httpPoolProperties.getProtocal() }, null, new NoopHostnameVerifier() );
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf)
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(httpPoolProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpPoolProperties.getMaxPerRoute());
        connectionManager.setValidateAfterInactivity(httpPoolProperties.getValidateAfterInactivity());
        return connectionManager;
	}
	
	@Bean
	public RequestConfig requestConfig() {
		return RequestConfig.custom()
                .setSocketTimeout(httpPoolProperties.getSocketTimeout()) 
                .setConnectTimeout(httpPoolProperties.getConnectTimeout()) 
                .setConnectionRequestTimeout(httpPoolProperties.getConnectionRequestTimeout())
                .build();
	}
		

}

