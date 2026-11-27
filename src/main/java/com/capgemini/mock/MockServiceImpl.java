package com.capgemini.mock;
import org.slf4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.capgemini.servicio.DniParameter;
import com.capgemini.servicio.EmailParameter;
import com.capgemini.servicio.PhoneParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Service
public class MockServiceImpl implements MockService {

    private static final Logger logger = LoggerFactory.getLogger(MockServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.service.url}")
    private String hostUrl;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    private HttpStatusCode conexionMock(@NonNull String url, String body, @NonNull HttpMethod method) throws Exception {
        HttpStatusCode result = null;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
        new HttpEntity<>(body, headers);

        ResponseEntity<Void> response =
                restTemplate.exchange(
                    url,
                    method,
                    request,
                    Void.class);

        logger.info("Código: " + response.getStatusCode());
        result = response.getStatusCode();

        return result;
    }

    @Override
    public boolean checkDni(String dni) throws Exception {
        DniParameter dniObjeto = new DniParameter(dni);
        String body = objectMapper.writeValueAsString(dniObjeto);
        HttpStatusCode result = conexionMock(hostUrl + "/check-dni", body, HttpMethod.PATCH);
        return HttpStatus.OK.equals(result);
    }

    @Override
    public HttpStatusCode emailUser(String email, String message) throws Exception {
        EmailParameter emailObjeto = new EmailParameter(email, message);
        String body = objectMapper.writeValueAsString(emailObjeto);
        HttpStatusCode result = conexionMock(hostUrl + "/email", body, HttpMethod.POST);
        return result;
    }

    @Override
    public HttpStatusCode sendSmsUser(String phone, String message) throws Exception {
        PhoneParameter phoneObjeto = new PhoneParameter(phone, message);
        String body = objectMapper.writeValueAsString(phoneObjeto);
        HttpStatusCode result = conexionMock(hostUrl + "/sms", body, HttpMethod.POST);
        return result;
    }
    
}
