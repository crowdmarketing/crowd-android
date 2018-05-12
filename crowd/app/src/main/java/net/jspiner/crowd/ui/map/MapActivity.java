package net.jspiner.crowd.ui.map;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.jspiner.crowd.ui.base.BaseActivity;
import net.jspiner.crowd.ui.base.BasePresenterInterface;

public class MapActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected BasePresenterInterface createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
