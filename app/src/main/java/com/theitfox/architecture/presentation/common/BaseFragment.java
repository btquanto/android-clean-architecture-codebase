package com.theitfox.architecture.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.theitfox.architecture.presentation.common.injection.abstracts.HasComponent;

/**
 * Created by btquanto on 05/10/2016.
 */
public abstract class BaseFragment extends Fragment implements HasComponent {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
