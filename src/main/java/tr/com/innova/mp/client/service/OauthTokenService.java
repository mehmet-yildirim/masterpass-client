package tr.com.innova.mp.client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;

/**
 * Created By : uunsal on  12.04.2021
 **/
@Service
public class OauthTokenService {

    @Value("${api.security.oauth.client-id}")
    private String clientId;

    @Value("${api.security.oauth.client-secret}")
    private String clientSecret;

    @Value("${api.security.oauth.authorization-server-url}")
    private String authServerURI;

    public String retriveOauthToken() {

        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        var clientInformation = clientId + ":" + clientSecret;
        headers.add("authorization", "Basic ".concat(Base64.getEncoder().encodeToString(clientInformation.getBytes())));
        var map = new LinkedMultiValueMap<String, Object>();
        map.add("grant_type", "client_credentials");

        var request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

        var response = restTemplate.postForObject(authServerURI, request, HashMap.class);
        return response != null ? response.get("access_token").toString() : null;
    }
}
