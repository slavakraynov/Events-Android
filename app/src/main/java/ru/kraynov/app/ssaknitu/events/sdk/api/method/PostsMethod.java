package ru.kraynov.app.ssaknitu.events.sdk.api.method;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.kraynov.app.ssaknitu.events.sdk.SDK;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostResultModel;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostsResultModel;

public class PostsMethod {

    public Posts getInstance(){
        return SDK.getInstance().getRestAdapterSsaKnitu().create(Posts.class);
    }

    public interface Posts {
        @GET("/get_recent_posts/?exclude=content,attachments")
        void posts(@Query("count") int count, @Query("page") int page, Callback<PostsResultModel> cb);

        @GET("/get_post/?exclude=content,attachments")
        void post(@Query("id") int id, Callback<PostResultModel> cb);
    }
}
