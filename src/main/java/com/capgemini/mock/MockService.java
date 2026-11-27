package com.capgemini.mock;

import org.springframework.http.HttpStatusCode;

public interface MockService {
    
    boolean checkDni(String dni) throws Exception;

    HttpStatusCode emailUser(String email, String message) throws Exception;

    HttpStatusCode sendSmsUser(String phone, String message) throws Exception;

}
