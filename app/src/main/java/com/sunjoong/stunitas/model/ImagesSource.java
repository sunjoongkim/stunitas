package com.sunjoong.stunitas.model;

import com.sunjoong.stunitas.model.data.ImageInfo;

import java.util.ArrayList;

public interface ImagesSource
{
    interface CompletedCallback
    {
        void onCompleted(ArrayList<ImageInfo> imageList, int page);
        void onError();
    }

    void getImages(String keyword, CompletedCallback callback);
    void addImages(CompletedCallback callback);
}
