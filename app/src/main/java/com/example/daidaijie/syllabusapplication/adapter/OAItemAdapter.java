package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.OABean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class OAItemAdapter extends RecyclerView.Adapter<OAItemAdapter.ViewHolder> {

    Activity mActivity;

    List<OABean> mOABeen;


    public OAItemAdapter(Activity activity, List<OABean> OABeen) {
        mActivity = activity;
        mOABeen = OABeen;
    }

    public List<OABean> getOABeen() {
        return mOABeen;
    }

    public void setOABeen(List<OABean> OABeen) {
        mOABeen = OABeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_oa_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OABean oaBean = mOABeen.get(position);

        holder.mOASubTextView.setText("" + oaBean.getSUBCOMPANYNAME());
        holder.mOATimeTextView.setText("" + oaBean.getDOCVALIDDATE() + " " + oaBean.getDOCVALIDTIME());
        holder.mOATitleTextView.setText("" + oaBean.getDOCSUBJECT());


    }

    @Override
    public int getItemCount() {
        if (mOABeen == null) {
            return 0;
        }
        return mOABeen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.oASubTextView)
        TextView mOASubTextView;
        @BindView(R.id.oATimeTextView)
        TextView mOATimeTextView;
        @BindView(R.id.oATitleTextView)
        TextView mOATitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
