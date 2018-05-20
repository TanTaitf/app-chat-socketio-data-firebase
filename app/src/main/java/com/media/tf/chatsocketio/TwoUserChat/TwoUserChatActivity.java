package com.media.tf.chatsocketio.TwoUserChat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.media.tf.chatsocketio.Adapter_Class.Adapter_Color_Backgr;
import com.media.tf.chatsocketio.Adapter_Class.Adapter_Messagesr;
import com.media.tf.chatsocketio.DataBase.Database;
import com.media.tf.chatsocketio.Implements_Class.Usermessager;
import com.media.tf.chatsocketio.R;
import com.media.tf.chatsocketio.RomChat.RoomChatActivity;
import com.media.tf.chatsocketio.UI_Background.Color;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.media.tf.chatsocketio.Hellper.Hellper.url_Socket;
import static com.media.tf.chatsocketio.R.id.btn_select_backgr;
import static com.media.tf.chatsocketio.RomChat.RoomChatActivity.database;

public class TwoUserChatActivity extends AppCompatActivity {

    Button btnsendmessg;
    private Socket msocket;
    EditText edt_inputmessg;
    TextView txtnameclient;

    public static ListView  lv_noidungchat;
    String key = "";
    public static String usernamenhan = "", usernamegui = "";
    public static String linkanhusernhan = "", linkanhusergui = "";
    public static Adapter_Messagesr adapter;
    public static List<Usermessager> listMessages;

    private Dialog dialog;
    GridView gridView ;
    Button btn_Sel;

    LinearLayout layooutReqairMessg;
    SharedPreferences sharedPreferences;
    boolean onclick_nav = true;
    Toolbar myToolbar;
    DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    Boolean check_nav = false;
    String user = "";
    LinearLayout linearLayout_twochat;
    ArrayList<Integer> getArrayId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentusername = getIntent();
        usernamenhan = intentusername.getStringExtra("usernamenhan");
        usernamegui = intentusername.getStringExtra("usernamegui");
        linkanhusernhan = intentusername.getStringExtra("hinhusernhan");
        linkanhusergui = intentusername.getStringExtra("hinhusergui");

        setTitle(usernamenhan);
        setContentView(R.layout.activity_twouserchat);
        Anhxa();

        getArrayId = new ArrayList<>();

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitleTextColor(android.graphics.Color.WHITE);
        myNavigationView.setItemIconTintList(null);


