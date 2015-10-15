package ru.kraynov.app.ssaknitu.events.sdk.api.method;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import ru.kraynov.app.ssaknitu.events.sdk.SDK;

public class PushMethod {

    public Push getInstance(){
        return SDK.getInstance().getRestAdapterEvents().create(Push.class);
    }

    public interface Push {
        @FormUrlEncoded
        @POST("/push.subscribe")
        void subscribe(@Field("type") int type, @Field("token") String token, Callback<JsonObject> cb);

        @FormUrlEncoded
        @POST("/push.unsubscribe")
        void unsubscribe(@Field("type") int type, Callback<JsonObject> cb);
    }
}
