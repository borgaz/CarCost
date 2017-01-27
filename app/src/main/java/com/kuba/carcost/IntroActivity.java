package com.kuba.carcost;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.PermissionObject;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener viewListener;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private Button nextButton, backButton;
    private EditText userEditText, vehicleEditText, tankVolume1EditText, TankVolume2EditText;
    private RadioGroup radioDistance, radioVolume, radioFuelType1, radioFuelType2;
    private RelativeLayout secondTankLayout;

    private DatabaseHelper myDb;
    private int[] layouts;
    private int distanceUnit;
    private int volumeUnit;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 123;
    private boolean permissionIsGranted = false;
    private int askForPermissionNumeber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT>=21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
        setContentView(R.layout.activity_intro);
        myDb = new DatabaseHelper(this);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout=(LinearLayout) findViewById(R.id.layoutDots);
        nextButton = (Button) findViewById(R.id.nextButton);
        backButton = (Button) findViewById(R.id.backButton);
        layouts = new int[]{R.layout.intro_slide_1, R.layout.intro_slide_permissions, R.layout.intro_slide_add_user, R.layout.intro_slide_add_vehicle};
        addBottomDots(0);

        setViewListener();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        // Zmiana ekranów poprzez przycisk dalej
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem();

                // Obsługa ekranu przyznawania uprawnień
                if(current == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(IntroActivity.this,
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        askForPermissionNumeber++;
                        ActivityCompat.requestPermissions(IntroActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_STORAGE);
//                            }
                    } else {
                        permissionIsGranted = true;
                    }
                    if(permissionIsGranted || askForPermissionNumeber > 1) {
                        current++;
                    }

                // Obsługa ekranu dodawania użytkownika
                } else if(current == 2) {
                    String name = userEditText.getText().toString();
                    if (name.length() < 3) {
                        userEditText.setError("Wymagane są minimum 3 znaki.");
                    } else {
                        addUserToDB();
                        current++;
                    }

                // Obsługa ekranu dodawania pojazdu
                } else if(current == 3) {
                    current++;

                } else {
                    current++;
                }
                if(current<layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    finish();
                }
            }
        });

        // Zmiana ekranów poprzez przycisk wstecz
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem();
                current--;
                if(current>-1) {
                    viewPager.setCurrentItem(current);
                } else {
                    finish();
                }
            }
        });
    }

    private void addUserToDB() {
        String name = userEditText.getText().toString();

        if (radioDistance.getCheckedRadioButtonId() == R.id.radioKm)
            distanceUnit = 0;
        else
            distanceUnit = 1;

        if (radioVolume.getCheckedRadioButtonId() == R.id.radioL)
            volumeUnit = 0;
        else
            volumeUnit = 1;

        myDb.insertUserData(name, 0, distanceUnit, volumeUnit, 1);
    }

    private void setViewListener() {
        viewListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

                addBottomDots(position);
                if(position == 1) {
                    backButton.setVisibility(View.VISIBLE);
                } else if(position == 2) {
                    userEditText = (EditText) findViewById(R.id.userEditText);
                    radioDistance = (RadioGroup) findViewById(R.id.radioDistance);
                    radioVolume = (RadioGroup) findViewById(R.id.radioVolume);
                } else if(position == 3) {
                    nextButton.setText(R.string.end);
                    vehicleEditText = (EditText) findViewById(R.id.vehicleEditText);
                    tankVolume1EditText = (EditText) findViewById(R.id.tankVolume1EditText);
                    TankVolume2EditText = (EditText) findViewById(R.id.tankVolume2EditText);
                    radioFuelType1 = (RadioGroup) findViewById(R.id.radioFuelType1);
                    radioFuelType2 = (RadioGroup) findViewById(R.id.radioFuelType2);
                    secondTankLayout = (RelativeLayout) findViewById(R.id.secondTankLayout);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionIsGranted = true;
                } else {
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "Bez uprawnień nie będzie można wykonać importu/eksportu bazy danych", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private void addBottomDots(int position) {
        dots = new TextView[layouts.length];
        int[] colorDots = getResources().getIntArray(R.array.color_dots);
        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            if(i == position) {
                dots[i].setTextColor(colorDots[0]);
            } else {
                dots[i].setTextColor(colorDots[1]);
            }
            dotsLayout.addView(dots[i]);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position], container, false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }
}
