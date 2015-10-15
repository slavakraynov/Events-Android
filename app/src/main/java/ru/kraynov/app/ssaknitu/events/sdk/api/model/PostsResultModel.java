package ru.kraynov.app.ssaknitu.events.sdk.api.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PostsResultModel implements Serializable{
    public String status;
    public int count;
    public int count_total;
    public int pages;
    public ArrayList<PostModel> posts;
}
