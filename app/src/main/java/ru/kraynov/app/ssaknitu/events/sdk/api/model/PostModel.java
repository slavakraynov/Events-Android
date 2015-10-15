package ru.kraynov.app.ssaknitu.events.sdk.api.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PostModel implements Serializable {
    public int id;
    public String type;
    public String slug;
    public String url;
    public String status;
    public String title;
    public String title_plain;
    public String content;
    public String excerpt;
    public String date;
    public String modified;
    public int comment_count;
    public String comment_status;
    public ArrayList<Categories> categories;
    public Author author;
    public ThumbnailImages thumbnail_images;
    public CustomFields custom_fields;

    public class Categories implements Serializable{
        public int id;
        public String slug;
        public String title;
        public String description;
        public int parent;
        public int post_count;
    }

    public class Author implements Serializable{
        public int id;
        public String slug;
        public String name;
        public String first_name;
        public String last_name;
        public String nickname;
        public String url;
        public String description;
    }

    public class ThumbnailImages implements Serializable{
        public ThumbnailImage full;
        public ThumbnailImage thumbnail;
        public ThumbnailImage medium;
    }

    public class ThumbnailImage implements Serializable{
        public String url;
        public int height;
        public int width;
    }

    public class CustomFields implements Serializable{
        public ArrayList<String> views;
    }
}
