package com.example.daidaijie.syllabusapplication.service;

import android.support.annotation.Nullable;

import com.example.daidaijie.syllabusapplication.bean.LoginInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/3.
 */
public interface LoginInternetService {

    @FormUrlEncoded
    @POST("login.php")
    Observable<String> loginInternet(@Field("opr") String opr,
                                        @Field("userName") String username,
                                        @Field("pwd") String password,
                                        @Field("rememberPwd") String isRemember);
}
