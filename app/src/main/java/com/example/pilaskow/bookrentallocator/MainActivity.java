package com.example.pilaskow.bookrentallocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.pilaskow.bookrentallocator.model.User;

public class MainActivity extends AppCompatActivity {
// tu menusy do reszty aktywnosci
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;
        Button buttonRent = findViewById(R.id.rentActivityAccess);

        // dopisz mi tutaj, lub gdzieś indziej ajdika

        buttonRent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), RentActivity.class);
                startIntent.putExtra("userId", User.getInstance().getId());
                startActivity(startIntent);
            }
        });

        Button searchingBt = findViewById(R.id.searchingActivButton);
        searchingBt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent searIntent = new Intent(getApplicationContext(), SearchingActivity.class);
                startActivity(searIntent);
            }
        });


    }

}
