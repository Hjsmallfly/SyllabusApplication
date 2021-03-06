package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.GradeInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/19.
 */
public interface GradeService {

    @FormUrlEncoded
    @POST("credit/api/v2/grade")
    Observable<GradeInfo> getGrade(@Field("username") String username,
                                   @Field("password") String password);
}
