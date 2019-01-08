package com.example.pilaskow.bookrentallocator;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        int i = 0;
        while(i<basket.size()){
            Book book = (Book) basket.get(i);
            info += "- "+book.getTitle()+" ("+book.getiSBN()+")\n";
            i++;
        }
        showNotification(i);
        summaryTextView.setText(info);
    }


    public void showNotification(int numberOfBooks){
        String text;
        if(numberOfBooks==1) text = "Wypożyczono książkę";
        else if(numberOfBooks==2) text = "Wypożyczono 2 książki";
        else text = "Wypożyczono książki";
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent clickNotif = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.book_rental)
                .setContentTitle(getString(R.string.library_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(clickNotif)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
    }
}
