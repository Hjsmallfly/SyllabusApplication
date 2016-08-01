package com.example.daidaijie.syllabusapplication.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.StudentInfoAdapter;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;
import com.example.daidaijie.syllabusapplication.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.finalteam.toolsfinal.StringUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ClassmateListActivity extends BaseActivity {

    public static final String EXTRA_STUDENT_LIST =
            "com.example.daidaijie.syllabusapplication.student_list";

    public static final String EXTRA_BG_COLOR =
            "com.example.daidaijie.syllabusapplication.bg_color";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.classmateRecyclerView)
    RecyclerView mClassmateRecyclerView;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.classmateRootLayout)
    LinearLayout mClassmateRootLayout;
    SearchView mSearchView;

    private List<StudentInfo> mStudentInfos;

    private StudentInfoAdapter mStudentInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }

        mStudentInfos = (List<StudentInfo>) getIntent().getSerializableExtra(EXTRA_STUDENT_LIST);
        mStudentInfoAdapter = new StudentInfoAdapter(this, mStudentInfos);
        mClassmateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mClassmateRecyclerView.setAdapter(mStudentInfoAdapter);

        mToolbar.setTitle("");

        int bgColor = getResources()
                .getColor(getIntent().getIntExtra(EXTRA_BG_COLOR, R.color.colorPrimary));
        mToolbar.setBackgroundColor(bgColor);

        setSupportActionBar(mToolbar);
        setupToolbar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_classmate_list;
    }

    public static Intent getIntent(Context packageContext, List<StudentInfo> studentInfos, int bgColor) {
        Intent intent = new Intent(packageContext, ClassmateListActivity.class);
        intent.putExtra(EXTRA_STUDENT_LIST, (Serializable) studentInfos);
        intent.putExtra(EXTRA_BG_COLOR, bgColor);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //判断是返回键然后退出当前Activity
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_classmate_list, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setQueryHint("搜索姓名\\学号\\专业\\性别");
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        textView.setTextSize(14);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryClassmateList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryClassmateList(newText);
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                Toast.makeText(ClassmateListActivity.this, "onExpand", Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(ClassmateListActivity.this, "Collapse", Toast.LENGTH_LONG).show();
                mStudentInfoAdapter.setStudentInfos(mStudentInfos);
                mStudentInfoAdapter.notifyDataSetChanged();
                return true;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void queryClassmateList(final String quertText) {
        Observable.from(mStudentInfos)
                .subscribeOn(Schedulers.computation())
                .filter(new Func1<StudentInfo, Boolean>() {
                    @Override
                    public Boolean call(StudentInfo studentInfo) {
                        if (quertText.length() == 0) {
                            return true;
                        }
                        //判断为学号
                        if (StringUtil.isNumberic(quertText)) {
                            if (studentInfo.getNumber().contains(quertText)) {
                                return true;
                            }
                        }
                        //判断性别
                        if (quertText.length() == 1) {
                            if (studentInfo.getGender().equals(quertText)) {
                                return true;
                            }
                        }
                        if (studentInfo.getName().contains(quertText) ||
                                studentInfo.getMajor().contains(quertText)) {
                            return true;
                        }

                        return false;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentInfo>() {

                    List<StudentInfo> mQueryInfo;

                    @Override
                    public void onStart() {
                        super.onStart();
                        mQueryInfo = new ArrayList<StudentInfo>();
                    }

                    @Override
                    public void onCompleted() {
                        mStudentInfoAdapter.setStudentInfos(mQueryInfo);
                        mStudentInfoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StudentInfo studentInfo) {
                        mQueryInfo.add(studentInfo);
                    }
                });
    }
}
