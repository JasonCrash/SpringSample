package hello.util.http;

import java.io.IOException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomHttpRequestRetryHandler implements HttpRequestRetryHandler{
	private final static Logger log = LoggerFactory.getLogger(CustomHttpRequestRetryHandler.class);
	private final static int SLEEP_MS = 1000;
	private int retryCount;
	
	public CustomHttpRequestRetryHandler(int retryCount) {
		this.retryCount = retryCount;
	}
	
	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		if (executionCount >= retryCount) {
            // Do not retry if over max retry count
        	log.info("HttpRequestRetryHandlerretry exception: " + exception.toString());
        	log.info("HttpRequestRetryHandlerretry over max retry count.");
            return false;
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
            // Retry if the request is considered idempotent
        	log.info("HttpRequestRetryHandlerretry exception: " + exception.toString());
            log.info("HttpRequestRetryHandlerretry will sleep " + SLEEP_MS + "ms and retry.");
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        log.info("HttpRequestRetryHandlerretry exception: " + exception.toString());
        log.info("HttpRequestRetryHandlerretry will NOT retry.");
        return false;
	}

}
