package com.cw.updateuifromchildthread;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyDemo {
    private RequestQueue requestQueue;

    private RequestQueue getRequestQueue(Context context) {
        if (null == requestQueue) {
            requestQueue = Volley.newRequestQueue(context);
        }

        return requestQueue;
    }

    public void testStringVolley(Context context) {
        StringRequest stringRequest = new StringRequest("https://www.baidu.com/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constant.TAG, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constant.TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        getRequestQueue(context).add(stringRequest);
    }


    public void testJsonVolley(Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://www.baidu.com/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(Constant.TAG, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constant.TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        getRequestQueue(context).add(jsonObjectRequest);
    }

}
