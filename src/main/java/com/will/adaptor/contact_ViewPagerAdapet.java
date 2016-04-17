package com.will.adaptor;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by will on 2016/4/10.
 */
public class contact_ViewPagerAdapet extends PagerAdapter {

    List<View> viewList;
    List<String> titleList;

    public contact_ViewPagerAdapet(List<View> viewlist,List<String> titleList) {
        super();
        this.viewList=viewlist;
        this.titleList=titleList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(viewList.get(position),position);
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView(viewList.get(position));
    }
}
