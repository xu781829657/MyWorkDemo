package demo.xdw.nwd.com.workdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.xdw.nwd.com.workdemo.R;

/**
 * Created by xudengwang on 2017/3/31.
 */

public class TestListAdapter extends BaseAdapter {


    private List<String> mMessageList;
    private LayoutInflater mInflater;

    public TestListAdapter(Context context,
                           List<String> messageList) {
        mMessageList = messageList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return mMessageList.size();
    }

    @Override
    public Object getItem(int arg0) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_for_test_list, null);
        }

        TextView messageTxt = (TextView) convertView
                .findViewById(R.id.tv_title);
        messageTxt.setText(mMessageList.get(position));

        return convertView;
    }

}

