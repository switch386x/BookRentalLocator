package com.example.pilaskow.bookrentallocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.pilaskow.bookrentallocator.model.Book;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView resultTV = (TextView) findViewById(R.id.resultTextView);
        ArrayList foundBooks;
        String results = "";
        if (getIntent().hasExtra("books")) {
            Bundle bundle = getIntent().getExtras();
                results += bundle.getString("parameters") + "\n\n\n";
                foundBooks = (ArrayList<Book>) bundle.getSerializable("books");
                if(foundBooks.size()==0) results +="Nie znaleziono książek o podanych parametrach";
                    for (int i = 0; i < foundBooks.size(); i++) {
                        Book book = (Book) foundBooks.get(i);
                        String conditional;
                        if(book.getCondition().equals("dostępna")) conditional = "Książka jest dostępna";
                        else conditional = "Książka jest wypożyczona";
                        String authorsNames = "";
                        for(int j = 0; j< book.getAuthor().size();j++){
                            if(j!=0) authorsNames += ", ";
                            authorsNames += (String) book.getAuthor().get(j);
                        }
                        results += book.getTitle() + " (" + book.getiSBN() + ")\n\t"+ authorsNames + "\t" + book.getPublishingHouse() + "\n\t" + book.getYear() + "\n\t"+conditional+"\n\n";
                    }


        }
        resultTV.setText(results);
    }
}
