package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import hello.Application;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class ApplicationTests {
	@Autowired
	RestTemplate restTemplate;
	@Test
	public void testGreeting() throws Exception {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/greeting", String.class);
		Assert.assertEquals(true, entity.getStatusCode().is2xxSuccessful());
		
//		ResponseEntity<String> entity2 = restTemplate.getForEntity("https://localhost:8443/greeting", String.class);
//		Assert.assertEquals(true, entity2.getStatusCode().is2xxSuccessful());
	}


}
