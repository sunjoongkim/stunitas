package com.sunjoong.stunitas.model;

import com.sunjoong.stunitas.define.Define;
import com.sunjoong.stunitas.model.data.ImageInfo;
import com.sunjoong.stunitas.request.NetworkService;
import com.sunjoong.stunitas.util.ImageParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImagesLocalDataSource implements ImagesSource
{
    private NetworkService mNetworkService;

    private ArrayList<ImageInfo> mImageList;
    private String mCurrentKeyword = "";
    private int mCurrentPage = 1;

    public ImagesLocalDataSource()
    {
        initRetrofit();
    }

    @Override
    public void getImages(String keyword, final CompletedCallback callback)
    {
        initKeyword();
        mCurrentKeyword = keyword;

        if(mNetworkService != null)
        {
            mNetworkService.getImages(keyword, mCurrentPage, Define.DEFAULT_PAGE_SIZE).enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    if(response.body() != null)
                    {
                        try
                        {
                            ArrayList<ImageInfo> imageList = ImageParser.parse(response.body().string());
                            mImageList = (ArrayList<ImageInfo>) imageList.clone();

                            if(!ImageParser.isLastPage)
                                imageList.add(new ImageInfo(""));

                            if(callback != null)
                                callback.onCompleted(imageList, mCurrentPage);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t)
                {
                    if(callback != null)
                        callback.onError();
                }
            });
        }
    }

    @Override
    public void addImages(final CompletedCallback callback)
    {
        mCurrentPage++;

        if(mNetworkService != null)
        {
            mNetworkService.getImages(mCurrentKeyword, mCurrentPage, Define.DEFAULT_PAGE_SIZE).enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    if(response.body() != null)
                    {
                        try
                        {
                            mImageList.addAll(ImageParser.parse(response.body().string()));
                            ArrayList<ImageInfo> imageList = (ArrayList<ImageInfo>) mImageList.clone();

                            if(!ImageParser.isLastPage)
                                imageList.add(new ImageInfo(""));

                            if(callback != null)
                                callback.onCompleted(imageList, mCurrentPage);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t)
                {
                    if(callback != null)
                        callback.onError();
                }
            });
        }
    }

    private void initRetrofit()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Define.URL_BASE)
                .build();

        mNetworkService = retrofit.create(NetworkService.class);
    }

    private void initKeyword()
    {
        mCurrentPage = 1;
        mCurrentKeyword = "";

        if(mImageList != null && !mImageList.isEmpty())
            mImageList.clear();
    }

}
