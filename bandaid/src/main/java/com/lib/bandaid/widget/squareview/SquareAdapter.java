package com.lib.bandaid.widget.squareview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lib.bandaid.R;
import java.util.List;

/**
 * Created by zy on 2017/9/18.
 */

public class SquareAdapter extends BaseAdapter {

    private List<GridItem> mItems;
    private Context mContext;

    public SquareAdapter(Context context, List<GridItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(GridItem item) {
        mItems.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.widget_square_list_item, null);
        ImageView image = convertView.findViewById(R.id.tvIcon);
        TextView text = convertView.findViewById(R.id.tvName);
        GridItem item = (GridItem) getItem(position);
        image.setImageResource(item.getIcon());
        text.setText(item.getName());
        return convertView;
    }
}
