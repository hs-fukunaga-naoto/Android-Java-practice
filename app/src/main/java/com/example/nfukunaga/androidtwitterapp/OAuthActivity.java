package com.example.nfukunaga.androidtwitterapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
//アプリ起動時はこのメソッドが呼ばれる
public class OAuthActivity extends Activity {
    //TwitterAPIを使用するときのキー（https://developer.twitter.com/から取得）
    public static final String CONSUMER_KEY = "SZzjDZCmRGaAWAitzAGZhKOSu";
    public static final String CONSUMER_SECRET = "ebgwkCAljwWahl8u3sIYCmnai379hfd4OHcxTPtbkpQhigLnv2";

    private static final String CALLBACK_URL = "callback://twitter";
    private RequestToken mReq = null;
    private OAuthAuthorization mOAuth = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences accessTokenShare = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken=accessTokenShare.getString("accessToken",null);
        String accessTokenSecret=accessTokenShare.getString("accessTokenSecret",null);
        if(accessToken!=null&&accessTokenSecret!=null){
            Intent main = new Intent(this, TwitterActivity.class);
            main.putExtra("accessToken", accessToken);
            main.putExtra("accessTokenSecret", accessTokenSecret);
            startActivity(main);
            finish();
            return;
        }else{
            new OAuthTask().execute();
            return;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        TokenTask task = new TokenTask();
        task.execute(intent);
    }

    //@param intent
    //ログイン時に使うトークンを取得する
    private void getAccessToken(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
            String verifier = uri.getQueryParameter("oauth_verifier");
            try {
                AccessToken accessTokenBase = mOAuth.getOAuthAccessToken(mReq, verifier);
                String accessToken = accessTokenBase.getToken();
                String accessTokenSecret = accessTokenBase.getTokenSecret();
                Intent main = new Intent(this, TwitterActivity.class);
                main.putExtra("accessToken", accessToken);
                main.putExtra("accessTokenSecret", accessTokenSecret);
                SharedPreferences accessTokenShare = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                accessTokenShare.edit().putString("accessToken",accessToken).commit();
                accessTokenShare.edit().putString("accessTokenSecret",accessTokenSecret).commit();
                startActivity(main);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void executeOAuth() {
        //Twitter4jの設定読み込み
        Configuration conf = ConfigurationContext.getInstance();

        //OAuthオブジェクト作成
        mOAuth = new OAuthAuthorization(conf);
        mOAuth.setOAuthAccessToken(null);
        //OAuthオブジェクトにキーとシークレットを設定
        mOAuth.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        //アプリの認証オブジェクト作成
        try {
            mReq = mOAuth.getOAuthRequestToken(CALLBACK_URL);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        String uriStr;
        uriStr = mReq.getAuthenticationURL();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr)));
    }

    class OAuthTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            executeOAuth();
            return null;
        }
    }

    class TokenTask extends AsyncTask<Intent, Void, Void> {

        @Override
        protected Void doInBackground(Intent... params) {
            getAccessToken(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            finish();
        }
    }
}


