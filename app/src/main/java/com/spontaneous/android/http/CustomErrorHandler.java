package com.spontaneous.android.http;

import com.spontaneous.android.Application;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by USER1 on 10/01/2016.
 */
public class CustomErrorHandler implements ErrorHandler {

    public CustomErrorHandler() {
    }

    @Override
    public Throwable handleError(RetrofitError error) {
        String errorDescription;

        if (error.getResponse() == null) {
            errorDescription = Application.getInstance().getString(R.string.error_no_response);
        } else if (error.getKind() == RetrofitError.Kind.NETWORK) {
            errorDescription = Application.getInstance().getString(R.string.error_network);
        } else {
            // Error message handling - return a simple error to Retrofit handlers..
            try {
                errorDescription = (String) error.getBodyAs(String.class);
            } catch (RuntimeException ex) {
                errorDescription = Application.getInstance().getString(R.string.error_network_http_error, error.getResponse().getStatus());
            }
        }

        return new Exception(errorDescription);
    }
}