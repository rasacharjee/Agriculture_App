package com.example.myapplication.Admin.AdoDdoActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Ado.AdoListAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoDdoCompleted extends Fragment {

    private String mDdoId;
    private ArrayList<String> locationNames;
    private ArrayList<String> locationAddresses;
    private ArrayList<String> mAdoNames;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private AdoListAdapter adapter;
    private String nextUrl;
    private boolean isDdo;
    private String token;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;


    public AdoDdoCompleted() {
        // Required empty public constructor
    }

    public AdoDdoCompleted(String mDdoId, boolean isDdo) {
        this.mDdoId = mDdoId;
        this.isDdo = isDdo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ddo_completed, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull5);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(AdoDdoCompleted.this).attach(AdoDdoCompleted.this).commit();
            }
        });
        String role;
        if (isDdo)
            role = "dda";
        else
            role = "ado";
        String mUrl = "http://18.224.202.135/api/admin/" + role + "/" + mDdoId + "/completed";
        Log.d("url", "onCreateView: completed" + mUrl);
        progressBar = view.findViewById(R.id.Ddo_completed_loading);
        recyclerView = view.findViewById(R.id.Ddo_completed_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        locationNames = new ArrayList<>();
        locationAddresses = new ArrayList<>();
        mAdoNames = new ArrayList<>();
        if (isDdo)
            adapter = new AdoListAdapter(getActivity(), locationNames, locationAddresses, mAdoNames, true);
        else
            adapter = new AdoListAdapter(getActivity(), locationNames, locationAddresses, mAdoNames, false);
        recyclerView.setAdapter(adapter);
        getData(mUrl);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        loadNextLocations(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    private void getData(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            if(resultsArray.length()== 0){
                                adapter.mshowshimmer = false;
                                adapter.notifyDataSetChanged();

                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                if (isDdo) {
                                    try {
                                        JSONObject adoObject = singleObject.getJSONObject("ado");
                                        String adoName = adoObject.getString("name");
                                        mAdoNames.add(adoName);
                                    } catch (JSONException e) {
                                        mAdoNames.add("Not Assigned");
                                    }
                                }
                                String locName = singleObject.getString("village_name");
                                String locAdd = singleObject.getString("block_name") +
                                        ", " + singleObject.getString("district");
                                locationNames.add(locName);
                                locationAddresses.add(locAdd);
                            }
                            adapter.mshowshimmer = false;
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void loadNextLocations(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        if (!url.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                JSONArray resultsArray = rootObject.getJSONArray("results");
                                if(resultsArray.length()== 0){
                                    adapter.mshowshimmer = false;
                                    adapter.notifyDataSetChanged();

                                    view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                    //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                                }
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject singleObject = resultsArray.getJSONObject(i);
                                    if (isDdo) {
                                        try {
                                            JSONObject adoObject = singleObject.getJSONObject("ado");
                                            String adoName = adoObject.getString("name");
                                            mAdoNames.add(adoName);
                                        } catch (JSONException e) {
                                            mAdoNames.add("Not Assigned");
                                        }
                                    }
                                    String locName = singleObject.getString("village_name");
                                    String locAdd = singleObject.getString("block_name") +
                                            ", " + singleObject.getString("district");
                                    locationNames.add(locName);
                                    locationAddresses.add(locAdd);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            queue.add(jsonObjectRequest);
            requestFinished(queue);
        }

    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
