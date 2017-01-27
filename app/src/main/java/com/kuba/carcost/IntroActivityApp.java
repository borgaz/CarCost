package com.kuba.carcost;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.kuba.carcost.fragment.IntroSlideFragment;

/**
 * Created by Kuba_HP on 21.01.2017.
 */

public class IntroActivityApp extends AppIntro {

    private DatabaseHelper myDb;
    private int distanceUnit;
    private int volumeUnit;
    private EditText userEditText;
    private RadioGroup radioDistance, radioVolume;
    private Fragment addUserFragment;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDb = new DatabaseHelper(this);
        addUserFragment = IntroSlideFragment.newInstance(R.layout.intro_slide_add_user);
        userEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (name.length() < 3) {
                    userEditText.setError("Wymagane są minimum 3 znaki.");
                }
                return false;
            }
        });

        addSlide(IntroSlideFragment.newInstance(R.layout.intro_slide_1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            addSlide(IntroSlideFragment.newInstance(R.layout.intro_slide_permissions));
        }
        addSlide(addUserFragment);
        addSlide(IntroSlideFragment.newInstance(R.layout.intro_slide_1));

        showSkipButton(false);
        setBackButtonVisibilityWithDone(true);
        setWizardMode(true);

        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        userEditText = (EditText) findViewById(R.id.userEditText);
        radioDistance = (RadioGroup) findViewById(R.id.radioDistance);
        radioVolume = (RadioGroup) findViewById(R.id.radioVolume);
        String name = userEditText.getText().toString();

        if (name.length() < 3) {
            userEditText.setError("Wymagane są minimum 3 znaki.");
        } else {
                if (radioDistance.getCheckedRadioButtonId() == R.id.radioKm)
                    distanceUnit = 0;
                else
                    distanceUnit = 1;

                if (radioVolume.getCheckedRadioButtonId() == R.id.radioL)
                    volumeUnit = 0;
                else
                    volumeUnit = 1;

                myDb.insertUserData(userEditText.getText().toString(), 0, distanceUnit, volumeUnit, 1);
                try {
                    ((TextView) findViewById(R.id.userNameTextView)).setText(myDb.getUser().getString(1));
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }

        }
        finish();
    }
}
