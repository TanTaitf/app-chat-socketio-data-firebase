package com.media.tf.chatsocketio.ReadNews;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.media.tf.chatsocketio.R;

public class WebViewNewsActivity extends AppCompatActivity {

    WebView webView;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Chi Tiết");
        setContentView(R.layout.activity_webviewnews);
        Intent intent = getIntent();
        String duonglink = intent.getStringExtra("link");

        progress = new ProgressDialog(WebViewNewsActivity.this);
        progress.setMessage("Vui lòng chờ...");
        progress.setIndeterminate(false);
        progress.setCancelable(true);


        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progress.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progress.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                progress.dismiss();
                showDialog();
                super.onReceivedError(view, request, error);
            }
        });
        webView.loadUrl(duonglink);
    }
    private  void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Lỗi mạng");
        builder.setMessage("Vui lòng tải lại trang");
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            if (webView.canGoBack()== true){
                webView.goBack();
            }else {
                webView.clearCache(true);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()== true){
            webView.goBack();
        }else {
            webView.clearCache(true);
            finish();
        }
    }
}
