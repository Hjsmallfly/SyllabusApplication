package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/16.
 * 获取用户
 */
public interface GetUserBaseService {
    @GET("/interaction/api/v2/compatible_user/{account}")
    Observable<UserBaseBean> get_user(@Path("account") String account);
}
