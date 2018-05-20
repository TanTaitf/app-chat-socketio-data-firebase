package com.media.tf.chatsocketio.TabLayout_Content;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.media.tf.chatsocketio.Implements_Class.LoadUser;
import com.media.tf.chatsocketio.Implements_Class.User;
import com.media.tf.chatsocketio.Implements_Class.Usermessager;
import com.media.tf.chatsocketio.R;
import com.media.tf.chatsocketio.RomChat.RoomChatActivity;
import com.media.tf.chatsocketio.Tab_Setting.Fragment_Tab02;
import com.media.tf.chatsocketio.Tab_User.Fragment_Tab01;
import com.media.tf.chatsocketio.TwoUserChat.TwoUserChatActivity;
import com.media.tf.chatsocketio.UI_Background.ViewPagerColorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import io.socket.emitter.Emitter;

import static com.media.tf.chatsocketio.HomeApp.HomeActivity.msocket;
import static com.media.tf.chatsocketio.RomChat.RoomChatActivity.database;

public class TabListUserActivity extends AppCompatActivity {

    // custom list user
    ArrayList<String> arrayuser;
    ArrayList<LoadUser> userArrayList;

    public static String usernamegui = "";
    public  static Adapter_List_User adapter_list_user;
    public static ArrayList<LoadUser> Listtemp;
    public static String linkanhusergui = "";
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<String> userOnline;

