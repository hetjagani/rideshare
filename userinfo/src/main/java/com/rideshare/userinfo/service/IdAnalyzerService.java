package com.rideshare.userinfo.service;

import com.rideshare.userinfo.webentity.IdAnalyzerRequest;
import com.rideshare.userinfo.webentity.IdAnalyzerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IdAnalyzerService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.idanalyzer.apikey}")
    private String apiKey;

    @Value("${app.idanalyzer.url}")
    private String apiUrl;


    public IdAnalyzerResponse verifyLicense(IdAnalyzerRequest request) {
        try {
            request.setApikey(apiKey);
            request.setAccuracy(1);
            request.setAuthenticate(true);
            request.setVerify_expiry(true);
            HttpEntity<IdAnalyzerRequest> httpEntity = new HttpEntity<IdAnalyzerRequest>(request);

            ResponseEntity<IdAnalyzerResponse> response = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, IdAnalyzerResponse.class);

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
