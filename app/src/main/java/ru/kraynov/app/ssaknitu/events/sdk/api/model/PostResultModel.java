package ru.kraynov.app.ssaknitu.events.sdk.api.model;

import java.io.Serializable;

public class PostResultModel implements Serializable {
    public String status;
    public PostModel post;
    public String previous_url;
    public String next_url;
}
