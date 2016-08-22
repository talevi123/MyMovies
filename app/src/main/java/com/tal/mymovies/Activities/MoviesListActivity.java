package com.tal.mymovies.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.tal.mymovies.Adapters.MoviesListAdapter;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.Network.ApiManager;
import com.tal.mymovies.R;
import com.tal.mymovies.Services.ApiService;
import com.tal.mymovies.Services.ApiThread;
import com.tal.mymovies.Services.MyResultReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesListActivity extends AppCompatActivity implements MyResultReceiver.Receiver {

    private static final String TAG = "MoviesListActivity";
    private MoviesListAdapter adapter;
    private MyResultReceiver resultReceiver;
    private EditText searchBox;
    ProgressDialog progressDialog;


    //////////////////////////////////////////oncreate//////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        searchBox = (EditText) findViewById(R.id.searchBox);

        serviceButton();
        handlerServicebutton();
        handlerPostbutton();
        asyncTaskButton();
        asyncTaskButton2();
    }
    //////////////////////////////////////////End_oncreate//////////////////////////////////////

    ///////////////////////////////service//////////////////////////////////////////

    private void serviceButton() {
        resultReceiver = new MyResultReceiver(new Handler(), this);
        Button serviceBtn = (Button) findViewById(R.id.service);

        if (serviceBtn != null) {
            serviceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
                    if (searchBox != null && searchBox.getText() != null && searchBox.getText().length() > 0) {
                        Bundle apiServiceBundle = new Bundle();
                        apiServiceBundle.putParcelable(ApiService.KEY_RECEIVER, resultReceiver);
                        apiServiceBundle.putString(ApiService.KEY_API_METHOD, ApiService.REQUEST_SEARCH_MOVIE);
                        apiServiceBundle.putString(ApiService.KEY_SEARCH, searchBox.getText().toString());
                        Intent serviceIntent = new Intent(MoviesListActivity.this, ApiService.class);
                        serviceIntent.putExtras(apiServiceBundle);
                        startService(serviceIntent);
                        closeKeyboard();
                    }
                }
            });
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        String api_method = resultData.getString(ApiService.KEY_API_METHOD);
        if (api_method.equals(ApiService.REQUEST_SEARCH_MOVIE)) {
            String jsonarry = resultData.getString(ApiService.KEY_MOVIES);
            List<Movie> movies = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonarry);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    if (jsonObject != null) {
                        Movie movie = new Movie(jsonObject);
                        movies.add(movie);
                    }
                }
                adapter.clear();
                adapter.addAll(movies);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {

            }
            progressDialog.dismiss();
        }

    }
    ///////////////////////////////service//////////////////////////////////////////

    //////////////////////////////handler///////////////////////////////////////////


    private void handlerPostbutton() {
        final Handler handler = new Handler();
        Button handlerPostBtn = (Button) findViewById(R.id.handlerPost);
        handlerPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Handler handler = new Handler();
                progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MoviesListActivity.this, "This is a delay message", Toast.LENGTH_SHORT).show();
                    }
                }, 5 * 1000);
                progressDialog.dismiss();
            }
        });
    }
    ////////////

    private void handlerServicebutton() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundleData = msg.getData();
                handlerServerResponse(bundleData);
            }
        };

        Button handlerBtn = (Button) findViewById(R.id.handler);

        if (handlerBtn != null) {
            handlerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
                    Bundle bundle = new Bundle();
                    bundle.putString(ApiThread.KEY_API_METHOD, ApiThread.REQUEST_SEARCH_MOVIE);
                    bundle.putString(ApiThread.KEY_SEARCH, searchBox.getText().toString());

                    ApiThread apiThread = new ApiThread(handler, bundle);
                    apiThread.start();
                }


            });
        }

    }

    public void handlerServerResponse(Bundle resultData) {
        String api_method = resultData.getString(ApiThread.KEY_API_METHOD);
        if (api_method.equals(ApiThread.REQUEST_SEARCH_MOVIE)) {
            String jsonarry = resultData.getString(ApiThread.KEY_MOVIES);
            List<Movie> movies = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonarry);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    if (jsonObject != null) {
                        Movie movie = new Movie(jsonObject);
                        movies.add(movie);
                    }
                }
                adapter.clear();
                adapter.addAll(movies);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {

            }
            progressDialog.dismiss();
        }

    }
    //////////////////////////////handler///////////////////////////////////////////

    //////////////////////////////////////////closeKeyboard//////////////////////////////////////
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    //////////////////////////////////////////closeKeyboard//////////////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();
    }

    //////////////////////////////////////////AsyncTask//////////////////////////////////////

    private void asyncTaskButton() {
        ImageButton searchBtn = (ImageButton) findViewById(R.id.searchButton);
        if (searchBtn != null) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
                    if (searchBox != null && searchBox.getText() != null && searchBox.getText().length() > 0) {
                        new SearchMoviesTask().execute(searchBox.getText().toString());
                        closeKeyboard();
                    }
                }
            });
        }
    }

    public class SearchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            return ApiManager.searchMovie(params[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                adapter.clear();
                adapter.addAll(movies);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }
    }

    /////////////////////

    private void asyncTaskButton2() {
        final ListView listview = (ListView) findViewById(R.id.listview);
        List<Movie> movies = new ArrayList<>();
        adapter = new MoviesListAdapter(this, R.layout.activity_line_list, movies);

        if (listview != null) {
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
                    Movie n = (Movie) (listview.getItemAtPosition(position));
                    new SearchMoviesTask2().execute(n.getimdbId());
                }
            });
        }
    }

    public class SearchMoviesTask2 extends AsyncTask<String, Void, Movie> {

        @Override
        protected Movie doInBackground(String... params) {
            return ApiManager.findMovie(params[0]);
        }

        @Override
        protected void onPostExecute(Movie m) {
            Intent intent = new Intent(MoviesListActivity.this, MovieActivity.class);
            intent.putExtra("title", m.getTitle());
            intent.putExtra("description", m.getDescription());
            intent.putExtra("imageUrl", m.getImageUrl());
            startActivity(intent);
            progressDialog.dismiss();
        }
    }

    //////////////////////////////////////////AsyncTask//////////////////////////////////////
}