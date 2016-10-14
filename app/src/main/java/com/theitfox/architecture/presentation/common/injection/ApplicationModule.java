package com.theitfox.architecture.presentation.common.injection;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by btquanto on 11/10/2016.
 */
@Module
public class ApplicationModule {
    private Context context;

    /**
     * Instantiates a new Application module.
     *
     * @param context the context
     */
    public ApplicationModule(Context context) {
        this.context = context;
    }

    /**
     * Provide context context.
     *
     * @return the context
     */
    @Provides
    public Context provideContext() {
        return this.context;
    }

    /**
     * Provide execution thread scheduler.
     *
     * @return the scheduler
     */
    @Provides @Named("executionThread")
    public Scheduler provideExecutionThread() {
        return Schedulers.newThread();
    }

    /**
     * Provide post execution thread scheduler.
     *
     * @return the scheduler
     */
    @Provides @Named("postExecutionThread")
    public Scheduler providePostExecutionThread() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * Provide composite subscription composite subscription.
     *
     * @return the composite subscription
     */
    @Provides
    public CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }
}
