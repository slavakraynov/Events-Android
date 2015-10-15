package ru.kraynov.app.ssaknitu.events.sdk.api.method;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import ru.kraynov.app.ssaknitu.events.sdk.SDK;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.OrganisationModel;

public class OrganisationMethod {

    public Organisation getInstance(){
        return SDK.getInstance().getRestAdapterEvents().create(Organisation.class);
    }

    public interface Organisation {
        @GET("/organisations.get.id")
        void organisationsId(Callback<ArrayList<OrganisationModel>> cb);
    }
}
