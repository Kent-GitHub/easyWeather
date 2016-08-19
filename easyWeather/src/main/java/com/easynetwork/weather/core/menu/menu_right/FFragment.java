package com.easynetwork.weather.core.menu.menu_right;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.easynetwork.weather.R;

import java.util.ArrayList;
import java.util.List;

public class FFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private List<ListItem> mDatas;

    private LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_right, null);
        mInflater = inflater;
        mListView = (ListView) view.findViewById(R.id.ffListView);
        initDatas();
        return view;
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add(new ListItem(R.drawable.icon_locate,"更改城市"));
        mDatas.add(new ListItem(R.drawable.icon_voice,"语音播报"));
        mDatas.add(new ListItem());
        mDatas.add(new ListItem());
        mDatas.add(new ListItem());
        MyAdapter mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }


    private class MyAdapter extends BaseAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = mInflater.inflate(R.layout.f_item, null);
                viewHolder.icon = (ImageView) view.findViewById(R.id.f_icon);
                viewHolder.text = (TextView) view.findViewById(R.id.f_text);
                viewHolder.button = (Switch) view.findViewById(R.id.f_toggle);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.icon.setImageResource(mDatas.get(i).iconId);
            viewHolder.text.setText(mDatas.get(i).content);
            if (i==1){
                viewHolder.button.setVisibility(View.VISIBLE);
                viewHolder.button.setOnClickListener(this);
            }
            return view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.f_toggle:

                    break;
            }
        }

        class ViewHolder {
            ImageView icon;
            TextView text;
            Switch button;
        }

    }

    class ListItem {
        public ListItem() {
        }

        public ListItem(int iconId, String content) {
            this.iconId = iconId;
            this.content = content;
        }

        int iconId;
        String content;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {

        }
    }

}
