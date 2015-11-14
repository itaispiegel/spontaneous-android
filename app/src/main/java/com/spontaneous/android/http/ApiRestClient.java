package com.spontaneous.android.http;

import android.content.Context;

import com.spontaneous.android.R;
import com.spontaneous.android.util.GsonFactory;
import com.spontaneous.android.util.Logger;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * This class is a configuration class for server requests.
 */
public final class ApiRestClient {

    private static final String BASE_URL = "https://spontaneous-server.herokuapp.com";
    private static RestAdapter sRestAdapter;

    private ApiRestClient() {
    }

    private static synchronized RestAdapter getRestAdapter(Context ctx) {
        if(sRestAdapter == null) {
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Content-Type", "application/json");
                    request.addHeader("Accept", "application/json");
                }
            };

            sRestAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setErrorHandler(new CustomErrorHandler(ctx))
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new GsonConverter(GsonFactory.getGson()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        }
        return sRestAdapter;
    }

    /**
     * @return an API request class.
     */
    public static <T> T getRequest(Context ctx, Class<T> request) {
        return getRestAdapter(ctx).create(request);
    }

    /**
     * Converts the complex error structure into a single string you can get with error.getLocalizedMessage() in Retrofit error handlers.
     * Also deals with there being no network available
     * <p/>
     * Uses a few string IDs for user-visible error messages
     */
    private static class CustomErrorHandler implements ErrorHandler {
        private final Context ctx;

        public CustomErrorHandler(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public Throwable handleError(RetrofitError cause) {
            String errorDescription;

            if(cause.getKind() == RetrofitError.Kind.NETWORK) {
                errorDescription = ctx.getString(R.string.error_network);
            } else {
                if(cause.getResponse() == null) {
                    errorDescription = ctx.getString(R.string.error_no_response);
                } else {

                    // Error message handling - return a simple error to Retrofit handlers..
                    try {
                        ErrorResponse errorResponse = (ErrorResponse) cause.getBodyAs(ErrorResponse.class);
                        errorDescription = errorResponse.error.data.message;
                    } catch(Exception ex) {
                        try {
                            errorDescription = ctx.getString(R.string.error_network_http_error, cause.getResponse().getStatus());
                        } catch(Exception ex2) {
                            Logger.error("handleError: " + ex2.getLocalizedMessage());
                            errorDescription = ctx.getString(R.string.error_unknown);
                        }
                    }
                }
            }

            return new Exception(errorDescription);
        }
    }

    public static class ErrorResponse {
        Error error;

        public static class Error {
            Data data;

            public static class Data {
                String message;
            }
        }
    }

}
