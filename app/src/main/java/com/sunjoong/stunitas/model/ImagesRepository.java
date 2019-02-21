package com.sunjoong.stunitas.model;

public class ImagesRepository implements ImagesSource
{
    private static ImagesRepository mImageRepository;
    private ImagesLocalDataSource mLocalDataSource;

    public static ImagesRepository getInstance()
    {
        if(mImageRepository == null)
            mImageRepository = new ImagesRepository();

        return mImageRepository;
    }

    private ImagesRepository()
    {
        mLocalDataSource = new ImagesLocalDataSource();
    }

    @Override
    public void getImages(String keyword, CompletedCallback callback)
    {
        if(mLocalDataSource != null)
            mLocalDataSource.getImages(keyword, callback);
    }

    @Override
    public void addImages(CompletedCallback callback)
    {
        if(mLocalDataSource != null)
            mLocalDataSource.addImages(callback);
    }
}
