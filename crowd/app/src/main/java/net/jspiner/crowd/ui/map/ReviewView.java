package net.jspiner.crowd.ui.map;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import net.jspiner.crowd.R;

public class ReviewView extends FrameLayout {

    private ViewDataBinding binding;

    public ReviewView(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.card_review, this, true);
    }


}
