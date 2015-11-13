package com.spontaneous.android.http.response;

/**
 * Created by eidan on 5/24/15.
 */
public class BaseResponse<T> {
    private int statusCode;
    private String description;
    private T body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
