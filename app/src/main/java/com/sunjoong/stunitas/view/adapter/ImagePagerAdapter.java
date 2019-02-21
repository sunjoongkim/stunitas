package com.sunjoong.stunitas.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.sunjoong.stunitas.view.fragment.ImageFragment;

import java.util.ArrayList;

public class ImagePagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener
{
    private ArrayList<ImageFragment> mImageList;

    public ImagePagerAdapter(FragmentManager fm, ArrayList<ImageFragment> list, OnPageListener listener)
    {
        super(fm);
        mImageList = list;
        mPageListener = listener;
    }


    @Override
    public Fragment getItem(int position)
    {
        return mImageList.get(position);
    }

    @Override
    public int getCount()
    {
        return mImageList.size();
    }


    private OnPageListener mPageListener;

    @Override
    public void onPageScrolled(int i, float v, int i1)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        if(position == mImageList.size() - 1)
            mPageListener.onArrived();
    }

    @Override
    public void onPageScrollStateChanged(int i)
    {

    }

    public interface OnPageListener
    {
        void onArrived();
    }
}