    ViewPager myViewPager;
    TabLayout myTabLayout;
    private MediaPlayer mediaPlayer;
    // khai bao den firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabListUserActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tab);
        Anhxa();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_message);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                userOnline = new ArrayList<>();
                // list user custom
                userArrayList = new ArrayList<>();
                Listtemp = new ArrayList<>();
                // khoi tao tham chieu database
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Intent intentusername = getIntent();
                usernamegui = intentusername.getStringExtra("usernamegui");
                linkanhusergui = intentusername.getStringExtra("linkanh");
                loadViewPagerWithTabLayout();

                // gán list user
                arrayuser = new ArrayList<>();
                msocket.on("server-send-chat",onListChat);
                msocket.emit("Listuser", "");
                msocket.emit("nameUserOnline", usernamegui);
                msocket.on("nameUserOnline",UserOnline);
                msocket.on("List-user",onListUserOnline);
                msocket.on("message-client-result",onChatTwoClient);

                ReadUserList(userOnline);
                adapter_list_user = new Adapter_List_User(getApplicationContext(),R.layout.custom_listview_user,Listtemp,arrayuser);
            }catch (Exception e){

            }

        } else {
            showDialog();
        }

    }
    private  void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Internet Connection Error");
        builder.setIcon(R.drawable.icons_wifi_filled25);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                return;
            }
        });
        builder.show();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if(mediaPlayer != null){
                mediaPlayer.release();
            }
            finish();
            ActivityCompat.finishAffinity(this);
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        msocket.disconnect();
        finish();
        super.onDestroy();
    }

    private Emitter.Listener onChatTwoClient = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.release();

                    String usergui = "";
                    String usernhan = "";
                    String message = "";
                    JSONObject object = (JSONObject) args[0];
                    String date = GetDate();

                    try {
                        JSONObject object_node = object.getJSONObject("node");
                        usergui = object_node.optString("usergui");
                        usernhan = object_node.optString("usernhan");
                        message = object_node.optString("messagenew");
                        String linkimg  = object_node.optString("linkimg");

                        if (usergui.equals(TwoUserChatActivity.usernamenhan) && usernhan.equals(TwoUserChatActivity.usernamegui)) {
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_message);
                            mediaPlayer.start();
                            Toast toast = Toast.makeText(getApplicationContext(), "Tin nhắn từ " + usergui + "!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            boolean isSelf = false;
                            Usermessager m = new Usermessager(usergui, message, date, linkimg, isSelf);
                            TwoUserChatActivity.listMessages.add(m);
                            TwoUserChatActivity.adapter.notifyDataSetChanged();
                            database.QueryData("INSERT INTO SMSTWO VALUES(null, '" + usergui + "', '" + usernhan + "','" + message + "','" + date + "','" + linkimg + "')");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    private String GetDate() {
        // chọn java.util
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dindanggio =new SimpleDateFormat("hh:mm");//HH:mm:ss a
        String date = " "+ dindanggio.format(calendar.getTime()) +" " + simpleDateFormat.format(calendar.getTime()) ;
        return date;
    }
    public Emitter.Listener onListChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.release();
                    JSONObject object = (JSONObject) args[0];
                    try {
                        JSONObject object_node = object.getJSONObject("chatContent");
                        String usergui = object_node.optString("usergui");
                        String message = object_node.optString("messagenew");
                        String linkimg = object_node.optString("linkimg");
                        String date = GetDate();
                        if(usergui.equals(usernamegui)== false){
                            boolean isSelf = false;
                            Toast toast =  Toast.makeText(getApplicationContext(), "Tin nhắn từ Phòng Chat !", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_message);
                            mediaPlayer.start();
                            Usermessager m = new Usermessager(usergui, message,date,linkimg, isSelf);
                            RoomChatActivity.listMessages.add(m);
                            RoomChatActivity.adapter.notifyDataSetChanged();
                            database.QueryData("INSERT INTO SMS VALUES(null, '" + usergui + "','" + message + "','"+date+"','"+linkimg+"')");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    // set view pager with tablayout
    public void loadViewPagerWithTabLayout(){

        ViewPagerColorAdapter adapter = new ViewPagerColorAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Tab01(),"User Onl");
        adapter.addFragment(new Fragment_Tab02(),"Setting");
        myViewPager.setAdapter(adapter);
        myTabLayout.setupWithViewPager(myViewPager);
        // gán icon cho tab
        View viewTab1 = getLayoutInflater().inflate(R.layout.tab_layout_cumstom,null);
        viewTab1.findViewById(R.id.imgtab).setBackgroundResource(R.drawable.icons8usegroupmanmanilled_64);
        myTabLayout.getTabAt(0).setCustomView(viewTab1);

        View viewTab2 = getLayoutInflater().inflate(R.layout.tab_layout_cumstom,null);
        viewTab2.findViewById(R.id.imgtab).setBackgroundResource(R.drawable.icons8_setting64);
        myTabLayout.getTabAt(1).setCustomView(viewTab2);
    }

    private void ReadUserList(final ArrayList<String> a){
        // đọc danh sách user
        mDatabase.child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        User user = dataSnapshot.getValue(User.class);
                        if (a.contains(user.getTen())){
                            if (user.getTen().matches(usernamegui) == false){
                                Listtemp.add(new LoadUser(user.getTen(),user.getLinkimg(),true));
                            }
                        }
                        else {
                            if (user.getTen().matches(usernamegui)==false){
                                Listtemp.add(new LoadUser(user.getTen(),user.getLinkimg(),false));
                            }
                        }
                        Collections.sort(Listtemp);
                        adapter_list_user.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void Anhxa() {
        myViewPager = (ViewPager)findViewById(R.id.myViewPager);
        myTabLayout = (TabLayout)findViewById(R.id.myTablayout);
    }

    private void KiemtraUserOnline(ArrayList<String> a, ArrayList<LoadUser> b){
        Toast.makeText(getApplicationContext(),"dang kiem tra user",Toast.LENGTH_SHORT).show();
        for( int i = 0; i < b.size(); i++){
//            for ( int j = 0; j < b.size(); j++){

                if (!a.contains(b.get(i).getTen().toString())){
                    Listtemp.add(new LoadUser(b.get(i).getTen(),b.get(i).getLink(),true));
                    Toast.makeText(getApplicationContext(),"dang add true",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"dang add true",Toast.LENGTH_SHORT).show();
                    Listtemp.add(new LoadUser(b.get(i).getTen(),b.get(i).getLink(),false));
                }

            //}
        }
    }
    private Emitter.Listener onListUserOnline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject object = (JSONObject) args[0];

                    try {
                        JSONArray jsonArray = object.getJSONArray("danhsach");
                        arrayuser.clear();
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            String listuser = jsonArray.optString(i);
                            arrayuser.add(listuser);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    };
    private Emitter.Listener UserOnline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];

                    try {
                        JSONArray jsonArray = object.getJSONArray("danhsach");
                        userOnline.clear();
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            String listuser = jsonArray.optString(i);
                            userOnline.add(listuser);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    };


}
