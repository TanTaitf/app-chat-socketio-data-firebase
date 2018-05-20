package com.media.tf.chatsocketio.ReadNews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.media.tf.chatsocketio.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.media.tf.chatsocketio.Hellper.Hellper.url_Read;

public class ListNewsActivity extends AppCompatActivity {

    ListView listView;
    Customadapter customadapter;
    ArrayList<Docbao> mangdocbao;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tin Tức");
        setContentView(R.layout.activity_listnews);
        listView = (ListView)findViewById(R.id.listViewnews);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            progress = ProgressDialog.show(this,"", "Loading...", true);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mangdocbao = new ArrayList<Docbao>();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ReadRss().execute(url_Read);
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(),WebViewNewsActivity.class);
                    intent.putExtra("link",mangdocbao.get(i).link);
                    startActivity(intent);
                }
            });
        }
        else {
            showDialog();
        }
    }
    class ReadRss extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... Params) {

            return docNoiDung_Tu_URL(Params[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            if(document != null){
                NodeList nodeList = document.getElementsByTagName("item");
                String tiltle = "";
                String link = "";
                NodeList nodeListdescription = document.getElementsByTagName("description");
                String hinhanh = "";
                for (int i = 0; i < nodeList.getLength(); i++) {

                    String cdata = nodeListdescription.item(i + 1).getTextContent();
                    Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                    Matcher matcher = p.matcher(cdata);
                    if (matcher.find()) {
                        hinhanh = matcher.group(1);
                    }
                    Element element = (Element) nodeList.item(i);
                    tiltle = parser.getValue(element, "title").toString();
                    link = parser.getValue(element, "link");
                    mangdocbao.add(new Docbao(tiltle, link, hinhanh));

                }
                customadapter = new Customadapter(ListNewsActivity.this, android.R.layout.simple_list_item_1, mangdocbao);
                listView.setAdapter(customadapter);
            }
            else {
                showDialog();
            }

            super.onPostExecute(s);
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
    private  static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();
        try
        {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("DDD",e.toString());
        }
        return content.toString();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
