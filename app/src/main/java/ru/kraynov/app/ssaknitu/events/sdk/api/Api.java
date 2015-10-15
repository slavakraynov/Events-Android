package ru.kraynov.app.ssaknitu.events.sdk.api;

import ru.kraynov.app.ssaknitu.events.sdk.api.method.EventMethod;
import ru.kraynov.app.ssaknitu.events.sdk.api.method.OrganisationMethod;
import ru.kraynov.app.ssaknitu.events.sdk.api.method.PostsMethod;
import ru.kraynov.app.ssaknitu.events.sdk.api.method.PushMethod;

public class Api {
    public static EventMethod.Event Events(){
        return new EventMethod().getInstance();
    }

    public static PushMethod.Push Push(){
        return new PushMethod().getInstance();
    }

    public static PostsMethod.Posts Posts(){
        return new PostsMethod().getInstance();
    }

    public static OrganisationMethod.Organisation Organisations(){
        return new OrganisationMethod().getInstance();
    }
}
