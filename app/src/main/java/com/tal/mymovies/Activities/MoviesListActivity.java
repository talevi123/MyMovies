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
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.tal.mymovies.Adapters.MoviesListAdapter;
import com.tal.mymovies.Adapters.MoviesListCursorAdapter;
import com.tal.mymovies.Adapters.PagerAdapter;
import com.tal.mymovies.DB.DBManager;
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

import static com.tal.mymovies.R.id.searchBox;

public class MoviesListActivity extends BaseActivity implements MyResultReceiver.Receiver {

    private static final String TAG = "MoviesListActivity";

    public static final String EVENT_NETWORK_DATA_READY = "com.tal.mymovies.NETWORK_DATA_READY";

    private BaseAdapter adapter;
    private MyResultReceiver resultReceiver;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor;
    public static final String KEY_MOVIES_LIST = "moviesList";
    private BroadcastReceiver listDataBradcastReceiver;
    private Toolbar toolbar;
    private DrawerLayout myDrawer;
    private NavigationView naView;
    private ActionBarDrawerToggle drawerToggle;
    private ListView listview;
    private ViewPager viewPager;
    private TabLayout tabs;
    private String moviesListAdapterType;
    private Fragment currentFragment;
    private EditText searchBox;


    //////////////////////////////////////////oncreate//////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initListDataBroadcastReceiver();
        initNavigationMenu();
        initSearchButton();
        initMoviesList();
        floatBtn();
        setViewPager();

    }


    private void setViewPager() {
        tabs = (TabLayout) findViewById(R.id.tab_host);
        tabs.addTab(tabs.newTab().setText("Main"));
        tabs.addTab(tabs.newTab().setText("Favorites"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentFragment = ((PagerAdapter) viewPager.getAdapter()).getFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initListDataBroadcastReceiver() {
        listDataBradcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handlerServerResponse(intent.getExtras());
            }
        };
    }

    private void floatBtn() {
        FloatingActionButton floatingActionBtn = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoviesListActivity.this, EditMovieActivity.class);
                startActivity(intent);
            }
        });
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
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
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
        moviesListAdapterType = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_adapter_key), getString(R.string.array_adapter));
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
            editor.putString(KEY_MOVIES_LIST, jsonarry).apply();

            List<Movie> movieList = parseMoviesListJson(jsonarry);

            updateListViewAdapter(movieList);
        }

    }

    private void updateListViewAdapter(List<Movie> movieList) {
        if (adapter instanceof MoviesListAdapter) {
            MoviesListAdapter moviesListAdapter = (MoviesListAdapter) adapter;
            moviesListAdapter.clear();
            moviesListAdapter.addAll(movieList);
            adapter.notifyDataSetChanged();
        } else {
            MoviesListCursorAdapter moviesListCursorAdapter = (MoviesListCursorAdapter) adapter;
            moviesListCursorAdapter.swapCursor(DBManager.getInstance(this).getAllMoviesAsCursor());
        }

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
        searchBox = (EditText) findViewById(R.id.searchBox);
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

        listview = (ListView) findViewById(R.id.listview);
        List<Movie> movies = new ArrayList<>();

        String savedMoviesList = sharedPreferences.getString(KEY_MOVIES_LIST, null);
        if (savedMoviesList != null) {
            movies = parseMoviesListJson(savedMoviesList);
        }

        adapter = new MoviesListAdapter(this, R.layout.activity_line_list, movies);
        //       Cursor allMoviesAsCursor = DBManager.getInstance(this).getAllMoviesAsCursor();
        //       adapter = new MoviesListCursorAdapter(this, allMoviesAsCursor);

        if (listview != null) {
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
                    Movie movie = Movie.createMovie(listview.getItemAtPosition(position));
                    new SearchMoviesTask2().execute(movie.getimdbId());
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
            intent.putExtra("VIDEO_ID", m.getVideoId());
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