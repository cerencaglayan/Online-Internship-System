package com.g7.payload.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class Response<T> {
    private String message;
    private T data;

    public static <T> ResponseEntity<Response<T>> ok(T data, String message) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> badRequest(String message){
        Response<T> response = new Response<>();
        response.setMessage(message);
        return ResponseEntity.badRequest().body(response);
    }


    public static <T> ResponseEntity<Response<T>> status(HttpStatus status, T data, String message) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setMessage(message);
        return ResponseEntity.status(status).body(response);
    }
}
