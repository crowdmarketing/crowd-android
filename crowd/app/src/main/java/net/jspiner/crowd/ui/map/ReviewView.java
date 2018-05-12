package net.jspiner.crowd.ui.map;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;

import net.jspiner.crowd.R;
import net.jspiner.crowd.databinding.CardReviewBinding;
import net.jspiner.crowd.model.User;

public class ReviewView extends FrameLayout {

    private int[] IMGS = {
            R.drawable.food_1,
            R.drawable.food_2,
            R.drawable.food_3,
            R.drawable.food_4,
            R.drawable.food_5,
            R.drawable.food_6
    };
    private CardReviewBinding binding;

    public ReviewView(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.card_review, this, true);
    }

    public void setData(User user) {
        binding.name.setText(user.user_name + "님의 추천");
        Glide.with(getContext())
                .load(user.profile_img_url)
                .into(binding.profile);
        Glide.with(getContext())
                .load(IMGS[(int) (Math.random() * IMGS.length)])
                .into(binding.food);
    }


}
