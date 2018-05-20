package com.media.tf.chatsocketio.UpdateAccount;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.media.tf.chatsocketio.R;

public class UpdateAccountActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText edt_lkface, edt_lkgmail, edt_lktwiter, edt_lkphone, edt_lkadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Cập nhập Thông tin");
        setContentView(R.layout.activity_update_account);
        Anhxa();
        sharedPreferences = getSharedPreferences("INFOUSER",MODE_PRIVATE);
        String FACE = sharedPreferences.getString("FACE","");
        String GMAIL = sharedPreferences.getString("GMAIL","");
        String TWITER = sharedPreferences.getString("TWITER","");
        String PHONE = sharedPreferences.getString("PHONE","");
        String ADD = sharedPreferences.getString("ADD","");

        if (FACE.length() != 0 || GMAIL.length()!= 0 || TWITER.length() != 0 || PHONE.length()!= 0 ||ADD.length() != 0){
            edt_lkface.setText(FACE);
            edt_lkgmail.setText(GMAIL);
            edt_lktwiter.setText(TWITER);
            edt_lkphone.setText(PHONE);
            edt_lkadd.setText(ADD);
        }

    }
    private void Anhxa() {
        edt_lkadd = (EditText)findViewById(R.id.edt_lkadd);
        edt_lkface = (EditText)findViewById(R.id.edt_lkface);
        edt_lkgmail = (EditText)findViewById(R.id.edt_lkgmail);
        edt_lkphone = (EditText)findViewById(R.id.edt_lkphone);
        edt_lktwiter = (EditText)findViewById(R.id.edt_lktwiter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("FACE",edt_lkface.getText().toString());
            editor.putString("GMAIL",edt_lkgmail.getText().toString());
            editor.putString("TWITER",edt_lktwiter.getText().toString());
            editor.putString("PHONE",edt_lkphone.getText().toString());
            editor.putString("ADD",edt_lkadd.getText().toString());
            editor.commit();
            Toast.makeText(getApplicationContext(),"Đã Cập nhật !!!",Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FACE",edt_lkface.getText().toString());
        editor.putString("GMAIL",edt_lkgmail.getText().toString());
        editor.putString("TWITER",edt_lktwiter.getText().toString());
        editor.putString("PHONE",edt_lkphone.getText().toString());
        editor.putString("ADD",edt_lkadd.getText().toString());
        editor.commit();
        Toast.makeText(getApplicationContext(),"Đã Cập nhật !!!",Toast.LENGTH_SHORT).show();
        finish();
        super.onBackPressed();
    }
}
