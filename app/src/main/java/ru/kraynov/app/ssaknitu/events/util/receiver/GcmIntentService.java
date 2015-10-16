package ru.kraynov.app.ssaknitu.events.util.receiver;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.Push;
import ru.kraynov.app.ssaknitu.events.util.helper.SharedPreferencesHelper;
import ru.kraynov.app.ssaknitu.events.view.activity.EvFragmentContainerActivity;
import ru.kraynov.app.ssaknitu.events.view.fragment.EventFragment;
import ru.kraynov.app.ssaknitu.events.view.fragment.PostWebFragment;

public class GcmIntentService extends IntentService {

    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras);
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle bundle) {
        Push push = new Push(bundle);

        Log.e("test", "events "+SharedPreferencesHelper.getInstance().getBoolean(SharedPreferencesHelper.PREFS.PUSH_EVENTS_ENABLE, true));
        Log.e("test", "posts "+SharedPreferencesHelper.getInstance().getBoolean(SharedPreferencesHelper.PREFS.PUSH_POSTS_ENABLE, true));
        Log.e("test", "news "+SharedPreferencesHelper.getInstance().getBoolean(SharedPreferencesHelper.PREFS.PUSH_NEWS_ENABLE, true));

        if (!SharedPreferencesHelper.getInstance().getBoolean(SharedPreferencesHelper.PREFS.PUSH_EVENTS_ENABLE, true) && push.type==0) return;
        if (!SharedPreferencesHelper.getInstance().getBoolean(SharedPreferencesHelper.PREFS.PUSH_POSTS_ENABLE, true) && push.type==1) return;
        if (!SharedPreferencesHelper.getInstance().getBoolean(SharedPreferencesHelper.PREFS.PUSH_NEWS_ENABLE, true) && push.type==2) return;

        if (push.type==0){
            ArrayList<Integer> orgs = new Gson().fromJson(SharedPreferencesHelper.getInstance().getString(SharedPreferencesHelper.PREFS.APP_PUSH_EVENTS_ORGS), new TypeToken<ArrayList<Integer>>(){}.getType());
            boolean isIncluded = false;

            for (int org : orgs){
                if (org == push.organisation) {
                    isIncluded = true;
                    break;
                }
            }

            if (!isIncluded) return;
        }

        if (push.type==2){
            if (push.god!=1){
                ArrayList<Integer> orgs = new Gson().fromJson(SharedPreferencesHelper.getInstance().getString(SharedPreferencesHelper.PREFS.APP_PUSH_NEWS_ORGS), new TypeToken<ArrayList<Integer>>(){}.getType());
                boolean isIncluded = false;

                for (int org : orgs){
                    if (org ==push.organisation) {
                        isIncluded = true;
                        break;
                    }
                }

                if (!isIncluded) return;
            }
        }

        PendingIntent contentIntent = null;
        String contentText = "";
        String contentTitle = getString(R.string.notify);
        int ID = 100500;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        switch (push.type){
            case 0:
                ID = push.id;
                contentTitle = push.organisation_name;
                contentText = push.title;

                contentIntent = PendingIntent.getActivity(this, ID,
                        new Intent(this, EvFragmentContainerActivity.class)
                                .putExtra(EvFragmentContainerActivity.ARG_FRAGMENT_ID, 1)
                                .putExtra(EventFragment.ARG_EVENT_ID, push.id)
                                .putExtra(EvFragmentContainerActivity.ARG_IS_FADING, true),
                        PendingIntent.FLAG_ONE_SHOT);
                break;
            case 1:
                ID = push.id;
                contentTitle = getString(R.string.post);
                contentText = push.title;

                contentIntent = PendingIntent.getActivity(this, ID,
                        new Intent(this, EvFragmentContainerActivity.class)
                                .putExtra(EvFragmentContainerActivity.ARG_FRAGMENT_ID, 2)
                                .putExtra(PostWebFragment.ARG_POST_ID, push.id),
                        PendingIntent.FLAG_ONE_SHOT);
                break;
            case 2:
                contentTitle = push.organisation_name;
                contentText = push.title;
                if (push.web_url.length()>0){
                    contentIntent = PendingIntent.getActivity(this, ID,
                            new Intent(Intent.ACTION_VIEW, Uri.parse(push.web_url)),
                            PendingIntent.FLAG_ONE_SHOT);
                }
                break;
        }

        Uri soundUri = SharedPreferencesHelper.getInstance().getBoolean(SharedPreferencesHelper.PREFS.APP_PUSH_SOUND, false ) ?
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) : null;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_event_white_48dp)
                        .setContentTitle(contentTitle)
                        .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                        .setAutoCancel(true)
                        .setContentText(contentText)
                        .setColor(getResources().getColor(R.color.material_red_500))
                        .setSound(soundUri);

        if (contentIntent!=null) mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(ID, mBuilder.build());
    }
}
