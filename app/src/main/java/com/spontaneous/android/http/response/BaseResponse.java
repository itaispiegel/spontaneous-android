package com.spontaneous.android.http.response;

/**
 * This class holds data returned from the REST API.
 */
public class BaseResponse<T> {

    /**
     * Status code of the HTTP Request.
     */
    private int statusCode;

    /**
     * The data itself.
     */
    private T body;

    // -----Response codes-----

    /**
     * Request was success.
     */
    public static final int SUCCESS = 0;

    /**
     * Request ended with error.
     */
    public static final int INTERNAL_ERROR = -1;
    //----------

    /**
     * Create a response with a body. Default response code is ResponseCodes.SUCCESS.
     */
    public BaseResponse(T body) {
        this.statusCode = SUCCESS;
        this.body = body;
    }

    /**
     * Create a response code with status code and body.
     */
    public BaseResponse(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * @return Status code of the HTTP Request.
     */
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return The data itself.
     */
    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
