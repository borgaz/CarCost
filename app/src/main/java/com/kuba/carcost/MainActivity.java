package com.kuba.carcost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kuba.carcost.fragment.AboutMeFragment;
import com.kuba.carcost.fragment.AddCostFragment;
import com.kuba.carcost.fragment.AddFuelFragment;
import com.kuba.carcost.fragment.ChangeVehicleFragment;
import com.kuba.carcost.fragment.HistoryFragment;
import com.kuba.carcost.fragment.HomeFragment;
import com.kuba.carcost.fragment.ImExFragment;
import com.kuba.carcost.fragment.PolicyFragment;
import com.kuba.carcost.fragment.SettingsFragment;
import com.kuba.carcost.fragment.StatsFragment;
import com.kuba.carcost.interfaces.ChangeFragment;


public class MainActivity extends AppCompatActivity implements ChangeFragment  {

    private DatabaseHelper myDb;
    private NavigationView navigationView;
    private DrawerLayout drawer;
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
    private static final String _ADDFUEL = "ADDFUEL";
    private static final String _ADDCOST = "ADDCOST";
    public static String _CURRENT = _HOME;
    private String name;

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

        myDb = new DatabaseHelper(this);

        startFirstIntro();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentTitles = getResources().getStringArray(R.array.fragment_titles);
        mHandler = new Handler();

        enableFloatingButton();

        // Pasek boczny
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //////////////////////////////////////////////////////////////

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // zmiana fragmentów
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // To change fragments
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
                openFragment();
                return true;
            }
        });
        openFragment();
    }

    // Do dodania nazwa aktywnego pojazdu
    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        setUserName();
    }

    // run first intro
    private void startFirstIntro() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor res = myDb.getUser();
                if(res.getCount() == 0){
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);
                }
//                //  Initialize SharedPreferences
//                SharedPreferences getPrefs = PreferenceManager
//                        .getDefaultSharedPreferences(getBaseContext());
//
//                //  Create a new boolean and preference and set it to true
//                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
//
//                //  If the activity has never started before...
//                if (isFirstStart) {
//
//                    //  Launch app intro
//                    Intent i = new Intent(MainActivity.this, IntroActivityApp.class);
//                    startActivity(i);
//
//                    //  Make a new preferences editor
//                    SharedPreferences.Editor e = getPrefs.edit();
//
//                    //  Edit preference to make it false because we don't want this to run again
//                    e.putBoolean("firstStart", false);
//
//                    //  Apply changes
//
//                    e.apply();
//                }
            }
        });

        // Start the thread
        t.start();
    }

    private void setUserName() {
        try {
            Cursor res = myDb.getUser();
            while (res.moveToNext()) {
                name = res.getString(1);
                TextView textView = (TextView) findViewById(R.id.userNameTextView);
                textView.setText(name);
            }
        } catch (Exception e) {
            //Toast.makeText(this, e.toString() + name, Toast.LENGTH_LONG).show();
        }
    }

    // Floating action button
    private void enableFloatingButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Kliknięcie plusa", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                navCurrentItem = 8;
                _CURRENT = _ADDFUEL;
                openFragment();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Snackbar.make(view, "Długie kliknięcie plusa", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                navCurrentItem = 9;
                _CURRENT = _ADDCOST;
                openFragment();
                return true;
            }
        });
    }

    // Zachowanie przycisku wstecz
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
                openFragment();
                return;
            }
        }
        super.onBackPressed();
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
            openFragment();
            return true;
        } else if(id == R.id.action_tutorial) {
            // TTTTUUUUUTTTTOOORRRIIAAALLLLLLL------------------------------------------
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
            ChangeVehicleFragment changeVehicleFragment = new ChangeVehicleFragment();
            return changeVehicleFragment;
        } else if (navCurrentItem == 6) {
            AboutMeFragment aboutMeFragment = new AboutMeFragment();
            return aboutMeFragment;
        } else if (navCurrentItem == 7) {
            PolicyFragment policyFragment = new PolicyFragment();
            return policyFragment;
        } else if (navCurrentItem == 8) {
            AddFuelFragment addFuelFragment = new AddFuelFragment();
            return addFuelFragment;
        } else if (navCurrentItem == 9) {
            AddCostFragment addCostFragment = new AddCostFragment();
            return addCostFragment;
        } else {
            return new HomeFragment();
        }
    }

    public void openFragment() {

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

    @Override
    public void openHomeFragment() {
        navCurrentItem = 0;
        _CURRENT = _HOME;

        openFragment();
    }
}
