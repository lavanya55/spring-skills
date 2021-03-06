/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spring.skills.samples;
import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class HelloSkillTests {
	
	@Autowired
	TestRestTemplate rest;
	
	@Autowired
	ApplicationContext context;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testSimpleIntentRequest() throws IOException, JSONException {
		String intentRequestJSON = readToString("/simple-intent-request.json");
		String expectedResponseJSON = readToString("/simple-intent-response.json");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(intentRequestJSON, headers);
		String response = rest.postForObject("/alexa", requestEntity, String.class);
		JSONAssert.assertEquals(expectedResponseJSON, response, false);
	}
	
	@Test
	public void testSimpleWebhookRequest() throws IOException, JSONException {
		String webhookRequestJSON = readToString("/simple-webhook-request.json");
		String expectedResponseJSON = readToString("/simple-webhook-response.json");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(webhookRequestJSON, headers);
		String response = rest.postForObject("/google", requestEntity, String.class);
		JSONAssert.assertEquals(expectedResponseJSON, response, false);
	}

	private String readToString(String path) throws IOException {
		ClassPathResource resource = new ClassPathResource(path);
		String intentRequestBody = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
		return intentRequestBody;
	}

}
