package com.spontaneous.android;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.spontaneous.android.http.CustomErrorHandler;
import com.spontaneous.android.util.GsonFactory;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class SpontaneousApplication extends android.app.Application {

    /**
     * The instance of the application.
     */
    private static SpontaneousApplication sInstance;

    /**
     * Base URL of the server.
     */
    private static final String BASE_URL = "https://spontaneous-server.herokuapp.com";

    /**
     * Rest adapter for HTTP requests.
     */
    private RestAdapter restAdapter;

    /**
     * A queue of all image loading requests.
     */
    private RequestQueue mRequestQueue;

    /**
     * The image loader object.
     */
    private ImageLoader mImageLoader;

    /**
     * @return Instance of the application.
     */
    public static SpontaneousApplication getInstance() {
        return sInstance;
    }

    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mRequestQueue = Volley.newRequestQueue(this);

        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final int maxCacheSize = 10000;
            private final LruCache<String, Bitmap> mCache = new LruCache<>(maxCacheSize);

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
            }
        };

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setErrorHandler(new CustomErrorHandler())
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new GsonConverter(GsonFactory.getGson()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    /**
     * @return {@link #mRequestQueue}
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * @return {@link #mImageLoader}
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * @param serviceClass The class of the request service.
     * @param <T>          The type of service.
     * @return An HTTP request service class.
     */
    public <T> T getService(Class<T> serviceClass) {
        return restAdapter.create(serviceClass);
    }
}