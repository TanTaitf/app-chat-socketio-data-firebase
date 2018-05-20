package com.media.tf.chatsocketio.TabLayout_Content;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.media.tf.chatsocketio.Implements_Class.LoadUser;
import com.media.tf.chatsocketio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity.usernamegui;


public class Adapter_List_User extends BaseAdapter {
    Context context;
    int layout;
    List<LoadUser> userList;
    List<String> userOnl;

    public Adapter_List_User(Context context, int layout, List<LoadUser> userList,List<String> userOnl) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
        this.userOnl = userOnl;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class viewHolder
    {
        TextView txtTen;
        ImageView hinhanh, icon_status;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            viewHolder.txtTen = (TextView) convertView.findViewById(R.id.textViewtitle);
            viewHolder.hinhanh = (ImageView)convertView.findViewById(R.id.imgthumnail);
            viewHolder.icon_status = (ImageView) convertView.findViewById(R.id.icon_status);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (viewHolder) convertView.getTag();
        }
        LoadUser user = userList.get(position);
        if (user.getTen().matches(usernamegui)){
            userList.remove(position);
            Toast.makeText(context,"da tim xoa dc",Toast.LENGTH_SHORT).show();
        }

        if (user.getOnl() == false){

        }else {
            viewHolder.icon_status.setImageResource(android.R.drawable.presence_online);
        }
        viewHolder.txtTen.setText(user.getTen());
        Picasso.with(context)
                .load(user.getLink())
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(viewHolder.hinhanh);
        return convertView;
    }



}
