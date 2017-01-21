package com.kuba.carcost;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuba.carcost.fragment.AboutMeFragment;
import com.kuba.carcost.fragment.ChangeFragment;
import com.kuba.carcost.fragment.HistoryFragment;
import com.kuba.carcost.fragment.HomeFragment;
import com.kuba.carcost.fragment.ImExFragment;
import com.kuba.carcost.fragment.PolicyFragment;
import com.kuba.carcost.fragment.SettingsFragment;
import com.kuba.carcost.fragment.StatsFragment;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // fragments tags
    private static final String _HOME = "HOME";
    private static final String _STATS= "STATS";
    private static final String _HISTORY = "HISTORY";
    private static final String _IMEX= "IMEX";
    private static final String _SETTINGS = "SETTINGS";
    private static final String _CHANGE= "CHANGE";
    private static final String _ABOUTME = "ABOUTME";
    private static final String _POLICY = "POLICY";
    public static String _CURRENT = _HOME;

    // index to identify current nav menu item
    public static int navCurrentItem = 0;

    // fragment titles
    private String[] fragmentTitles;

    // to load home fragment when back key pressed
    private boolean shouldLoadHomeFragment = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////////////////////////
        myDb = new DatabaseHelper(this);


        ///////////////////////////////////

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentTitles = getResources().getStringArray(R.array.slide_titles);
        mHandler = new Handler();

        // Funkcje sterujące zachowaniem pływającego przycisku
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Kliknięcie plusa", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Długie kliknięcie plusa", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

        //
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Instrukcje warunkowe
                if (id == R.id.nav_home) {
                    navCurrentItem = 0;
                    _CURRENT = _HOME;
                } else if (id == R.id.nav_stats) {
                    navCurrentItem = 1;
                    _CURRENT = _STATS;
                } else if (id == R.id.nav_history) {
                    navCurrentItem = 2;
                    _CURRENT = _HISTORY;
                } else if (id == R.id.nav_im_ex) {
                    navCurrentItem = 3;
                    _CURRENT = _IMEX;
                } else if (id == R.id.nav_settings) {
                    navCurrentItem = 4;
                    _CURRENT = _SETTINGS;
                } else if (id == R.id.nav_change) {
                    navCurrentItem = 5;
                    _CURRENT = _CHANGE;
                } else if (id == R.id.nav_about_me) {
                    navCurrentItem = 6;
                    _CURRENT = _ABOUTME;
                } else if (id == R.id.nav_policy) {
                    navCurrentItem = 7;
                    _CURRENT = _POLICY;
                }
                return true;
            }
        });
        loadFragment();
    }

    // Zachowanie
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if(shouldLoadHomeFragment) {
            if(navCurrentItem != 0) {
                navCurrentItem = 0;
                _CURRENT = _HOME;
                loadFragment();
                return;
            }
        }

        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            navCurrentItem = 4;
            _CURRENT = _SETTINGS;
            return true;
        } else if(id == R.id.action_tutorial) {

        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void showHideFab() {
        if (navCurrentItem == 0)
            fab.show();
        else
            fab.hide();
    }

    // set title in Toolbar
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(fragmentTitles[navCurrentItem]);
    }

    // to do something when fragment is huge
    private void loadFragment() {

        setToolbarTitle();

        // check if user choose the same fragment
        if(getSupportFragmentManager().findFragmentByTag(_CURRENT) != null) {
            drawer.closeDrawers();
            showHideFab();
            return;
        }

        // for big data fragments
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getCurrentFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_main, fragment, _CURRENT);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // add to message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        showHideFab();
        // close drawer
        drawer.closeDrawers();
        // refresh toolbar
        invalidateOptionsMenu();
    }

    private Fragment getCurrentFragment() {
        if (navCurrentItem == 0) {
            HomeFragment homeFragment = new HomeFragment();
            return homeFragment;
        } else if (navCurrentItem == 1) {
            StatsFragment statsFragment = new StatsFragment();
            return statsFragment;
        } else if (navCurrentItem == 2) {
            HistoryFragment historyFragment = new HistoryFragment();
            return historyFragment;
        } else if (navCurrentItem == 3) {
            ImExFragment imExFragment = new ImExFragment();
            return imExFragment;
        } else if (navCurrentItem == 4) {
            SettingsFragment settingsFragment = new SettingsFragment();
            return settingsFragment;
        } else if (navCurrentItem == 5) {
            ChangeFragment changeFragment = new ChangeFragment();
            return changeFragment;
        } else if (navCurrentItem == 6) {
            AboutMeFragment aboutMeFragment = new AboutMeFragment();
            return aboutMeFragment;
        } else if (navCurrentItem == 7) {
            PolicyFragment policyFragment = new PolicyFragment();
            return policyFragment;
        } else {
            return new HomeFragment();
        }
    }

}
