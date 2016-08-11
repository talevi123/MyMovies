package com.tal.mymovies.Activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        final ListView listview = (ListView) findViewById(R.id.listview);
        searchBox = (EditText) findViewById(R.id.searchBox);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.searchButton);

        if (searchBtn != null) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchBox != null && searchBox.getText() != null && searchBox.getText().length() > 0) {

//                        new SearchMoviesTask().execute(searchBox.getText().toString());


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

        //SharedPreferences sp = getSharedPreferences("key", 0);
        //String tValue = sp.getString("textvalue","");
        //String tOperative = sp.getString("textvalue2","");

        Intent in = getIntent();

        String fullName = in.getStringExtra("full_name");
        String email = in.getStringExtra("email");
        String username = in.getStringExtra("username");

        List<Movie> movies = new ArrayList<>();

        adapter = new MoviesListAdapter(this,
                R.layout.activity_line_list, movies);
        if (listview != null) {
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    Movie movie = adapter.getItem(position);
                    new SearchMoviesTask2().execute(movie.getImdbId());
                }

            });
        }

        resultReceiver = new MyResultReceiver(new Handler(), this);

        initHandlerPostButton();
        initHandlerServiceButton();
    }

    private void initHandlerServiceButton() {
        final Handler handler = new Handler() {
            @Override
            // On the main thread
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                handlerServerResponse(data);
            }
        };

        Button searchByHandlerButton = (Button) findViewById(R.id.handler_service_button);
        if (searchByHandlerButton != null) {
            searchByHandlerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ApiService.KEY_RECEIVER, resultReceiver);
                    bundle.putString(ApiService.KEY_API_METHOD, ApiService.REQUEST_SEARCH_MOVIE);
                    bundle.putString(ApiService.KEY_SEARCH, searchBox.getText().toString());
                    Intent intent = new Intent(MoviesListActivity.this, ApiService.class);
                    intent.putExtras(bundle);

                    ApiThread apiThread = new ApiThread(handler, intent);
                    apiThread.start();
                }
            });
        }


    }

    private void initHandlerPostButton() {
        final Handler handler = new Handler();

        Button postButton = (Button) findViewById(R.id.handler_post_button);
        if (postButton != null) {
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoviesListActivity.this, "This is a delay message", Toast.LENGTH_SHORT).show();
                        }
                    }, 5 * 1000);
                }
            });
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        handlerServerResponse(resultData);
    }

    private void handlerServerResponse(Bundle resultData) {
        String apiMethod = resultData.getString(ApiService.KEY_API_METHOD);
        if (apiMethod.equals(ApiService.REQUEST_SEARCH_MOVIE)) {
            List<Movie> movieList = new ArrayList<>();
            try {
                String jsonArrayString = resultData.getString(ApiService.KEY_MOVIES);
                JSONArray searchJsonArray = new JSONArray(jsonArrayString);
                for (int i = 0; i < searchJsonArray.length(); i++) {
                    JSONObject movieJson = searchJsonArray.optJSONObject(i);
                    if (movieJson != null) {
                        Movie movie = new Movie(movieJson);
                        movieList.add(movie);
                    }
                }

                adapter.clear();
                adapter.addAll(movieList);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            }
        }
    }

    public class SearchMoviesTask2 extends AsyncTask<String, Void, Movie> {

        @Override
        protected Movie doInBackground(String... params) {
            return ApiManager.findMovie(params[0]);
        }

        @Override
        protected void onPostExecute(Movie m) {
            Intent intent = new Intent(MoviesListActivity.this, Movie.class);
            intent.putExtra("title", m.getTitle());
            intent.putExtra("description", m.getDescription());
            intent.putExtra("imageUrl", m.getImageUrl());
        }
    }

}


