package net.jspiner.crowd.ui.map;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import net.jspiner.crowd.model.Review;
import net.jspiner.crowd.model.User;

import java.util.ArrayList;

public class CardAdapter extends PagerAdapter {

    private ArrayList<User> dataList = new ArrayList<>();

    public void resetAll(ArrayList<User> userList) {
        dataList.clear();
        dataList.addAll(userList);
        notifyDataSetChanged();
        Log.i("TAG", "resetall");
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i("TAG", "init item : " + position);
        ReviewView view = new ReviewView(container.getContext());
        view.setData(dataList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        Log.i("TAG", "destroyu : " + position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
