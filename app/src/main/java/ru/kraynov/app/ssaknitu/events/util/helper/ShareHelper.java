package ru.kraynov.app.ssaknitu.events.util.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ShareHelper {
   public static void shareText(Context context,String text){
       Intent sendIntent = new Intent();
       sendIntent.setAction(Intent.ACTION_SEND);
       sendIntent.putExtra(Intent.EXTRA_TEXT, text);
       sendIntent.setType("text/plain");
       context.startActivity(Intent.createChooser(sendIntent, "Отправить через"));
   }

    public static void copyText(Context context, String title, String url) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(title, url);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
    }
}
