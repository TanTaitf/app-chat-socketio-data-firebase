package com.media.tf.chatsocketio.Tab_Setting;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.media.tf.chatsocketio.InfoHoho.InfoHohoActivity;
import com.media.tf.chatsocketio.RomChat.RoomChatActivity;
import com.media.tf.chatsocketio.R;
import com.media.tf.chatsocketio.ReadNews.ListNewsActivity;
import com.media.tf.chatsocketio.ScanQR.ScanQRActivity;
import com.media.tf.chatsocketio.UpdateAccount.UpdateAccountActivity;

import static com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity.linkanhusergui;
import static com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity.usernamegui;

/**
 * Created by Windows 8.1 Ultimate on 10/08/2017.
 */

public class Fragment_Tab02 extends Fragment {

    TextView txt_acount;
    TextView txt_font;
    TextView txt_infohoho;
    TextView txt_phongtrochuyen;
    TextView txt_QR;
    TextView txt_readnews;
    ImageView img_account;
    ImageView img_font;
    ImageView img_infohoho;
    ImageView img_phongtrochuyen;
    ImageView img_QR;
    ImageView img_readnews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tabhost_2,container,false);
        txt_acount = (TextView)view.findViewById(R.id.txt_account);
        txt_phongtrochuyen = (TextView)view.findViewById(R.id.txt_phongtrochuyen);
        txt_font = (TextView)view.findViewById(R.id.txt_font);
        txt_infohoho = (TextView)view.findViewById(R.id.txt_infohoho);
        txt_QR = (TextView)view.findViewById(R.id.txt_QR);
        txt_readnews = (TextView)view.findViewById(R.id.txt_Readnews);


        txt_phongtrochuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RoomChatActivity.class).putExtra("usernamegui", usernamegui).putExtra("linkanhgui", linkanhusergui));
            }
        });
        txt_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialog();
            }
        });

        txt_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScanQRActivity.class));
            }
        });

        txt_infohoho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InfoHohoActivity.class));
            }
        });
        txt_acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpdateAccountActivity.class));
            }
        });
        txt_readnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ListNewsActivity.class));
            }
        });


        return view;
    }
    private  void showDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Cài đặt Font");
        builder.setMessage("Font chữ đã được cài đặt");

        builder.setIcon(R.drawable.img_font_dialog_filled_32);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.setCancelable(true);
            }
        });
        builder.show();

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
