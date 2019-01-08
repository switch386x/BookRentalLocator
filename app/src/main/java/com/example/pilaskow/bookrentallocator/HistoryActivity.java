package com.example.pilaskow.bookrentallocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilaskow.bookrentallocator.model.Rental;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseReference mBooksRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bookrentallocator.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final TextView historyContentTV = (TextView) findViewById(R.id.userHistoryContentTextView);
        Intent mainActivityIntent = getIntent();
        final String userId = mainActivityIntent.getStringExtra("userId");
        mBooksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DataSnapshot userHistory = dataSnapshot.child("rental").child(userId);
                    Iterable<DataSnapshot> history = userHistory.getChildren();
                    String text1=""; String text2="";
                    for (DataSnapshot rental : history) {
                        Rental r = rental.getValue(Rental.class);
                        if (!r.getDateOfReturn().equals("")) {
                            text1 += dataSnapshot.child("books").child(r.getiSBNNumber()).child("title").getValue(String.class) + " (" + r.getiSBNNumber() + ")\n\tWypożyczono: " + r.getDateOfRental() + "\n\t" + "Zwrócono: " + r.getDateOfReturn() + "\n\n";
                        }
                        else{
                            text2 += dataSnapshot.child("books").child(r.getiSBNNumber()).child("title").getValue(String.class) + " (" + r.getiSBNNumber() + ")\n\tWypożyczono: " + r.getDateOfRental() + "\n";
                        }
                    }
                    if(text1=="" && text2=="") historyContentTV.setText(getString(R.string.noHistory));
                    else if (text2=="") historyContentTV.setText(text1);
                    else historyContentTV.setText(getString(R.string.dontForget)+text2+"\n\n"+text1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this, getString(R.string.database_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}