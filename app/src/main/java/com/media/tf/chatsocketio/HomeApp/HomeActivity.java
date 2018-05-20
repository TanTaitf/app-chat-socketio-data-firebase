package com.media.tf.chatsocketio.HomeApp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.media.tf.chatsocketio.R;
import com.media.tf.chatsocketio.TabLayout_Content.TabListUserActivity;
import com.media.tf.chatsocketio.Implements_Class.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.media.tf.chatsocketio.Hellper.Hellper.url_Socket;


public class HomeActivity extends AppCompatActivity {

    Button   btnchatnow;
    TextView txtversion;
    ProgressDialog progress;
    // dialog
    private TextView titlehanhdong;
    private ImageView img_user;
    private Button btn_chonhinh, btn_chuphinh;
    private EditText thongtin;
    private Button dongy, huy;
    private Dialog dialog;
    // firebase
    private DatabaseReference mdatabase;
    private StorageReference mStorageRef;
    // socket
    public static Socket msocket;
    // sharedPreferences
    SharedPreferences sharedPreferences;
    String name = "";
    String anh = "";
    public static String linkanh = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        Anhxa();
        progress = new ProgressDialog(HomeActivity.this);
        progress.setMessage("Đang đăng ký...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            sharedPreferences = getSharedPreferences("name",MODE_PRIVATE);
            Boolean checknumber = sharedPreferences.getBoolean("name",false);
            name = sharedPreferences.getString("tenuser","");
            anh = sharedPreferences.getString("linkanhusergui","");
            if(checknumber == true) {
                startActivity(new Intent(HomeActivity.this,TabListUserActivity.class).putExtra("usernamegui",name).putExtra("linkanh",anh));
            }
            try {
                mStorageRef = FirebaseStorage.getInstance().getReference();
                mdatabase = FirebaseDatabase.getInstance().getReference();
                try {
                    msocket = IO.socket(url_Socket);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                msocket.connect();
                msocket.on("server-result-register",onRegister);
                btnchatnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialoglistviewSelectIntem();

                    }
                });

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
                finish();
                return;
            }
        });
        builder.show();
    }
    private  void showDialogRes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Lỗi đăng ký");
        builder.setMessage("Vui lòng kiểm tra lại");
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
    private void updateImg_User(final String ten){

        long time = System.currentTimeMillis();
        StorageReference mountainsRef = mStorageRef.child("image"+time+".png");
        BitmapDrawable bitmapDrawable = (BitmapDrawable)img_user.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmap = getResizedBitmap(bitmap,100,100);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progress.dismiss();
                showDialogRes();
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(),"Upload error",Toast.LENGTH_SHORT).show();
                Log.d("AAA",exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("url","URL download"+downloadUrl.toString());
                // save data user gồm tên và link img
                update_DataUser(ten, downloadUrl.toString());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 456 && resultCode ==RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_user.setImageBitmap(getCircularBitmap(bitmap));
        }
        if (requestCode == 123 && resultCode ==RESULT_OK && data != null)
        {
            Uri uri = data.getData(); // tạo đường dẫn của file
            // cách 1
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri); // lấy đường dẫn bằng inputstream.
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream) ;
                img_user.setImageBitmap(getCircularBitmap(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    public Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        return output;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void Anhxa() {
        btnchatnow = (Button)findViewById(R.id.buttonchatnow);
        txtversion = (TextView)findViewById(R.id.textViewversion);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    private  void DialoglistviewSelectIntem() {
        dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        AnhxaDialog(dialog);
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,123);
            }
        });
        btn_chuphinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,456);
            }
        });
        btn_chonhinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,123);
            }
        });

        dongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = thongtin.getText().toString().trim();
                // ẩn bàn phím
                InputMethodManager ixnm = (InputMethodManager) getSystemService (Context. INPUT_METHOD_SERVICE);
                ixnm.hideSoftInputFromWindow (thongtin.getWindowToken (), 0);
                if (!input.isEmpty()) {
                    progress.show();
                    msocket.emit("client-register", input);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                }
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void update_DataUser(final String ten, final String linkimg){
        // tạo node trên database
        // lưu dữ liệu user
        User user = new User(ten, linkimg);
        linkanh = linkimg;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("linkanhusergui",linkanh);
        editor.putBoolean("name",true);
        editor.putString("tenuser",ten);
        editor.commit();
        mdatabase.child("user").push().setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null)
                {
                    progress.dismiss();
                    startActivity(new Intent(HomeActivity.this,TabListUserActivity.class).putExtra("usernamegui",ten).putExtra("linkanh",linkanh));
                    Toast toast =  Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{
                    showDialogRes();
                    Log.d("EEE",databaseError.toString());
                }
            }
        });

    }

    private   void AnhxaDialog(Dialog dialog) {
        // anh xa dialog
        titlehanhdong = (TextView)dialog.findViewById(R.id.titlehanhdong);
        thongtin = (EditText) dialog.findViewById(R.id.edt_nameuser);
        dongy = (Button)dialog.findViewById(R.id.dongy);
        huy = (Button)dialog.findViewById(R.id.huy);
        btn_chonhinh = (Button)dialog.findViewById(R.id.btn_chonhinh);
        btn_chuphinh = (Button)dialog.findViewById(R.id.btn_chuphinh);
        img_user = (ImageView)dialog.findViewById(R.id.imageView_profile);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_dowload);
        img_user.setImageBitmap(getCircleBitmap(bm));
    }
    private Emitter.Listener onRegister = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject object = (JSONObject) args[0];
                    try {
                        boolean exist = object.getBoolean("ketqua");
                        if (exist == true) {
                            String input = thongtin.getText().toString();
                            updateImg_User(input);

                        }else {
                            progress.dismiss();
                            Toast toast =  Toast.makeText(getApplicationContext(), "User đã tồn tại !!!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            showDialogRes();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };

}
