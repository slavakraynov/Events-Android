package ru.kraynov.app.ssaknitu.events.util.helper;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class TypeFaceHelper {

    public static final String TYPEFACE_FOLDER = "fonts";

    private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(
            4);

    public static Typeface getTypeFace(Context context, String fileName) {
        Typeface tempTypeface = sTypeFaces.get(fileName);

        if (tempTypeface == null) {
            String fontPath = new StringBuilder(TYPEFACE_FOLDER).append('/').append(fileName).toString();
            tempTypeface = Typeface.createFromAsset(context.getAssets(), fontPath);
            sTypeFaces.put(fileName, tempTypeface);
        }

        return tempTypeface;
    }
}
