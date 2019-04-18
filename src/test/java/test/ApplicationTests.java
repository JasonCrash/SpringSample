package test;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import hello.Application;
import hello.util.http.HttpClientUtil;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class ApplicationTests {
	@Autowired
	RestTemplate restTemplate;
	@Test
	public void testGreeting() throws Exception {
		ResponseEntity<String> entity = restTemplate.getForEntity("https://129.40.179.215/cloud/cloud/v1/api/chaincode/secretToken?email=aa", String.class);
		Assert.assertEquals(true, entity.getStatusCode().is2xxSuccessful());
		
//		ResponseEntity<String> entity2 = restTemplate.getForEntity("https://localhost:8443/greeting", String.class);
//		Assert.assertEquals(true, entity2.getStatusCode().is2xxSuccessful());
	}
	
//	@Test
//	public void testGetRequest() {
//		String url = "https://129.40.179.215/cloud/cloud/v1/api/chaincode/secretToken?email=aa";
//		HttpClientUtil.get(url, 5000, 1);
//	}
//	@Test
//	public void testGetRequestWithCert() {
//		String url = "https://129.40.179.215/cloud/cloud/v1/api/chaincode/secretToken?email=aa";
//		HttpClientUtil.getWithCert(url);
//	}
//	
//	@Test
//	public void testPostRequest() {
//		String url = "https://129.40.179.215/cloud/cloud/v1/check/email";
//		JSONObject data = new JSONObject().put("email", "zfr").put("flag", "vm");
//		HttpClientUtil.post(url, data.toString(), 5000, 1);
//	}
//	
//	@Test
//	public void testPostWithCert() {
//		String url = "https://129.40.179.215/cloud/cloud/v1/check/email";
//		JSONObject data = new JSONObject().put("email", "zfr").put("flag", "vm");
//		HttpClientUtil.postWithCert(url, data.toString(), 5000, 1);
//	}
//	
//	@Test
//	public void testDeleteRequest() {
//		String url = "https://127.0.0.1:8443/delete/1";
//		HttpClientUtil.delete(url,1000, 2);
//	}
//	@Test
//	public void testDeleteWithCert() {
//		String url = "https://127.0.0.1:8443/delete/1";
//		HttpClientUtil.deleteWithCert(url,1000, 2);
//	}
}
