package com.example.SmartHome.Connection;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private Context mCtx;
    private RequestQueue mRequestQueu;
    private static MySingleton mInstance;

    public MySingleton(Context mCtx) {
        this.mCtx = mCtx;
        mRequestQueu = gwtmRequestQueu();
    }

    private RequestQueue gwtmRequestQueu() {
        if(mRequestQueu == null){
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueu = new RequestQueue(cache, network);
            mRequestQueu = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
        return mRequestQueu;
    }

    public static synchronized MySingleton getmInstance(Context context){
        if (mInstance == null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }
    public <T> void addToRequestQueue(Request <T> request){
        mRequestQueu.add(request);
    }

    public static String getUrl(){
        String url = "http://192.168.0.104/LoginRegister/";
        return url;
    }
}
