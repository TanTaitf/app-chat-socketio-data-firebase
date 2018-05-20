package com.media.tf.chatsocketio.Tab_User;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.media.tf.chatsocketio.TwoUserChat.TwoUserChatActivity;
import com.media.tf.chatsocketio.RomChat.RoomChatActivity;
import com.media.tf.chatsocketio.R;
import com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity;

import java.util.Locale;

import static com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity.Listtemp;
import static com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity.linkanhusergui;
import static com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity.usernamegui;

/**
 * Created by Windows 8.1 Ultimate on 10/08/2017.
 */

// implements SearchView.OnQueryTextListener
public class Fragment_Tab01 extends Fragment implements SearchView.OnQueryTextListener {

    Button btnchat;
    ListView listViewuser;
    SearchView mySearchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabhost_1,container,false);
        btnchat = (Button)view.findViewById(R.id.btn_chatroom);
        listViewuser = (ListView)view.findViewById(R.id.listviewDanhsachUser);
        mySearchView = (SearchView) view.findViewById(R.id.searchview);
        listViewuser.setAdapter(TabListUserActivity.adapter_list_user);

        mySearchView.setOnQueryTextListener(this);
        mySearchView.setQueryHint("Search name");
        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RoomChatActivity.class).putExtra("usernamegui", usernamegui).putExtra("linkanhgui", linkanhusergui));

            }
        });
        listViewuser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(),TwoUserChatActivity.class)
                        .putExtra("usernamenhan", Listtemp.get(i).getTen().toString())
                        .putExtra("hinhusernhan", Listtemp.get(i).getLink())
                        .putExtra("usernamegui", TabListUserActivity.usernamegui)
                        .putExtra("hinhusergui", TabListUserActivity.linkanhusergui));
            }
        });
        return view;
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        int i ;
        if (charText.length() == 0) {

        } else {
            for (i = 0;i < Listtemp.size(); i++ )
                if (Listtemp.get(i).getTen().toLowerCase(Locale.getDefault()).contains(charText)) {
                    final int finalI = i;
                    listViewuser.post(new Runnable() {
                        public void run() {
                            listViewuser.smoothScrollToPosition(finalI);
                        }});


                }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        filter(text);
        return true;
    }
}
