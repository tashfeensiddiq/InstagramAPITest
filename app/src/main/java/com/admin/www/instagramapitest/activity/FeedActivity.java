package com.admin.www.instagramapitest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.www.instagramapitest.InstagramResponse;
import com.admin.www.instagramapitest.POJO.Data;
import com.admin.www.instagramapitest.R;
import com.admin.www.instagramapitest.adapter.SimpleListViewAdapter;
import com.admin.www.instagramapitest.rest.RestClient;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

    private EditText etSearch;

    private SimpleListViewAdapter lvAdapter;
    private ArrayList<Data> data = new ArrayList<>();

    private String access_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Get the access_token from the intent extra
        Intent i = this.getIntent();
        access_token = i.getStringExtra("access_token");

        ListView lvFeed = (ListView) findViewById(R.id.lv_feed);
        etSearch = (EditText) findViewById(R.id.et_search);

        // Set the listview adapter
        lvAdapter = new SimpleListViewAdapter(this, 0, data);
        lvFeed.setAdapter(lvAdapter);

        // Set the listener for the "Done" button of the soft keyboard
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // Don't search if the etSearch is emtpy when pressing the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(etSearch.getText().length() <= 0){
                        Toast.makeText(getApplicationContext(), "Enter a search tag", Toast.LENGTH_SHORT).show();

                    } else {
                        lvAdapter.clearListView();
                        fetchData(etSearch.getText().toString());
                        etSearch.setText("");
                        etSearch.clearFocus();
                    }

                    // Close the soft keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }
    public void fetchData(String tag) {
        retrofit2.Call<InstagramResponse> call = RestClient.getRetrofitService().getTagPhotos(tag, access_token);
        call.enqueue(new Callback<InstagramResponse>() {
            @Override
            public void onResponse(retrofit2.Call<InstagramResponse> call, Response<InstagramResponse> response) {

                if (response.body() != null) {
                    data.addAll(Arrays.asList(response.body().getData()));

                    lvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {
                //Handle failure
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}