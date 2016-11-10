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
import android.widget.Toast;

import com.tal.mymovies.Adapters.PagerAdapter;
import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.Fragments.FavoritesFragment;
import com.tal.mymovies.Fragments.MovieListFragment;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.MyMoviesApplication;
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

import static com.google.android.gms.analytics.internal.zzy.c;

public class MoviesListActivity extends BaseActivity implements MyResultReceiver.Receiver, MovieListFragment.SearchMovieListener {

    private static final String TAG = "MoviesListActivity";

    public static final String EVENT_NETWORK_DATA_READY = "com.tal.mymovies.NETWORK_DATA_READY";

    private MyResultReceiver resultReceiver;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String KEY_MOVIES_LIST = "moviesList";
    private BroadcastReceiver listDataBradcastReceiver;
    private Toolbar toolbar;
    private DrawerLayout myDrawer;
    private NavigationView naView;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private TabLayout tabs;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initListDataBroadcastReceiver();
        initNavigationMenu();
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
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                boolean firstInitialize = currentFragment == null;
                currentFragment = pagerAdapter.getFragment(position);
                if (firstInitialize) {
                    String moviesList = PreferenceManager.getDefaultSharedPreferences(MoviesListActivity.this).getString(KEY_MOVIES_LIST, null);
                    if (moviesList != null) {
                        updateMoviesAdapter(moviesList);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Fragment selectedFragment = pagerAdapter.getFragment(position);
                if (selectedFragment instanceof FavoritesFragment) {
                    ((FavoritesFragment) selectedFragment).updateFavorites();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

        String searchInput = null;
        if (currentFragment instanceof MovieListFragment) {
            searchInput = ((MovieListFragment) currentFragment).getSearchBoxValue().toString();
        }

        switch (menuItem.getItemId()) {
            case R.id.handlerMenu:
                handlerServicebutton(searchInput);
                break;
            case R.id.serviceMenu:
                serviceButton(searchInput);
                break;
            case R.id.handlerPostMenu:
                handlerPostbutton();
                break;
            case R.id.broadcastrecevierMenu:
                bradcastButton(searchInput);
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.clean_fav:
                DBManager.getInstance(MyMoviesApplication.getInstance()).deleteAllFavs();
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

    private void bradcastButton(String searchBoxInput) {
        if (searchBoxInput != null) {
            progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");
            Bundle bundle = new Bundle();
            bundle.putString(ApiThread.KEY_API_METHOD, ApiThread.REQUEST_SEARCH_MOVIE);
            bundle.putString(ApiThread.KEY_SEARCH, searchBoxInput);

            ApiBroadcastThread apiThread = new ApiBroadcastThread(MoviesListActivity.this, bundle);
            apiThread.start();
        }
    }

    /////////////////////BradcastReceiver////////////////////////////////////////////////

    ///////////////////////////////service//////////////////////////////////////////

    private void serviceButton(String searchBoxInput) {
        if (searchBoxInput != null) {
            resultReceiver = new MyResultReceiver(new Handler(), this);
            progressDialog = ProgressDialog.show(MoviesListActivity.this, "", "Loading...");

            Bundle apiServiceBundle = new Bundle();
            apiServiceBundle.putParcelable(ApiService.KEY_RECEIVER, resultReceiver);
            apiServiceBundle.putString(ApiService.KEY_API_METHOD, ApiService.REQUEST_SEARCH_MOVIE);
            apiServiceBundle.putString(ApiService.KEY_SEARCH, searchBoxInput);
            Intent serviceIntent = new Intent(MoviesListActivity.this, ApiService.class);
            serviceIntent.putExtras(apiServiceBundle);
            startService(serviceIntent);
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

    private void handlerServicebutton(String searchBoxInput) {
        if (searchBoxInput != null) {
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
            bundle.putString(ApiThread.KEY_SEARCH, searchBoxInput);

            ApiThread apiThread = new ApiThread(handler, bundle);
            apiThread.start();
        }
    }


    public void handlerServerResponse(Bundle resultData) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        String api_method = resultData.getString(ApiThread.KEY_API_METHOD);
        if (api_method.equals(ApiThread.REQUEST_SEARCH_MOVIE)) {
            String jsonarry = resultData.getString(ApiThread.KEY_MOVIES);
            PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .edit().putString(KEY_MOVIES_LIST, jsonarry).apply();
            updateMoviesAdapter(jsonarry);
        }
    }

    private void updateMoviesAdapter(String jsonarry) {
        List<Movie> movieList = parseMoviesListJson(jsonarry);
        if (currentFragment instanceof MovieListFragment) {
            ((MovieListFragment) currentFragment).updateListViewAdapter(movieList);
        }
    }

    public List<Movie> parseMoviesListJson(String jsonarry) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonarry);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Movie movie = new Movie(jsonObject);
                    boolean isFavorite = DBManager.getInstance(MyMoviesApplication.getInstance()).checkIfExsists(movie.getimdbId());
                    movie.setFavorie(isFavorite);
                    movies.add(movie);
                }
            }

        } catch (JSONException e) {

        }
        return movies;
    }
    //////////////////////////////handler///////////////////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();
    }

    //////////////////////////////////////////AsyncTask//////////////////////////////////////

    @Override
    public void searchMovie(String name) {
        new SearchMoviesTask().execute(name);
    }

    @Override
    public void initMovie(String imdbId) {
        new SearchMoviesTask2().execute(imdbId);
    }

    public class SearchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            return ApiManager.searchMovie(params[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                if (currentFragment instanceof MovieListFragment) {
                    ((MovieListFragment) currentFragment).updateListViewAdapter(movies);
                }
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
        }
    }

    /////////////////////

    private void initMoviesList() {
        String savedMoviesList = sharedPreferences.getString(KEY_MOVIES_LIST, null);
    }

}