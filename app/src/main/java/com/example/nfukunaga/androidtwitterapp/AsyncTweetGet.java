package com.example.nfukunaga.androidtwitterapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Iterator;


import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class AsyncTweetGet extends AsyncTask<String, Void, ArrayList<Status>> {
    private ListView tweetList;
    private Context context;
    private ProgressBar loadingCircle;
    private int displayNumber;
    private ListAdapter adapter;
    private ArrayList<com.example.nfukunaga.androidtwitterapp.Status> listItems = new ArrayList<>();

    //コンテキストとビューを受け取る
    public AsyncTweetGet(ProgressBar loadingCircle, ListView tweetList, Context context, int displayNumber) {
        this.context = context;
        this.tweetList = tweetList;
        this.loadingCircle = loadingCircle;
        this.displayNumber = displayNumber;
    }

    //非同期処理開始前の処理
    @Override
    protected void onPreExecute() {
        //画面のリセット
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        //プログレスバーの表示
        loadingCircle.setVisibility(View.VISIBLE);
    }

    //非同期処理
    @Override
    protected ArrayList<com.example.nfukunaga.androidtwitterapp.Status> doInBackground(String... params) {
        //認証しているトークンとキーからログイン
        String accessToken = params[0];
        String accessTokenSecret = params[1];
        ConfigurationBuilder cbuilder = new ConfigurationBuilder();
        cbuilder.setOAuthConsumerKey(OAuthActivity.CONSUMER_KEY);
        cbuilder.setOAuthConsumerSecret(OAuthActivity.CONSUMER_SECRET);
        cbuilder.setOAuthAccessToken(accessToken);
        cbuilder.setOAuthAccessTokenSecret(accessTokenSecret);
        cbuilder.setRestBaseURL("https://api.twitter.com/1.1/");
        cbuilder.setTweetModeExtended(true);
        Configuration conf = cbuilder.build();
        TwitterFactory twitterFactory = new TwitterFactory(conf);
        Twitter mTwitter = twitterFactory.getInstance();
        ResponseList<twitter4j.Status> statuses;
        try {
            //取得件数の設定
            Paging paging = new Paging();
            paging.setCount(displayNumber);
            //取得したタイムライン情報を格納するためのリスト
            ArrayList<com.example.nfukunaga.androidtwitterapp.Status> timelineList = new ArrayList<>();
            Log.d("msg","getHomeTimelineが動くよ");
            statuses = mTwitter.getHomeTimeline(paging);
            for (twitter4j.Status status : statuses) {
                com.example.nfukunaga.androidtwitterapp.Status s = new com.example.nfukunaga.androidtwitterapp.Status();
                if (status.isRetweet()) {
                    twitter4j.Status rtStatus=status.getRetweetedStatus();
                    s.setUserNameAndId(rtStatus.getUser().getName() + "@" + rtStatus.getUser().getScreenName());
                    s.setText(rtStatus.getText());
                    s.setCreatedAt(rtStatus.getCreatedAt());
                    s.setMediaEntities(rtStatus.getMediaEntities());
                    s.setRetweetUserName(status.getUser().getName() + "さんがリツイート");
                    timelineList.add(s);
                } else {
                    //取ってきた情報をentityとして格納
                    s.setUserNameAndId(status.getUser().getName() + "@" + status.getUser().getScreenName());
                    s.setText(status.getText());
                    s.setCreatedAt(status.getCreatedAt());
                    s.setMediaEntities(status.getMediaEntities());
                    s.setRetweetUserName(null);
                    timelineList.add(s);
                }
            }
            return timelineList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //非同期処理終了時の処理
    @Override
    protected void onPostExecute(ArrayList<com.example.nfukunaga.androidtwitterapp.Status> timelineList) {
        if (timelineList != null) {
            //取得した情報をアダプターに受けわたす
            for (Iterator<com.example.nfukunaga.androidtwitterapp.Status> iterator = timelineList.iterator(); iterator.hasNext(); ) {
                com.example.nfukunaga.androidtwitterapp.Status status = iterator.next();
                com.example.nfukunaga.androidtwitterapp.Status item = new com.example.nfukunaga.androidtwitterapp.Status(status.getText(), status.getMediaEntities(), status.getCreatedAt(), status.getUserNameAndId(),status.getRetweetUserName());
                listItems.add(item);
            }
            adapter = new ListAdapter(context, R.layout.sample, listItems);
            tweetList.setAdapter(adapter);
        }
        loadingCircle.setVisibility(View.GONE);
        cancel(true);
    }


}
