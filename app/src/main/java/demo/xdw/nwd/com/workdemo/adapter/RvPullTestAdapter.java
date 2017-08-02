package demo.xdw.nwd.com.workdemo.adapter;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import android.content.Context;

import java.util.List;

import demo.xdw.nwd.com.workdemo.R;

/**
 * Created by xudengwang on 2017/8/2.
 */

public class RvPullTestAdapter extends CommonAdapter<String> {
    public RvPullTestAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_for_rv_pull_up_test, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_name, s);

    }
}
