package ru.kraynov.app.ssaknitu.events.sdk;

import android.content.Context;
import android.content.Intent;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import ru.kraynov.app.ssaknitu.events.sdk.util.UtilsHelper;

public class SDK {
    public static final int SDK_REQUEST_CODE = 100;
    private static SDK sSDK;
    private final Context mContext;

    public static final String SERVER_URL_EVENTS = "http://events.ssaknitu.ru/api/v1";
    public static final String SERVER_URL_SSA_KNITU = "http://ssaknitu.ru/wp-api/";
    private RestAdapter mRestAdapterEvents;
    private RestAdapter mRestAdapterSsaKnitu;

    private SDK(Context context){
        mContext = context;
    }

    public static void initialize(final Context context){
        if (sSDK==null) {
            sSDK = new SDK(context);
            sSDK.mRestAdapterEvents = new RestAdapter.Builder()
                    .setEndpoint(SERVER_URL_EVENTS)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader("Accept", "application/json");
                            request.addHeader("Device-Info", UtilsHelper.getDeviceInfo(context));
                        }
                    })
                    .build();
            sSDK.mRestAdapterSsaKnitu = new RestAdapter.Builder()
                    .setEndpoint(SERVER_URL_SSA_KNITU)
                    .build();
        }
    }

    public static SDK getInstance(){
        return sSDK;
    }

    public RestAdapter getRestAdapterEvents(){
        return mRestAdapterEvents;
    }

    public RestAdapter getRestAdapterSsaKnitu(){
        return mRestAdapterSsaKnitu;
    }


    public void processActivityResult(int resultCode, Intent data) {

    }
}
