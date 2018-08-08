package com.example.nfukunaga.androidtwitterapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import twitter4j.ExtendedMediaEntity;
import twitter4j.MediaEntity;

//画面表示用のアダプタークラス
public class ListAdapter extends ArrayAdapter<Status> {

    private int mResource;
    private List<Status> mItems;
    private LayoutInflater mInflater;
    private Context context;

    /**
     * コンストラクタ
     *
     * @param context  コンテキスト
     * @param resource リソースID
     * @param items    リストビューの要素
     */
    public ListAdapter(Context context, int resource, List<Status> items) {
        super(context, resource, items);
        this.context = context;
        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(mResource, null);
        }

        // リストビューに表示する要素を取得
        Status item = mItems.get(position);

        //リツイートしたユーザを設定
        TextView rtUserInfo=view.findViewById(R.id.RTuser);
        if(item.getRetweetUserName()!=null){
            rtUserInfo.setHeight(40);
            rtUserInfo.setText(item.getRetweetUserName());
        }else{
            rtUserInfo.setHeight(0);
            rtUserInfo.setText("");
        }

        // ユーザ名を設定
        TextView userInfo = view.findViewById(R.id.userInfo);
        userInfo.setText(item.getUserNameAndId());

        // 内容を設定
        TextView content = view.findViewById(R.id.content);
        content.setText(item.getText());

        // 日付を設定
        TextView date = view.findViewById(R.id.date);
        Date tweetTime = item.getCreatedAt();
        String timeStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(tweetTime);
        date.setText(timeStr);

        //画像の表示
        MediaEntity[] mediaEntities = item.getMediaEntities();
        ImageView[] imageView = new ImageView[4];
        imageView[0] = view.findViewById(R.id.imageView1);
        imageView[1] = view.findViewById(R.id.imageView2);
        imageView[2] = view.findViewById(R.id.imageView3);
        imageView[3] = view.findViewById(R.id.imageView4);
        for (int i = 0; i < imageView.length; i++) {
            imageView[i].setImageDrawable(null);
        }
        //表示するための画像情報を渡す
        setImage(mediaEntities, imageView);


        return view;
    }

    //受け取った画像情報から画像表示
    public void setImage(MediaEntity[] mediaEntities, ImageView[] imageView) {
        if (mediaEntities.length == 0) {
            return;
        }
        for (int i = 0; i < mediaEntities.length; i++) {
            Picasso.with(context)
                    .load(mediaEntities[i].getMediaURLHttps())
                    .into(imageView[i]);
        }


    }
}
