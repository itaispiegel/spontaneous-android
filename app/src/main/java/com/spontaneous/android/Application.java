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

public class Application extends android.app.Application {
    private static Application sInstance;

    //ImageLoader
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    /**
     * Base URL of the server.
     */
    private static final String BASE_URL = "https://spontaneous-server.herokuapp.com";

    /**
     * Rest adapter for HTTP requests.
     */
    private RestAdapter restAdapter;

    public static Application getInstance() {
        return sInstance;
    }

    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mRequestQueue = Volley.newRequestQueue(this);

        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10000);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

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

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public <T> T getService(Class<T> serviceClass) {
        return restAdapter.create(serviceClass);
    }
}