package com.example.pilaskow.bookrentallocator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilaskow.bookrentallocator.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final TextView loginText = findViewById(R.id.loginTitle);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                login(username.getText().toString(),password.getText().toString());
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            User user = User.getInstance();
            user.setId(currentUser.getProviderId());
            user.setUserName(currentUser.getDisplayName());
        }
        else
            login("test@test.test", "testtest");
    }

    private void showRulesDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.regulamin);
        dialog.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });
        dialog.setNegativeButton(R.string.refuse, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            User mUser = User.getInstance();
                            if (user != null) {
                                String path = "users/" + user.getProviderId();
                                DatabaseReference ref = database.getReference(path);
                                //Log.d("test", ref.getKey());
                                mUser.setId(user.getUid());
                                showRulesDialog();
//                                mUser.setUserName(user.getDisplayName());
                            }
                        } else {
                            Log.d("test", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
