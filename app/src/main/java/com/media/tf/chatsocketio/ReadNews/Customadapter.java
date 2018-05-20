package com.media.tf.chatsocketio.ReadNews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.media.tf.chatsocketio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Customadapter extends ArrayAdapter<Docbao> {

    public Customadapter(Context context, int resource, List<Docbao> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.dong_layout_list_news, null);
        }
        Docbao p = getItem(position);
        if (p != null) {
            TextView txttitle = (TextView) view.findViewById(R.id.textViewtitlenew);
            txttitle.setText(p.title);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewnews);
            Picasso.with(getContext()).load(p.image).into(imageView);
        }
        return view;
    }

}