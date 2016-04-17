package com.will.adaptor;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.will.bean.ExpressionItem;

import java.util.List;

/**
 * Created by will on 2016/2/22.
 */
public class ExpressionPageAdapet extends PagerAdapter{

    List<View> mlist;

    public ExpressionPageAdapet(List<View> list) {
        mlist=list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mlist.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(mlist.get(position));
        return mlist.get(position);
    }
}
