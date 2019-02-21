package com.sunjoong.stunitas.presenter;

import com.sunjoong.stunitas.model.data.ImageInfo;

import java.util.ArrayList;

public class MainContract
{
    public interface View
    {
        void setImages(ArrayList<ImageInfo> imageList, int page);
        void setError();
    }

    interface Presenter
    {
        void getImages(String keyword);
        void addImages();
    }
}
