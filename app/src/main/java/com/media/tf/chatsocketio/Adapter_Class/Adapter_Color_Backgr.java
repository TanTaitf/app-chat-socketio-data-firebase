package com.media.tf.chatsocketio.Adapter_Class;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.media.tf.chatsocketio.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Color_Backgr extends BaseAdapter {
    private Context context;
    private int layout;
    private List<com.media.tf.chatsocketio.UI_Background.Color> ListDanhsach;
    ArrayList <String> ArrayScreen;

    public Adapter_Color_Backgr(Context context, int layout, List<com.media.tf.chatsocketio.UI_Background.Color> listDanhsach) {
        this.context = context;
        this.layout = layout;
        ListDanhsach = listDanhsach;
    }

    @Override

    public int getCount() {
        return ListDanhsach.size();
    }

    @Override
    public Object getItem(int position) {
        return ListDanhsach.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class viewHolder
    {
        LinearLayout linearLayout;
        ImageView imgview;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.item_gridview);
            viewHolder.imgview = (ImageView)convertView.findViewById(R.id.img_item_grid);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (viewHolder)convertView.getTag();
        }
        com.media.tf.chatsocketio.UI_Background.Color danhsach = ListDanhsach.get(position);
        viewHolder.imgview.setBackgroundColor(Color.parseColor(danhsach.getColor()));
        return convertView;
    }

}
