package ru.kraynov.app.ssaknitu.events.sdk.api.method;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.kraynov.app.ssaknitu.events.sdk.SDK;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.EventModel;

public class EventMethod {

    public Event getInstance(){
        return SDK.getInstance().getRestAdapterEvents().create(Event.class);
    }

    public interface Event {
        @GET("/events.get")
        void events(Callback<ArrayList<EventModel>> cb);

        @GET("/events.get")
        void events(@Query("id") int id, Callback<EventModel> cb);
    }
}