        // ánh xạ view header
        View headerView = myNavigationView.getHeaderView(0);
        final TextView textView = (TextView)headerView.findViewById(R.id.txttitle_header);
        final ImageView imageView = (ImageView)headerView.findViewById(R.id.img_header);

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.info) {
                    if (myDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                        myDrawerLayout.closeDrawer(GravityCompat.END);
                    } else {
                        myDrawerLayout.openDrawer(GravityCompat.END);
                        textView.setText(usernamenhan);
                        Picasso.with(getApplicationContext())
                                .load(linkanhusernhan)
                                .into(imageView);
                    }
                }
                return false;
            }
        });
        getSharePre();

        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.cai_dat_hinh_nen:
                        myDrawerLayout.closeDrawer(Gravity.END);
                        DialoglistviewSelectIntem();
                        break;
                    case R.id.gui_mail:
                        try {
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.setType("plain/text");
                            sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "usernamenull@gmail.com" });
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi You !!!");
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is a message sent from Hoho Chat app :-)");
                            startActivity(sendIntent);
                        }catch (Exception e){
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            startActivity(emailIntent);
                        }
                        break;
                    case R.id.danh_dau_ban_chung:
                        if (onclick_nav == true) {
                            item.setChecked(true);
                            item.setIcon(R.drawable.icons_user_manfilled_32);
                            onclick_nav = false;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("name",true);
                            editor.putString(""+usernamenhan,usernamenhan);
                            editor.commit();

                        }else {
                            item.setChecked(false);
                            onclick_nav = true;
                            item.setIcon(R.drawable.icons_user_group_32);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("name");
                            editor.remove(""+usernamenhan);
                            editor.commit();
                        }
                        break;
                    case R.id.chup_man_hinh:
                        myDrawerLayout.closeDrawer(Gravity.END);
                        CountDownTimer();
                        break;
                    case R.id.thong_tin_facebook:

                        try{
                            getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/<id_here>")));
                        }catch (Exception e){
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("http://www.facebook.com.vn"));
                            startActivity(intent);
                        }
                        break;
                }
                return false;
            }
        });

        key = usernamegui;
        listMessages = new ArrayList<Usermessager>();
        adapter = new Adapter_Messagesr(TwoUserChatActivity.this, listMessages);

        database = new Database(this,"quanlyhoho.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS SMSTWO( Id INTEGER PRIMARY KEY AUTOINCREMENT, KeyUserGUI VARCHAR(200), TenUserNHAN VARCHAR(200),Noidung VARCHAR(200), Ngay VARCHAR(200), LinkHinh VARCHAR(300))");

        getCussor();
        lv_noidungchat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv_noidungchat.setAdapter(adapter);

        lv_noidungchat.post(new Runnable(){
            public void run() {
                lv_noidungchat.setSelection(lv_noidungchat.getCount() - 1);
            }});
        try {
            msocket = IO.socket(url_Socket);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        msocket.connect();
        msocket.emit("ChatTwoClient", usernamenhan, usernamegui);
        msocket.on("typing",onstatusTyping);
        msocket.on("stop typing",onstatusStopTyping);
        msocket.on("ketnoi",onConectTwoClient);

        onClickEditTextView();
        btnsendmessg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edt_inputmessg.getText().toString().trim();
                String date = GetDate();
                if (!input.isEmpty()) {
                    boolean isSelf = true;
                    Usermessager m = new Usermessager(usernamegui, input, date, linkanhusergui, isSelf);
                    listMessages.add(m);
                    adapter.notifyDataSetChanged();
                    msocket.emit("message-two-client", usernamenhan, usernamegui, input, key, linkanhusergui);
                    database.QueryData("INSERT INTO SMSTWO VALUES(null, '" + usernamegui + "', '" + usernamenhan + "','" + input + "','"+date+"','"+linkanhusergui+"')");
                }
                edt_inputmessg.setText("");
                btnsendmessg.setBackgroundResource(R.drawable.congxan);
            }
        });

        lv_noidungchat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(listMessages.get(i).getMessager(),getArrayId.get(i));
                return false;
            }
        });


    }
    public boolean checkInternet(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    private  void showDialogCheckInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Internet Connection Error");
        builder.setIcon(R.drawable.icons_wifi_filled25);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                return;
            }
        });
        builder.show();
    }
    private  void showDialog(final String text,int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String []array = {"Copy Text", "Xóa Tin nhắn", "Hủy"};
        builder.setCancelable(false);
        final String ten = String.valueOf(i);
        builder.setTitle("Chọn hành động");
        builder.setIcon(R.drawable.icon_info);

        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        CopyText(text);
                        break;
                    case 1:
                        database.QueryData("DELETE FROM SMSTWO WHERE Id = '"+ten+"'");
                        getCussor();
                        break;
                    case 2:
                        break;
                }
            }
        });
        builder.show();

    }
    private void CopyText(String text){
        int sdk_Version = android.os.Build.VERSION.SDK_INT;
        if(sdk_Version < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(getApplicationContext(), "Đã Copy!", Toast.LENGTH_SHORT).show();
        }
        else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Đã Copy!", Toast.LENGTH_SHORT).show();
        }
    }
    private  void DialoglistviewSelectIntem() {
        dialog = new Dialog(TwoUserChatActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_backgr);
        dialog.setCanceledOnTouchOutside(false);
        AnhxaDialog(dialog);
        final ArrayList<Color> a  = new ArrayList<>();
        a.add(new Color("","#EF9A9A"));
        a.add(new Color("","#F44336"));
        a.add(new Color("","#9C27B0"));
        a.add(new Color("","#9575CD"));
        a.add(new Color("","#7986CB"));
        a.add(new Color("","#3949AB"));
        a.add(new Color("","#64B5F6"));
        a.add(new Color("","#2196F3"));
        a.add(new Color("","#80DEEA"));
        a.add(new Color("","#26C6DA"));
        a.add(new Color("","#4DB6AC"));
        a.add(new Color("","#009688"));
        a.add(new Color("","#81C784"));
        a.add(new Color("","#4CAF50"));
        a.add(new Color("","#B2FF59"));
        a.add(new Color("","#76FF03"));
        a.add(new Color("","#FFF176"));
        a.add(new Color("","#FFEB3B"));
        a.add(new Color("","#FF8A65"));
        a.add(new Color("","#FF5722"));
        a.add(new Color("","#A1887F"));
        a.add(new Color("","#795548"));
        a.add(new Color("","#90A4AE"));
        a.add(new Color("","#607D8B"));
        final Adapter_Color_Backgr adapter = new Adapter_Color_Backgr(this, R.layout.item_gridview, a);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                linearLayout_twochat.setBackgroundColor(android.graphics.Color.parseColor(a.get(i).getColor()));

            }
        });
        btn_Sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void AnhxaDialog(Dialog dialog) {
        // anh xa dialog
        gridView = (GridView)dialog.findViewById(R.id.grd_backgr);
        btn_Sel = (Button)dialog.findViewById(btn_select_backgr);
    }

    public void getSharePre(){
        sharedPreferences = getSharedPreferences("Navigation",MODE_PRIVATE);
        check_nav = sharedPreferences.getBoolean("name",false);
        user = sharedPreferences.getString(""+usernamenhan,"");
        if (user.equals(usernamenhan) && check_nav == true){
            onclick_nav = false;
            myNavigationView.getMenu().getItem(2).setChecked(true).setIcon(R.drawable.icons_user_manfilled_32);
        }
    }
    private void CountDownTimer(){
        final CountDownTimer courTime = new CountDownTimer(400,0) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap,GetDateSaveIMG());
            }
        };
        courTime.start();
    }
    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap,String time) {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "/HoHo/Images/");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            File imagePath = new File(Environment.getExternalStorageDirectory() + "/HoHo/Images/"+"screenshot_"+time+".png");
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imagePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                Toast toast =  Toast.makeText(getApplicationContext(), "Đã lưu ảnh !!!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

            } catch (FileNotFoundException e) {
                Log.e("GREC", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("GREC", e.getMessage(), e);
            }

        } else {
            Toast toast =  Toast.makeText(getApplicationContext(), "Chưa lưu được ảnh !!!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void onClickEditTextView(){
        edt_inputmessg.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if(edt_inputmessg.getText().toString().equals("")==false){
                    edt_inputmessg.requestFocus();
                    btnsendmessg.setBackgroundResource(R.drawable.iconsend32);
                }
                else{
                    btnsendmessg.setBackgroundResource(R.drawable.congxan);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

    }
    private  void getCussor() {
        listMessages.clear();
        getArrayId.clear();
        Cursor cursor = RoomChatActivity.database.GetData("SELECT * FROM SMSTWO");
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String tengui = cursor.getString(1);
            String tennhan = cursor.getString(2);
            String noidung = cursor.getString(3);
            String date = cursor.getString(4);
            String hinh = cursor.getString(5);

            if(tengui.equals(usernamenhan)&& tennhan.equals(usernamegui)){
                if (tengui.equals(usernamegui)== false){
                    Usermessager m = new Usermessager(tengui, noidung, date, hinh,false);
                    listMessages.add(m);
                    getArrayId.add(id);
                }
            }
            if(tengui.equals(usernamegui)&& tennhan.equals(usernamenhan)){
                Usermessager m = new Usermessager(tengui, noidung, date, hinh,true);
                listMessages.add(m);
                getArrayId.add(id);

            }
        }
        adapter.notifyDataSetChanged();
        lv_noidungchat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

    }
    private String GetDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dindanggio =new SimpleDateFormat("hh:mm");//HH:mm:ss a
        String date = " "+ dindanggio.format(calendar.getTime()) +" " + simpleDateFormat.format(calendar.getTime()) ;
        return date;
    }
    private String GetDateSaveIMG() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatngay = new SimpleDateFormat("dd");
        SimpleDateFormat simpleDateFormatthang = new SimpleDateFormat("MM");
        SimpleDateFormat dindanggio =new SimpleDateFormat("hh");//HH:mm:ss a
        SimpleDateFormat dindangphut =new SimpleDateFormat("mm");
        SimpleDateFormat dindanggiay =new SimpleDateFormat("ss");
        String date = simpleDateFormatngay.format(calendar.getTime())+"_"+simpleDateFormatthang.format(calendar.getTime())+"_"
                +dindanggio.format(calendar.getTime())+"_"+dindangphut.format(calendar.getTime())+"_"+dindanggiay.format(calendar.getTime());
        return date;
    }
    private Emitter.Listener onConectTwoClient = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            JSONObject object = (JSONObject) args[0];
            try {
                Boolean conect = object.optBoolean("ketnoi");
                if (conect == true){
                    txtnameclient.setText(usernamenhan);
                }else {
                    txtnameclient.setText("Ket noi that bai " + usernamenhan);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener onstatusTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String username = object.optString("username");
                        Toast.makeText(getApplicationContext(),username + " typing",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private Emitter.Listener onstatusStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String username = object.optString("username");
                        Toast.makeText(getApplicationContext(),username+" stop typing",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        if (myDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            myDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            finish();
        }

    }
    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    private void Anhxa() {
        btnsendmessg = (Button)findViewById(R.id.btn_sendmessg);
        edt_inputmessg = (EditText)findViewById(R.id.edt_inputmessg);
        lv_noidungchat = (ListView)findViewById(R.id.listviewChatmessg);
        layooutReqairMessg = (LinearLayout)findViewById(R.id.layooutReqairMessg);
        myToolbar = (Toolbar)findViewById(R.id.mytoolbar);
        myDrawerLayout = (DrawerLayout)findViewById(R.id.mydrawerLayout);
        myNavigationView = (NavigationView)findViewById(R.id.myNavigationView);
        linearLayout_twochat = (LinearLayout)findViewById(R.id.linear_twochat);
    }
    // goi file menu xml ra giao dien
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info_user,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
