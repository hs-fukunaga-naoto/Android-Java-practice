package com.example.nfukunaga.androidtwitterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayNumberChangeActivity extends Activity{
    private ArrayList<Integer> displayNumbers;
        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_number_change);
        displayNumbers = new ArrayList<>();
        Integer number = 0;
        for (int i = 0; i < 4; i++) {
            number = number + 50;
            displayNumbers.add(number);
        }
        ListView display_number_list = findViewById(R.id.display_number_change);
        DisplayNumberChangeAdapter displayNumberChangeAdapter = new DisplayNumberChangeAdapter(getApplicationContext(), R.layout.element_display_number_change, displayNumbers);
        display_number_list.setAdapter(displayNumberChangeAdapter);

        display_number_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                int sendDisplayNumber=displayNumbers.get(position);
                intent.putExtra("displayNumber",sendDisplayNumber);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
