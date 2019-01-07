package com.example.pilaskow.bookrentallocator;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pilaskow.bookrentallocator.model.Book;
import com.example.pilaskow.bookrentallocator.model.Rental;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RentActivity extends AppCompatActivity {
    private DatabaseReference mBooksRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bookrentallocator.firebaseio.com/");
    private ArrayList basket = new ArrayList();
    private DatabaseReference booksDR = mBooksRef.child("books");
    private Book bookToBasket = new Book();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        Intent mainActivityIntent = getIntent();
        userId = mainActivityIntent.getStringExtra("userId");
    }

    public void search(View view){
        final Button rentBt = (Button) findViewById(R.id.rentButton);
        final TextView informationTV = (TextView) findViewById(R.id.detailsTextView);
        final EditText iSBNEdTxt = (EditText) findViewById(R.id.iSBNEditText);
        String numberISBN = iSBNEdTxt.getText().toString();
        rentBt.setVisibility(View.INVISIBLE);
        informationTV.setText(getString(R.string.empty_string));
        findBook(numberISBN);
    }

    public void scan(View v){
        final Button rentBt = (Button) findViewById(R.id.rentButton);
        final TextView informationTV = (TextView) findViewById(R.id.detailsTextView);
        rentBt.setVisibility(View.INVISIBLE);
        informationTV.setText("");
        Intent barcodeScannerIntent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(barcodeScannerIntent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            final EditText iSBNEdTxt = (EditText) findViewById(R.id.iSBNEditText);
            String numberISBN = data.getStringExtra("numberISBN");
            iSBNEdTxt.setText(numberISBN);
            findBook(numberISBN);
        }
    }

    public void findBook(final String iSBN){
        DatabaseReference bookDR = booksDR.child(iSBN);
        bookDR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    final Book book = new Book();
                    book.setiSBN(iSBN);
                    book.setTitle(dataSnapshot.child("title").getValue(String.class));
                    book.setYear(String.valueOf(dataSnapshot.child("year").getValue(long.class)));
                    book.setCondition(dataSnapshot.child("condition").getValue(String.class));
                    DataSnapshot authors = dataSnapshot.child("author");
                    Iterable<DataSnapshot> authorsNames = authors.getChildren();
                    ArrayList authorsNameAL = new ArrayList();
                    for (DataSnapshot author : authorsNames) {
                        authorsNameAL.add(author.getValue(String.class));
                    }
                    book.setAuthor(authorsNameAL);
                    showInfo(book);
                } catch (Exception e) {
                    Toast.makeText(RentActivity.this, getString(R.string.bookcode_notfound), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RentActivity.this, getString(R.string.database_error), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showInfo(Book book) {
        final TextView informationTV = (TextView) findViewById(R.id.detailsTextView);
        if (book.getTitle() != null) {
            String author = "";
            for (int i = 0; i < book.getAuthor().size(); i++) {
                if (i != 0) author += ", ";
                author += book.getAuthor().get(i);
            }
            String condition = book.getCondition();
            if(!condition.equals("dostępna")) condition = "wypożyczona";
            informationTV.setText("Tytuł: "+ book.getTitle() + "\nAutor: " + author + "\nRok wydania: " + book.getYear() + "\n\nKsiążka jest " + condition);  //stringBuilder
            final Button rentBt = (Button) findViewById(R.id.rentButton);
            if(book.getCondition().equals("dostępna")) {
                bookToBasket = book;
                rentBt.setVisibility(View.VISIBLE);
            }
        }
    }


    public void rent(View view){
        DatabaseReference bookDR = booksDR.child(bookToBasket.getiSBN());
        final boolean[] available = new boolean[1];
        bookDR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("condition").getValue(String.class)).equals("dostępna"))
                available[0] = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(available[0] = true){
            basket.add(bookToBasket);
            mBooksRef.child("rental").child(userId).push().setValue(new Rental(bookToBasket.getiSBN()));
            bookDR.child("condition").setValue(userId);
            Toast.makeText(RentActivity.this, getString(R.string.book_rented), Toast.LENGTH_SHORT).show();
            final Button rentBt = (Button) findViewById(R.id.rentButton);
            final Button nextBt = (Button) findViewById(R.id.nextStep);
            rentBt.setVisibility(View.INVISIBLE);
            nextBt.setVisibility(View.VISIBLE);
            bookToBasket = null;
        }
        else{
            Toast.makeText(RentActivity.this, getString(R.string.book_already_rented), Toast.LENGTH_SHORT).show();
        }
    }

    public void nextStep(View view){
        Intent summaryIntent = new Intent(this, SummaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("basket",basket);
        summaryIntent.putExtras(bundle);
        startActivity(summaryIntent);
    }



}
