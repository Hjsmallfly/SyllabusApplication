package com.example.daidaijie.syllabusapplication.view;

import android.net.Uri;

import com.example.daidaijie.syllabusapplication.bean.Syllabus;

/**
 * Created by daidaijie on 2016/7/26.
 */
public interface ISyllabusFragmentView extends MvpView, IUserInfoView, ISwipeLoadingView {


    //展示课表
    void showSyllabus(Syllabus syllabus);


    //设置成功获取数据显示后的Bannner
    void showSuccessBanner();

    //设置不成功获取数据显示后的Bannner
    void showFailBannner();

    //设置ViewPager滑动
    void setViewPagerEnable(boolean enable);


}
