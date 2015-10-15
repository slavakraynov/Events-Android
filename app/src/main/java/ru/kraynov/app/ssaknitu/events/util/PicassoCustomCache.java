package ru.kraynov.app.ssaknitu.events.util;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class PicassoCustomCache {
    static Picasso sPicasso;
    private static final String CACHE_PATH = "picasso-custom-cache";
    private final int CACHE_SIZE = 30;

    public static Picasso getPicassoCustomCache(Context context){
        if (sPicasso==null) {
            context = context.getApplicationContext();

            File cacheDir = createDefaultCacheDir(context, CACHE_PATH);

            OkHttpClient httpClient = new OkHttpClient();
            Cache cache = null;

            try {
                cache = new Cache(cacheDir, 1 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }

            httpClient.setCache(cache);

            sPicasso = new Picasso
                    .Builder(context)
                    .downloader(new OkHttpDownloader(httpClient))
                    .build();
        }

        return sPicasso;
    }

    static File createDefaultCacheDir(Context context, String path) {
        File cacheDir = context.getApplicationContext().getExternalCacheDir();
        if (cacheDir == null) cacheDir = context.getApplicationContext().getCacheDir();
        File cache = new File(cacheDir, path);
        if (!cache.exists()) {
            cache.mkdirs();
        }

        return cache;
    }
}