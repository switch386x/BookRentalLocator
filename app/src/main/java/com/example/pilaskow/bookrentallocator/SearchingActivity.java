package com.example.pilaskow.bookrentallocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pilaskow.bookrentallocator.model.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchingActivity extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bookrentallocator.firebaseio.com/");
    private DatabaseReference booksDR = mDatabase.child("books");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        final ArrayList<Book> listOfBooks = downBooks();
        Button buttonRent = findViewById(R.id.searchButton);
        buttonRent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText titleEt = (EditText) findViewById(R.id.titleEditText);
                EditText authorEt = (EditText) findViewById(R.id.authorEditText);
                EditText yearEt = (EditText) findViewById(R.id.yearEditText);
                EditText publishingHouseEt = (EditText) findViewById(R.id.pubHouseEditText);
                Bundle bundle = new Bundle();
                ArrayList<Book> foundBooks = new ArrayList();
                String parameter = getString(R.string.search_result);
                String findTitle ="";String findAuthor = ""; String findYear = ""; String findPubHouse="";
                if(!titleEt.getText().toString().equals("")) {
                    findTitle = titleEt.getText().toString();
                    parameter += "\n- Tytuł: " + findTitle;
                }
                if(!authorEt.getText().toString().equals("")){
                    findAuthor = authorEt.getText().toString();
                    parameter += "\n- Autor: " + findAuthor;
                }
                if(!yearEt.getText().toString().equals("")){
                    findYear = yearEt.getText().toString();
                    parameter += "\n- Rok: " + findYear;
                }
                if(!publishingHouseEt.getText().toString().equals("")){
                    findPubHouse = publishingHouseEt.getText().toString();
                    parameter += "\n- Wydawnictwo: " + findPubHouse;
                }
                for(int i=0;i<listOfBooks.size();i++){
                    Book book = (Book) listOfBooks.get(i);
                    if(book.getTitle().toLowerCase().equals(findTitle.toLowerCase())) foundBooks.add(book);
                    else if(book.getYear().toLowerCase().equals(findYear.toLowerCase())) foundBooks.add(book);
                    else if(book.getPublishingHouse().toLowerCase().equals(findPubHouse.toLowerCase())) foundBooks.add(book);
                    else{
                        ArrayList authors = (ArrayList) book.getAuthor();
                        for(int j=0;j<authors.size();j++){
                            String author = (String) authors.get(j);
                            if(author.toLowerCase().equals(findAuthor.toLowerCase())) foundBooks.add(book);
                        }
                    }
                }
                if(!findAuthor.equals("")||!findPubHouse.equals("")||!findTitle.equals("")||!findYear.equals("")){
                    Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
                    bundle.putSerializable("books",foundBooks);
                    bundle.putString("parameters", parameter);
                    resultIntent.putExtras(bundle);
                    startActivity(resultIntent);
                }
                else Toast.makeText(SearchingActivity.this, "Wprowadź dane", Toast.LENGTH_LONG).show();

            }
        });
    }

    public ArrayList downBooks(){
        final ArrayList listOfBooks = new ArrayList<Book>();
        booksDR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> books = dataSnapshot.getChildren();
                for (DataSnapshot book : books) {
                    Iterable<DataSnapshot> authorsNames = book.child("author").getChildren();
                    ArrayList authorsNameAL = new ArrayList();
                    for (DataSnapshot author : authorsNames) {
                        authorsNameAL.add(author.getValue(String.class));
                    }
                    listOfBooks.add(new Book(book.getKey(), book.child("title").getValue(String.class), authorsNameAL, String.valueOf(book.child("year").getValue(Long.class)),book.child("publishingHouse").getValue(String.class),book.child("condition").getValue(String.class) ));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listOfBooks;
    }
}
