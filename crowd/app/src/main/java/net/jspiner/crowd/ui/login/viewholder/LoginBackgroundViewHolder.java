package net.jspiner.crowd.ui.login.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import net.jspiner.crowd.R;
import net.jspiner.crowd.databinding.CardLoginBackgroundBinding;

public class LoginBackgroundViewHolder extends RecyclerView.ViewHolder {

    private final int[] imgs = {
            R.drawable.food_1,
            R.drawable.food_2,
            R.drawable.food_3,
            R.drawable.food_4,
            R.drawable.food_5,
            R.drawable.food_6,
    };

    protected CardLoginBackgroundBinding binding;

    public LoginBackgroundViewHolder(View itemView) {
        super(itemView);
    }

    public LoginBackgroundViewHolder(CardLoginBackgroundBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void setData(int position) {
        Glide.with(binding.getRoot().getContext())
                .load(imgs[position % 6])
                .centerCrop()
                .into(binding.image);
    }

}
