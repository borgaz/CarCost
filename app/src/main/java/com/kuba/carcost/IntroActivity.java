package com.kuba.carcost;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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
        addSlide(IntroSlideFragment.newInstance(R.layout.intro_slide_2));

        showSkipButton(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
