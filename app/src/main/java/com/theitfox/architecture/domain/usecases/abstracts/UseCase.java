package com.theitfox.architecture.domain.usecases.abstracts;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;

/**
 * Created by btquanto on 22/08/2016.
 * <p>
 * This class is the super class of use cases
 * Use cases represent business logic. Each use case represents one logic.
 *
 * @param <T> the type of event the use case promises to emit upon execution
 */
public abstract class UseCase<T> {

    /**
     * The business logic will run on this thread
     */
    protected final Scheduler executionThread;

    /**
     * The events will be emitted to this thread
     */
    protected final Scheduler postExecutionThread;

    /**
     * Instantiates a new Use case.
     *
     * @param executionThread     the execution thread
     * @param postExecutionThread the post execution thread
     */
    public UseCase(Scheduler executionThread, Scheduler postExecutionThread) {
        this.executionThread = executionThread;
        this.postExecutionThread = postExecutionThread;
    }

    /**
     * Build the Observable that promises the output of the use case
     *
     * @return an Observable that promises the output of this use case
     */
    protected abstract Observable<T> buildUseCaseObservable();

    /**
     * Build the final Observable to be executed
     * This by default just calls{@link #buildUseCaseObservable}
     * However, it can be overridden for extended implementation
     * For example, merging {@link #buildUseCaseObservable()} with another Observable
     *
     * @return an Observable that promises the output of this use case
     */
    protected Observable<T> buildExecutionObservable() {
        return buildUseCaseObservable();
    }

    /**
     * Subscribe the execution observable to {@link #executionThread}
     * And modify the Observable to have its emission and notifications to perform
     * on {@link #postExecutionThread}
     *
     * @return an Observable that promises the output of this use case
     */
    private Observable<T> buildObservable() {
        return buildUseCaseObservable()
                .subscribeOn(executionThread)
                .observeOn(postExecutionThread);
    }

    /**
     * Execute the use case.
     *
     * @param useCaseSubscriber A subscriber that will process the emitted output
     * @return the subscription that manages the life cycle of the execution observable
     */
    public Subscription execute(Subscriber<T> useCaseSubscriber) {
        return buildObservable()
                .subscribe(useCaseSubscriber);
    }

    /**
     * Execute the use case.
     *
     * @param onNextAction the on next action
     * @return the subscription that manages the life cycle of the execution observable
     */
    public Subscription execute(Action1<T> onNextAction) {
        return buildObservable()
                .subscribe(onNextAction, Actions.empty(), Actions.empty());
    }

    /**
     * Execute the use case.
     *
     * @param onNextAction  handle
     * @param onErrorAction the on error action
     * @return the subscription that manages the life cycle of the execution observable
     */
    public Subscription execute(Action1<T> onNextAction, Action1<Throwable> onErrorAction) {
        return buildObservable()
                .subscribe(onNextAction, onErrorAction, Actions.empty());
    }

    /**
     * Execute the use case.
     *
     * @param onNextAction     the on next action
     * @param onErrorAction    the on error action
     * @param onCompleteAction the on complete action
     * @return the subscription that manages the life cycle of the execution observable
     */
    public Subscription execute(Action1<T> onNextAction, Action1<Throwable> onErrorAction, Action0 onCompleteAction) {
        return buildObservable()
                .subscribe(onNextAction, onErrorAction, onCompleteAction);
    }
}


