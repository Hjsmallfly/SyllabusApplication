package com.example.daidaijie.syllabusapplication.presenter;

import android.util.Log;

import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.SyllabusGrid;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.model.LessonModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.GetUserBaseService;
import com.example.daidaijie.syllabusapplication.service.UserInfoService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import org.joda.time.DateTime;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/8/24.
 */
public class LoginPresenter extends ILoginPresenter {

    @Override
    public void login(final String username, final String password) {

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            mView.showLoginFail();
            return;
        }

        if (User.getInstance().getUserBaseBean() != null && User.getInstance().getUserInfo() != null) {
            if (User.getInstance().getUserInfo().getToken() != null
                    && !User.getInstance().getUserInfo().getToken().isEmpty()) {
                mView.showLoginSuccess();
                return;
            }
        }

        mView.showLoadingDialog();

        final UserInfoService service = RetrofitUtil.getDefault().create(UserInfoService.class);
        GetUserBaseService userBaseService = RetrofitUtil.getDefault().create(GetUserBaseService.class);

        String queryYear = "";
        String querySem = "";
        DateTime dateTime = DateTime.now();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        if (month < 8) {
            queryYear = (year - 1) + "-" + year;
            querySem = "1";
        } else {
            queryYear = year + "-" + (year + 1);
            querySem = "2";
        }

        final String finalQueryYear = queryYear;
        final String finalQuerySem = querySem;

        userBaseService.get_user(username)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<UserBaseBean, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(UserBaseBean userBaseBean) {
                        User.getInstance().setUserBaseBean(userBaseBean);
                        return service.getUserInfo(
                                userBaseBean.getAccount(),
                                password,
                                "query",
                                finalQueryYear
                                , finalQuerySem
                        );
                    }
                })
                .flatMap(new Func1<UserInfo, Observable<Lesson>>() {
                    @Override
                    public Observable<Lesson> call(UserInfo userInfo) {
                        //这里就算登陆失败userInfo还是获取到，且id=0
                        User.getInstance().setUserInfo(userInfo);
                        return Observable.from(userInfo.getClasses());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Lesson>() {

                    private int colorIndex = 0;
                    Syllabus mSyllabus;

                    @Override
                    public void onStart() {
                        super.onStart();
                        mSyllabus = new Syllabus();
                    }

                    @Override
                    public void onCompleted() {

                        User.getInstance().setAccount(username);
                        User.getInstance().setPassword(password);
                        LessonModel.getInstance().save();
                        mView.dismissLoadingDialog();
                        mView.showLoginSuccess();

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoadingDialog();
                        mView.showLoginFail();
                    }

                    @Override
                    public void onNext(Lesson lesson) {
                        //将lesson的时间格式化
                        lesson.convertDays();
                        lesson.setBgColor(Syllabus.bgColors[colorIndex++ % Syllabus.bgColors.length]);

                        //获取该课程上的节点上的时间列表
                        List<Lesson.TimeGird> timeGirds = lesson.getTimeGirds();
                        /*if (timeGirds.size() != 0) {
                            Log.d(TAG, "onNext: " + timeGirds.get(0).getTimeList());
                        }*/
                        //把该课程添加到课程管理去
                        LessonModel.getInstance().addLesson(lesson);
                        for (int i = 0; i < timeGirds.size(); i++) {
                            Lesson.TimeGird timeGrid = timeGirds.get(i);
                            for (int j = 0; j < timeGrid.getTimeList().length(); j++) {
                                char x = timeGrid.getTimeList().charAt(j);
                                int time = Syllabus.chat2time(x);

                                SyllabusGrid syllabusGrid = mSyllabus.getSyllabusGrids()
                                        .get(timeGrid.getWeekDate())
                                        .get(time);



                                //将该课程添加到时间节点上去
                                syllabusGrid.getLessons().add(lesson.getIntID());
                            }
                        }
                    }
                });
    }
}
