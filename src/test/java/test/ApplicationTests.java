package test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import hello.Application;
import hello.entity.Demo;
import hello.entity.MongoDemo;
import hello.util.http.HttpClientUtil;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class ApplicationTests {
	@Autowired
	RestTemplate restTemplate;
	@Test
	public void testGreeting() throws Exception {
//		ResponseEntity<String> entity2 = restTemplate.getForEntity("https://localhost:8443/greeting", String.class);
//		Assert.assertEquals(true, entity2.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void testSave() throws Exception {
		String name = "test03";
		Demo demo = new Demo();
		demo.setName(name);
		demo.setCreateTime(new Date());
		String result = restTemplate.postForObject("https://localhost:8443/save", demo, String.class);
		Assert.assertEquals("save "+ name +" success", result);
	}
	@Test
	public void testFindAll() throws Exception {
		ResponseEntity<String> entity = restTemplate.getForEntity("https://localhost:8443/list", String.class);
		Assert.assertEquals(true, entity.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void testMongoSave() throws Exception {
		String name = "test01";
		MongoDemo demo = new MongoDemo();
		demo.setId(new ObjectId());
		demo.setName(name);
		demo.setCreateTime(new Date());
		String result = restTemplate.postForObject("https://localhost:8443/mongo/save", demo, String.class);
		Assert.assertEquals("save "+ name +" success", result);
	}
	@Test
	public void testMongoFindAll() throws Exception {
		ResponseEntity<String> entity = restTemplate.getForEntity("https://localhost:8443/mongo/list", String.class);
		Assert.assertEquals(true, entity.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void testMongoDemo() throws Exception {
		restTemplate.delete("https://localhost:8443/mongo/delete/5cc5768d86b8f53d61b963dc");
	}
}
