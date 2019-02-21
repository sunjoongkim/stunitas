package com.sunjoong.stunitas.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sunjoong.stunitas.R;


public class ImageFragment extends Fragment
{

    private Context mContext;
    private String mImageUrl;
    private ProgressBar mLoadingView;

    public ImageFragment(Context context, String url)
    {
        mContext = context;
        mImageUrl = url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.view_pager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.view_image);
        mLoadingView = (ProgressBar) view.findViewById(R.id.loading_view);

        Glide.with(getContext())
             .load(mImageUrl)
             .listener(mRequestListener)
             .into(imageView);

        return view;
    }

    private RequestListener mRequestListener = new RequestListener()
    {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource)
        {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource)
        {
            mLoadingView.setVisibility(View.INVISIBLE);
            return false;
        }
    };
}
