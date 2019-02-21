package com.sunjoong.stunitas.request;

import com.sunjoong.stunitas.define.Define;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NetworkService
{
    @Headers("Authorization: KakaoAK " + Define.REST_API_KEY)
    @GET(Define.URL_SEARCH_IMAGE)
    Call<ResponseBody> getImages(@Query(Define.URL_QUERY) String query,
                                 @Query(Define.URL_PAGE) int page,
                                 @Query(Define.URL_SIZE) int size);
}
