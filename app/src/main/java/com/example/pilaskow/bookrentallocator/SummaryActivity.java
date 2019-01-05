package com.example.pilaskow.bookrentallocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pilaskow.bookrentallocator.model.Book;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Button backToMenuBt = (Button) findViewById(R.id.backToMenuButton);
        backToMenuBt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
            }
        });
        if (getIntent().hasExtra("basket")) {
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null)
                showSummary((ArrayList<Book>) bundle.getSerializable("basket"));
            else
                Log.e("null","null");
        }
    }

    public void showSummary(ArrayList basket){
        TextView summaryTextView = (TextView) findViewById(R.id.summaryTextView);
        String info = "Wypożyczono:\n\n";
        for(int i=0;i<basket.size();i++){
            Book book = (Book) basket.get(i);
            info += "- "+book.getTitle()+" ("+book.getiSBN()+")\n";
        }
        summaryTextView.setText(info);
    }
}
