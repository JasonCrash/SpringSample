package hello.util.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class HttpConnectionManager {
	private final static Logger log = LoggerFactory.getLogger(HttpConnectionManager.class);
	
	private final static int MAX_TOTAL = 200;
	private final static int DEFAULT_MAX_PERROUTE = 200;
	private final static String PROTOCOL = "TLSv1.2";
	
	private String cafileName = "keystore.p12";
	private String pass = "123456";
	
	private PoolingHttpClientConnectionManager cm = null; 
	private CloseableHttpClient httpClient = null; 
	
	public HttpConnectionManager(boolean caflag) {
		if( cm == null ) {
			try {
				SSLContext sslContext = getSSLContext(caflag);
				SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{PROTOCOL}, null, new NoopHostnameVerifier());
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
						.register("https", sslConnectionSocketFactory)
						.register("http", PlainConnectionSocketFactory.getSocketFactory())
						.build();
				cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				cm.setMaxTotal(MAX_TOTAL);
				cm.setDefaultMaxPerRoute(DEFAULT_MAX_PERROUTE);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("PoolingHttpClientConnectionManager initialization failed.\n" + e.getStackTrace());
			}
		}
	}
	
	private SSLContext getSSLContext(boolean caflag) throws Exception {
		SSLContext sslContext = null;
		if(caflag) {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(new ClassPathResource(cafileName).getFile()), pass.toCharArray());
            sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
		}else {
			TrustManager x509Trust = new X509TrustManager() {
				
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
			sslContext = SSLContext.getInstance(PROTOCOL) ;
			sslContext.init(null, new TrustManager[] { x509Trust }, null);
		}
		return sslContext;
	}
	
	public CloseableHttpClient getHttpClient(int retryCount) {
		if(httpClient == null) {
			CustomHttpRequestRetryHandler retryHandler = new CustomHttpRequestRetryHandler(retryCount);
			httpClient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(retryHandler).build();
		}
		return httpClient;
	}
	
	public static void close(HttpRequestBase requestBase,CloseableHttpResponse response, CloseableHttpClient httpClient) {
		try {
			if(requestBase != null) {
				requestBase.releaseConnection();
			}
			if(response != null) {
				response.close();
			}
			if(httpClient != null) {
				httpClient.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Failed to close the resource.\n" + e.getStackTrace());
		}
	}
	
}
