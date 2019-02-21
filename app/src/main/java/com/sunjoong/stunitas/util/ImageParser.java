package com.sunjoong.stunitas.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sunjoong.stunitas.define.Define;
import com.sunjoong.stunitas.model.data.ImageInfo;

import java.util.ArrayList;

public class ImageParser
{

    public static boolean isLastPage = false;

    public static ArrayList<ImageInfo> parse(String json)
    {
        String meta;
        String documents;

        JsonParser parser = new JsonParser();
        JsonElement element = (JsonElement) parser.parse(json);

        meta = element.getAsJsonObject().get(Define.KEY_META).toString();
        documents = element.getAsJsonObject().get(Define.KEY_DOC).toString();

        isLastPage(meta);

        return getUrlByDocuments(documents);
    }

    private static void isLastPage(String meta)
    {
        JsonParser parser = new JsonParser();
        JsonElement element = (JsonElement) parser.parse(meta);

        isLastPage = element.getAsJsonObject().get(Define.KEY_IS_END).toString().equals("true") ? true : false;
    }

    private static ArrayList<ImageInfo> getUrlByDocuments(String documents)
    {
        ArrayList<ImageInfo> list = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(documents);

        for(JsonElement element : array)
        {
            String url = element.getAsJsonObject().get(Define.KEY_IMAGE_URL).toString().replace("\"", "");
            list.add(new ImageInfo(url));
        }

        return list;
    }
}
