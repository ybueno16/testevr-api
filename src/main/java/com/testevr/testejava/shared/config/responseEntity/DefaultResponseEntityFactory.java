package com.testevr.testejava.shared.config.responseEntity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DefaultResponseEntityFactory {

    public static ResponseEntity<DefaultResponseEntity> create(
            String pMessage, Object pObject, HttpStatus pHttpStatus) {
        System.out.println(pMessage);

        return new ResponseEntity<>(
                new DefaultResponseEntity(pMessage, pObject), pHttpStatus);
    }
}
