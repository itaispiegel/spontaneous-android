package com.spontaneous.android.http;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * This class implements the {@link ErrorHandler} interface, and is used to specify custom actions in case the HTTP requests fail with an error.
 * In this case, the custom error handler will print the errors, with relevant text.
 */
public class CustomErrorHandler implements ErrorHandler {

    public CustomErrorHandler() {
    }

    @Override
    public Throwable handleError(RetrofitError error) {
        String errorDescription;

        if (error.getResponse() == null) {
            errorDescription = SpontaneousApplication.getInstance().getString(R.string.error_no_response);
        } else if (error.getKind() == RetrofitError.Kind.NETWORK) {
            errorDescription = SpontaneousApplication.getInstance().getString(R.string.error_network);
        } else {
            // Error message handling - return a simple error to Retrofit handlers..
            try {
                errorDescription = (String) error.getBodyAs(String.class);
            } catch (RuntimeException ex) {
                errorDescription = SpontaneousApplication.getInstance().getString(R.string.error_network_http_error) +
                        " " + error.getResponse().getStatus();
            }
        }

        return new Exception(errorDescription);
    }
}