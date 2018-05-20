package com.media.tf.chatsocketio.UI_Background;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.media.tf.chatsocketio.R;

import java.util.ArrayList;

/**
 * Created by Windows 8.1 Ultimate on 10/08/2017.
 */

public class Fragment_Backgr_Mss extends Fragment {
    ArrayAdapter adapter;
    ArrayList arrayListColor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_mss,container,false);
        GridView gridView = view.findViewById(R.id.grd_backgr);
        arrayListColor = new ArrayList();
        addArrayColor();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayListColor);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return view;
    }
    private void addArrayColor(){
        arrayListColor.add("#8e5cd4");
        arrayListColor.add("#b1bc39");
        arrayListColor.add("#dc5562");
        arrayListColor.add("#398de3");
    }

}
