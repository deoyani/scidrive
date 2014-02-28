/*******************************************************************************
 * Copyright 2013 Johns Hopkins University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package edu.jhu.pha.vospace.keystone;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import edu.jhu.pha.vospace.jobs.MyHttpConnectionPoolProvider;

public class KeystoneToken {
	private JsonNode tokenNode;
	
	public KeystoneToken(String token) throws KeystoneException {
		HttpClient client = MyHttpConnectionPoolProvider.getHttpClient();
		
		HttpGet tokenGet = new HttpGet("http://zinc26.pha.jhu.edu:5000/v2.0/tokens/"+token);
		tokenGet.setHeader("X-Auth-Token", "PQjnt72Q1z");
		
		try {
			HttpResponse tokenResp = client.execute(tokenGet);
			if(tokenResp.getStatusLine().getStatusCode() != 200)
				throw new KeystoneException(Response.Status.UNAUTHORIZED, "Keystone");

			tokenNode = new ObjectMapper().readValue(tokenResp.getEntity().getContent(), JsonNode.class);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new KeystoneException(Response.Status.BAD_REQUEST, null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new KeystoneException(Response.Status.BAD_REQUEST, null);
		}
	}

	String getUserId() {
		return tokenNode.path("access").path("user").path("id").getTextValue();
	}
	
	String getUsername() {
		return tokenNode.path("access").path("user").path("username").getTextValue();
	}
	
	Set<String> getRoles() {
		JsonNode rolesNode = tokenNode.path("access").path("user").path("roles");
		if(rolesNode.isMissingNode() || !rolesNode.isArray())
			throw new KeystoneException(Response.Status.BAD_REQUEST, null);
		HashSet<String> roles = new HashSet<String>(); 
		for(JsonNode roleNode: rolesNode) {
			roles.add(roleNode.path("name").getTextValue());
		}
		return roles;
	}
}
