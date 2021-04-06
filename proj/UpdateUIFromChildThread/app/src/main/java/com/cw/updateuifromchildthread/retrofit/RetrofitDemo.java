package com.cw.updateuifromchildthread.retrofit;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// sample : https://square.github.io/retrofit/

// https://api.github.com/users/octocat/repos

public class RetrofitDemo {
    public void testSyncRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")  // set url
                .addConverterFactory(GsonConverterFactory.create())  // set date parser
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // set support RxJava
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<List<Repo>> repos = service.listRepos("octocat");

        try {
            repos.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testAsyncRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<List<Repo>> repos = service.listRepos("octocat");

        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {

            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

            }
        });
    }
}
