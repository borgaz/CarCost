package com.kuba.carcost;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import com.github.paolorotolo.appintro.AppIntro;
import com.kuba.carcost.fragment.IntroSlideFragment;

/**
 * Created by Kuba_HP on 21.01.2017.
 */

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(IntroSlideFragment.newInstance(R.layout.intro_slide_1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            addSlide(IntroSlideFragment.newInstance(R.layout.intro_slide_permissions));
        }
        addSlide(IntroSlideFragment.newInstance(R.layout.intro_slide_2));

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

        String name = ((EditText) findViewById(R.id.userEditText)).getText().toString();


        AlertDialog mDialog = new AlertDialog.Builder(this)
                .setTitle("Gmelon")
                .setMessage(name)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("MyTag" , "Click YES");
                            }
                        })

                .setNegativeButton("NO",
                        new android.content.DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("MyTag", "Click NO");
                            }
                        }).create();
        mDialog.show();
        finish();
    }
}
