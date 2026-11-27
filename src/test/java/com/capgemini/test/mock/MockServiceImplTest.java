package com.capgemini.test.mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpMethod;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.capgemini.mock.MockServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MockServiceImplTest {
    
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MockServiceImpl mockService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mockService, "hostUrl", "http://localhost:1080");
    }

    @Test 
    public void testCheckDniOK() {
        // GIVEN
        String dni = "12345678A";

        ResponseEntity<Void> respuestaOK = ResponseEntity.status(HttpStatus.OK).build();

        when(restTemplate.exchange(
            eq("http://localhost:1080/check-dni"),
            eq(HttpMethod.PATCH),
            any(),
            any(Class.class)
        )).thenReturn(respuestaOK); 

        // WHEN
        try {
            boolean response = mockService.checkDni(dni);

            // THEN
            assertEquals(true, response);

            ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
            verify(restTemplate).exchange(eq("http://localhost:1080/check-dni"), eq(HttpMethod.PATCH),
            captor.capture(), eq(Void.class));
            assertTrue(captor.getValue().getBody().contains(dni));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test 
    public void testCheckDniErrorKO() {
        // GIVEN
        String dni = "12345678A";

        when(restTemplate.exchange(
            eq("http://localhost:1080/check-dni"),
            eq(HttpMethod.PATCH),
            any(),
            any(Class.class)
        )).thenThrow(new RestClientException("Error de mock")); 

        // WHEN
        // THEN
        assertThrows(RestClientException.class,
            () -> mockService.checkDni(dni));

        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("http://localhost:1080/check-dni"), eq(HttpMethod.PATCH),
        captor.capture(), eq(Void.class));
        assertTrue(captor.getValue().getBody().contains(dni));
    }

    @Test 
    public void emailUserOK() {
        // GIVEN
        String email = "a@b.com";
        String message = "Test message";

        ResponseEntity<Void> respuestaOK = ResponseEntity.status(HttpStatus.OK).build();

        when(restTemplate.exchange(
            eq("http://localhost:1080/email"),
            eq(HttpMethod.POST),
            any(),
            any(Class.class)
        )).thenReturn(respuestaOK); 

        // WHEN
        try {
            HttpStatusCode response = mockService.emailUser(email, message);

            // THEN
            assertEquals(HttpStatus.OK, response);

            ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
            verify(restTemplate).exchange(eq("http://localhost:1080/email"), eq(HttpMethod.POST),
            captor.capture(), eq(Void.class));
            assertTrue(captor.getValue().getBody().contains(email));
            assertTrue(captor.getValue().getBody().contains(message));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test 
    public void emailUserKO() {
        // GIVEN
        String email = "a@b.com";
        String message = "Test message";

        when(restTemplate.exchange(
            eq("http://localhost:1080/email"),
            eq(HttpMethod.POST),
            any(),
            any(Class.class)
        )).thenThrow(new RestClientException("Error de mock")); 

        // WHEN
        // THEN
        assertThrows(RestClientException.class,
            () -> mockService.emailUser(email, message));

        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("http://localhost:1080/email"), eq(HttpMethod.POST),
        captor.capture(), eq(Void.class));
        assertTrue(captor.getValue().getBody().contains(email));
        assertTrue(captor.getValue().getBody().contains(message));
    }

    @Test 
    public void sendSmsUserOK() {
        // GIVEN
        String phone = "98765432";
        String message = "Test message";

        ResponseEntity<Void> respuestaOK = ResponseEntity.status(HttpStatus.OK).build();

        when(restTemplate.exchange(
            eq("http://localhost:1080/sms"),
            eq(HttpMethod.POST),
            any(),
            any(Class.class)
        )).thenReturn(respuestaOK); 

        // WHEN
        try {
            HttpStatusCode response = mockService.sendSmsUser(phone, message);

            // THEN
            assertEquals(HttpStatus.OK, response);

            ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
            verify(restTemplate).exchange(eq("http://localhost:1080/sms"), eq(HttpMethod.POST),
            captor.capture(), eq(Void.class));
            assertTrue(captor.getValue().getBody().contains(phone));
            assertTrue(captor.getValue().getBody().contains(message));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test 
    public void sendSmsUserKO() {
        // GIVEN
        String phone = "98765432";
        String message = "Test message";

        when(restTemplate.exchange(
            eq("http://localhost:1080/sms"),
            eq(HttpMethod.POST),
            any(),
            any(Class.class)
        )).thenThrow(new RestClientException("Error de mock")); 

        // WHEN
        // THEN
        assertThrows(RestClientException.class,
            () -> mockService.sendSmsUser(phone, message));

        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("http://localhost:1080/sms"), eq(HttpMethod.POST),
        captor.capture(), eq(Void.class));
        assertTrue(captor.getValue().getBody().contains(phone));
        assertTrue(captor.getValue().getBody().contains(message));
    }
}
