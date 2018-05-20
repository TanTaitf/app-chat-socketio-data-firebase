package com.media.tf.chatsocketio.Adapter_Class;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.media.tf.chatsocketio.R;
import com.media.tf.chatsocketio.Implements_Class.Usermessager;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter_Messagesr extends BaseAdapter {

    Context context;
    List<Usermessager> listmessager;

    public Adapter_Messagesr(Context context, List<Usermessager> listmessager) {
        this.context = context;
        this.listmessager = listmessager;
    }

    @Override
    public int getCount() {
        return listmessager.size();
    }

    @Override
    public Object getItem(int position) {
        return listmessager.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Usermessager m = listmessager.get(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (listmessager.get(position).isSelf()) {
            convertView = mInflater.inflate(R.layout.list_item_message_right,
                    null);
        } else {
            convertView = mInflater.inflate(R.layout.list_item_message_left,
                    null);
        }

        TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
        TextView time = (TextView) convertView.findViewById(R.id.lblTimeout);
        ImageView user = (ImageView) convertView.findViewById(R.id.imgviewuser);

        txtMsg.setText(m.getMessager());
        lblFrom.setText(m.getUsername());
        time.setText(m.getTime_messager());
        Picasso.with(context)
                .load(m.getLinkimg())
                .into(user);
        return convertView;
    }
}