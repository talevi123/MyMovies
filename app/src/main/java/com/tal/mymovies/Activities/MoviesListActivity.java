package com.tal.mymovies.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.tal.mymovies.Adapters.MoviesListAdapter;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.Network.ApiManager;
import com.tal.mymovies.R;
import com.tal.mymovies.Services.ApiBroadcastThread;
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

    public static final String EVENT_NETWORK_DATA_READY = "com.tal.mymovies.NETWORK_DATA_READY";

    private MoviesListAdapter adapter;
    private MyResultReceiver resultReceiver;
    private EditText searchBox;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String KEY_MOVIES_LIST = "nameKey";
    private BroadcastReceiver listDataBradcastReceiver;
    private Toolbar toolbar;
    private DrawerLayout myDrawer;
    private NavigationView naView;
    private ActionBarDrawerToggle drawerToggle;


    //////////////////////////////////////////oncreate//////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        searchBox = (EditText) findViewById(R.id.searchBox);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        initListDataBroadcastReceiver();
        initNavigationMenu();
        initSearchButton();
        initMoviesList();

    }

    private void initListDataBroadcastReceiver() {
        listDataBradcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handlerServerResponse(intent.getExtras());
            }
        };
    }

    //////////////////////////////////////////End_oncreate//////////////////////////////////////

    private void initNavigationMenu() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();


        myDrawer.addDrawerListener(drawerToggle);
        naView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(naView);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, myDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.handlerMenu:
                handlerServicebutton();
                break;
            case R.id.serviceMenu:
                serviceButton();
                break;
            case R.id.handlerPostMenu:
                handlerPostbutton();
                break;
            case R.id.broadcastrecevierMenu:
                bradcastButton();
                break;
            default:
        }


        setTitle(menuItem.getTitle());
        myDrawer.closeDrawers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                myDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /////////////////////BradcastReceiver////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        // Register ListDataBroadcastReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(listDataBradcastReceiver,
                new IntentFilter(EVENT_NETWORK_DATA_READY));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listDataBradcastReceiver);
        super.onPause();
    }

    private void bradcastButton() {

        progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
        Bundle bundle = new Bundle();
        bundle.putString(ApiThread.KEY_API_METHOD, ApiThread.REQUEST_SEARCH_MOVIE);
        bundle.putString(ApiThread.KEY_SEARCH, searchBox.getText().toString());

        ApiBroadcastThread apiThread = new ApiBroadcastThread(MoviesListActivity.this, bundle);
        apiThread.start();
    }

    /////////////////////BradcastReceiver////////////////////////////////////////////////

    ///////////////////////////////service//////////////////////////////////////////

    private void serviceButton() {
        resultReceiver = new MyResultReceiver(new Handler(), this);
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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        handlerServerResponse(resultData);
    }
    ///////////////////////////////service//////////////////////////////////////////

    //////////////////////////////handler///////////////////////////////////////////


    private void handlerPostbutton() {
        final Handler handler = new Handler();
        progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MoviesListActivity.this, "This is a delay message", Toast.LENGTH_SHORT).show();
            }
        }, 5 * 1000);
        progressDialog.dismiss();
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

        progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
        Bundle bundle = new Bundle();
        bundle.putString(ApiThread.KEY_API_METHOD, ApiThread.REQUEST_SEARCH_MOVIE);
        bundle.putString(ApiThread.KEY_SEARCH, searchBox.getText().toString());

        ApiThread apiThread = new ApiThread(handler, bundle);
        apiThread.start();

    }


    public void handlerServerResponse(Bundle resultData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String api_method = resultData.getString(ApiThread.KEY_API_METHOD);
        if (api_method.equals(ApiThread.REQUEST_SEARCH_MOVIE)) {

            String jsonarry = resultData.getString(ApiThread.KEY_MOVIES);
            editor.putString(KEY_MOVIES_LIST, jsonarry);
            editor.apply();

            List<Movie> movieList = parseMoviesListJson(jsonarry);

            updateListViewAdapter(movieList);
        }

    }

    private void updateListViewAdapter(List<Movie> movieList) {
        adapter.clear();
        adapter.addAll(movieList);
        adapter.notifyDataSetChanged();

        progressDialog.dismiss();
    }

    private List<Movie> parseMoviesListJson(String jsonarry) {
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

        } catch (JSONException e) {

        }

        return movies;
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

    private void initSearchButton() {
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
                updateListViewAdapter(movies);
            }
        }
    }

    /////////////////////

    private void initMoviesList() {
        final ListView listview = (ListView) findViewById(R.id.listview);
        List<Movie> movies = new ArrayList<>();

        String savedMoviesList = sharedPreferences.getString(KEY_MOVIES_LIST, null);
        if (savedMoviesList != null) {
            movies = parseMoviesListJson(savedMoviesList);
        }

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
           // intent.putExtra("VIDEO_ID", m.getVideoId());
            intent.putExtra("title", m.getTitle());
            intent.putExtra("description", m.getDescription());
            intent.putExtra("director", m.getDirector());
            intent.putExtra("genre", m.getGenre());
            intent.putExtra("year", m.getYear());
            intent.putExtra("min", m.getDuration());
            intent.putExtra("rating", m.getRating());
            intent.putExtra("imageUrl", m.getImageUrl());
            startActivity(intent);
            progressDialog.dismiss();
        }
    }

    //////////////////////////////////////////AsyncTask//////////////////////////////////////
}