package system.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServerConfig {
	@Value("${server.port}")
	private Integer httpsPort;
	
	@Value("${server.http.port}")
	private Integer httpPort;
	
    @Bean
    public ServletWebServerFactory servletWebServerFactory(){
    	 TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
    		 @Override
    		protected void postProcessContext(Context context) {
    			 SecurityConstraint securityConstraint = new SecurityConstraint();
    			 securityConstraint.setUserConstraint("CONFIDENTIAL");
    			 SecurityCollection securityCollection = new SecurityCollection();
    			 securityCollection.addPattern("/*");
    			 securityConstraint.addCollection(securityCollection);
    			 context.addConstraint(securityConstraint);
    		}
    	 };
         factory.addAdditionalTomcatConnectors(httpConnector());
         return factory;
    }

    @Bean
    public Connector httpConnector(){
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);
        return  connector;
    }
}
