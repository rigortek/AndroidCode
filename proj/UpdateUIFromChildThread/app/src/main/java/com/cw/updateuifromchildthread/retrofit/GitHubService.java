package com.cw.updateuifromchildthread.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Create by robin On 21-3-16
 */
public interface GitHubService {
    // @GET表示GET请求类型
    // @Path表示如果user为空，则使用默认参数参数r
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);
}
