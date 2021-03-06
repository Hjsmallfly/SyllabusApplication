package com.example.daidaijie.syllabusapplication.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LibraryPagerAdapter;
import com.example.daidaijie.syllabusapplication.model.LibraryModel;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

import butterknife.BindView;

public class LibraryActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchEditText)
    EditText mSearchEditText;
    @BindView(R.id.searchButton)
    Button mSearchButton;
    AlertDialog mLoadingDialog;
    @BindView(R.id.libraryViewPager)
    ViewPager mLibraryViewPager;

    AlertDialog mSearchRangeDialog;
    AlertDialog mSearchOBDialog;
    AlertDialog mSearchSFDialog;
    int searchWordWhich;
    int searchSFWhich;
    int searchOBWhich;


    LibraryPagerAdapter mLibraryPagerAdapter;
    @BindView(R.id.searchRangeButton)
    Button mSearchRangeButton;
    @BindView(R.id.searchSFButton)
    Button mSearchSFButton;
    @BindView(R.id.searchOBButton)
    Button mSearchOBButton;
    @BindView(R.id.queryLayout)
    LinearLayout mQueryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeModel.getInstance().colorPrimary);

        final String[] rangeItems = getResources().getStringArray(R.array.query_lib_string);
        searchWordWhich = 0;
        mSearchRangeButton.setText(rangeItems[searchWordWhich]);
        mSearchRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchRangeDialog.show();
            }
        });
        mSearchRangeDialog = new AlertDialog.Builder(this)
                .setTitle("检索范围")
                .setItems(R.array.query_lib_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchWordWhich = which;
                        mSearchRangeButton.setText(rangeItems[searchWordWhich]);
                    }
                }).create();


        final String[] sfItems = getResources().getStringArray(R.array.query_lib_sf_string);
        searchSFWhich = 0;
        mSearchSFButton.setText(sfItems[searchSFWhich]);
        mSearchSFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchSFDialog.show();
            }
        });
        mSearchSFDialog = new AlertDialog.Builder(this)
                .setTitle("排序方式")
                .setItems(R.array.query_lib_sf_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchSFWhich = which;
                        mSearchSFButton.setText(sfItems[searchSFWhich]);
                    }
                }).create();

        final String[] obItems = getResources().getStringArray(R.array.query_lib_ob_string);
        searchOBWhich = 0;
        mSearchOBButton.setText(obItems[searchOBWhich]);
        mSearchOBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchOBDialog.show();
            }
        });
        mSearchOBDialog = new AlertDialog.Builder(this)
                .setTitle("排序方式")
                .setItems(R.array.query_lib_ob_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchOBWhich = which;
                        mSearchOBButton.setText(obItems[searchOBWhich]);
                    }
                }).create();



        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLibraryPagerAdapter = new LibraryPagerAdapter(getSupportFragmentManager(),
                        LibraryModel.getInstance().searchWords.get(searchWordWhich),
                        mSearchEditText.getText().toString().trim(),
                        LibraryModel.getInstance().libSFs.get(searchSFWhich),
                        LibraryModel.getInstance().libOBs.get(searchOBWhich));
                mLibraryViewPager.setAdapter(mLibraryPagerAdapter);
                mLibraryViewPager.setCurrentItem(0);
            }
        });


        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQueryLayout.getVisibility()==View.GONE){
                    mQueryLayout.setVisibility(View.VISIBLE);
                }else{
                    mQueryLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_library;
    }
}
