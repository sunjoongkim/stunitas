package com.sunjoong.stunitas.presenter;

import com.sunjoong.stunitas.model.ImagesRepository;
import com.sunjoong.stunitas.model.ImagesSource;
import com.sunjoong.stunitas.model.data.ImageInfo;

import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter
{
    private MainContract.View mView;
    private ImagesRepository mImagesRepository;

    public MainPresenter(MainContract.View view)
    {
        mView = view;
        mImagesRepository = ImagesRepository.getInstance();
    }

    @Override
    public void getImages(String keyword)
    {
        if(mImagesRepository != null)
            mImagesRepository.getImages(keyword, mCompletedCallback);
    }

    @Override
    public void addImages()
    {
        if(mImagesRepository != null)
            mImagesRepository.addImages(mCompletedCallback);
    }

    private ImagesSource.CompletedCallback mCompletedCallback = new ImagesSource.CompletedCallback()
    {
        @Override
        public void onCompleted(ArrayList<ImageInfo> imageList, int page)
        {
            mView.setImages(imageList, page);
        }

        @Override
        public void onError()
        {
            mView.setError();
        }
    };
}
