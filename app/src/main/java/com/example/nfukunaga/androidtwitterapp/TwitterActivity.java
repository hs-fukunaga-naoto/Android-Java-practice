package com.example.nfukunaga.androidtwitterapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

//Twitterからタイムライン情報を取ってくるクラス
public class TwitterActivity extends Activity {
    String accessToken;
    String accessTokenSecret;
    AsyncTweetGet asyncTweetGet;
    ListView tweetList;
    ProgressBar loadingCircle;
    Context context;
    int displayNumber=50;
    private FragmentManager flagmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        accessToken = getIntent().getStringExtra("accessToken");
        accessTokenSecret = getIntent().getStringExtra("accessTokenSecret");
        tweetList = findViewById(R.id.sample_listView);
        loadingCircle = findViewById(R.id.product_image_loading);
        context = getApplicationContext();
        tweetGet();
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweetGet();
            }
        });
    }

    public void tweetGet(){
        asyncTweetGet = new AsyncTweetGet(loadingCircle, tweetList, context, displayNumber);
        asyncTweetGet.execute(accessToken, accessTokenSecret);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //main.xmlの内容を読み込む
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.d_n_c:
                changeActivity(DisplayNumberChangeActivity.class);
                return true;
            case R.id.logout:
                logoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeActivity(Class cl) {
        Intent intent = new Intent(getApplication(), cl);
        startActivityForResult(intent, RESULT_CANCELED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Integer selectedDisplayNumber = intent.getIntExtra("displayNumber", 50);
        Log.d("selected", selectedDisplayNumber.toString());

        displayNumber=selectedDisplayNumber;
        asyncTweetGet = new AsyncTweetGet(loadingCircle, tweetList, context, displayNumber);
        asyncTweetGet.execute(accessToken, accessTokenSecret);
    }

    public void logoutDialog(){
        AlertDialog.Builder logoutDialog=new AlertDialog.Builder(this);

        logoutDialog.setTitle("ログアウト確認");
        logoutDialog.setMessage("ログアウトしますか？");
        logoutDialog.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //いいえの時の処理
            }
        });
        logoutDialog.setNegativeButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //はいの時の処理
                SharedPreferences accessTokenShare = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                accessTokenShare.edit().remove("accessToken").commit();
                accessTokenShare.edit().remove("accessTokenSecret").commit();
                Intent intent= new Intent(getApplicationContext(),SplashActivity.class);
                startActivity(intent);
                return;
            }
        });
        logoutDialog.setCancelable(true);
        AlertDialog alertDialog=logoutDialog.create();
        alertDialog.show();

    }
}
