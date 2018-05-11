package net.jspiner.crowd.ui.base;

import io.reactivex.subjects.BehaviorSubject;

public abstract class BasePresenter<T> implements BasePresenterInterface {

    protected T view;
    private BehaviorSubject disposeSubject = BehaviorSubject.create();

    public BasePresenter(T view){
        this.view = view;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {
        disposeSubject.onComplete();
    }
}
