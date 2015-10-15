package ru.kraynov.app.ssaknitu.events.sdk.api.model;

import android.os.Bundle;

/**
 * messageType [0 - event, 1 - post, 2 - news]
 */
public class Push {
    public int id;
    public int type;
    public int organisation;
    public String organisation_name;
    public int god;
    public String title;
    public String description;
    public String web_url;

    public Push(int id, int type, int organisation, int god, String title, String description, String organisation_name, String web_url){
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.organisation = organisation;
        this.god = god;
        this.organisation_name = organisation_name;
        this.web_url = web_url;
    }

    public Push(Bundle bundle){
        this(
                Integer.parseInt(bundle.getString("id", "-1")),
                Integer.parseInt(bundle.getString("type", "-1")),
                Integer.parseInt(bundle.getString("organisation", "-1")),
                Integer.parseInt(bundle.getString("god", "0")),
                bundle.getString("title", "null"),
                bundle.getString("description", ""),
                bundle.getString("organisation_name",""),
                bundle.getString("web_url", "")
        );
    }
}
