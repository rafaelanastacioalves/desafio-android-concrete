package com.example.rafaelanastacioalves.desafioandroid.retrofit;

/**
 * Created by rafaelalves on 19/05/17.
 */

@SuppressWarnings({"DefaultFileTemplate", "unused"})
public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}