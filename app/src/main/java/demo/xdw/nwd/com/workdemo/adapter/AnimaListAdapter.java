package demo.xdw.nwd.com.workdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.base.frame.Base;

import java.util.List;

import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.demo.anima.FadeAnimaActivity;
import demo.xdw.nwd.com.workdemo.demo.anima.FadeAnimaFixHeightActivity;
import demo.xdw.nwd.com.workdemo.demo.anima.Rotate3DActivity;
import demo.xdw.nwd.com.workdemo.demo.circleprogress.CircleProgressDemoActivity;
import demo.xdw.nwd.com.workdemo.demo.hongbao.HongbaoActivity;
import demo.xdw.nwd.com.workdemo.demo.onlineservice.OnlineServiceEntryActivity;
import demo.xdw.nwd.com.workdemo.demo.randomdrag.RandowDragActivity;
import demo.xdw.nwd.com.workdemo.demo.rules.RulesDemoActivity;
import demo.xdw.nwd.com.workdemo.demo.rxjava.RxjavaTestActivity;
import demo.xdw.nwd.com.workdemo.demo.test.TestActivity;
import demo.xdw.nwd.com.workdemo.demo.turntable.TurnTableActivity;
import demo.xdw.nwd.com.workdemo.demo.video.VideoActivity;
import demo.xdw.nwd.com.workdemo.util.SkipUtil;

public class AnimaListAdapter extends RecyclerView.Adapter<AnimaListAdapter.ViewHolder> {

    private List<String> mDemoTitles;
    private int[] mUsernameColors;
    private Context mContext;

    public AnimaListAdapter(Context context, List<String> titles) {
        mContext = context;
        mDemoTitles = titles;
    }

    /**
     * ItemView的单击事件(如果需要，重写此方法就行)
     *
     * @param position
     */
    protected void onItemClick(int position) {
        switch (position){
            case 0:
                //X轴3D旋转
                SkipUtil.gotoActivity(mContext, Rotate3DActivity.class);
                break;
            case 1:
                //下拉展开，上滑收缩
                SkipUtil.gotoActivity(mContext, FadeAnimaActivity.class);
                break;
            case 2:
                //下拉展开，上滑收缩固定高度
                SkipUtil.gotoActivity(mContext, FadeAnimaFixHeightActivity.class);
                break;
        }
        Base.showToast("" + position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_for_main_rv;
//        switch (viewType) {
//            case Message.TYPE_MESSAGE:
//                layout = R.layout.item_message;
//                break;
//            case Message.TYPE_LOG:
//                layout = R.layout.item_log;
//                break;
//            case Message.TYPE_ACTION:
//                layout = R.layout.item_action;
//                break;
//        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String title = mDemoTitles.get(position);
        viewHolder.setTitle(title);
    }

    @Override
    public int getItemCount() {
        return mDemoTitles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(getAdapterPosition());
                }
            });
            mTitleView = (TextView) itemView.findViewById(R.id.tv_demo_name);
        }
        public void setTitle(String title) {
            if (null == mTitleView) return;
            mTitleView.setText(title);
        }


    }
}