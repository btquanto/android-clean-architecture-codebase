package com.theitfox.architecture.presentation.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.theitfox.architecture.R;

/**
 * Created by btquanto on 30/09/2016.
 * <p>
 * Super class of Activities that only have a single fragment
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_single_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = onCreateFragment();
        fm.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    /**
     * Override this to specify which fragment should this activity contains
     *
     * @return the fragment that this activity should contains
     */
    protected abstract Fragment onCreateFragment();
}
