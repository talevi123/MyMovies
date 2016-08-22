/**package com.tal.mymovies.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tal.mymovies.R;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout myDrawer;
    private Toolbar toolbar;
    private NavigationView naView;
    private ActionBarDrawerToggle Dtoggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        naView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(naView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                myDrawer.openDrawer(GravityCompat.START);
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem){

        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = LoginActivity.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = MovieActivity.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = MoviesListActivity.class;
                break;
            default:
                fragmentClass = LoginActivity.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //replace between fragments
        FragmentManager fragmentManager  = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();

        //after replacing
        menuItem.setChecked(true);

        setTitle(menuItem.getTitle());

        myDrawer.closeDrawers();
    }

}
*/