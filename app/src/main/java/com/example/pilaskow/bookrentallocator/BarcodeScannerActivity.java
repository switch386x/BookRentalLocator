package com.example.pilaskow.bookrentallocator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        scan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onStop(){
        super.onStop();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume(){
        super.onResume();
        scan();
    }

    private void scan(){
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        zXingScannerView.stopCamera();
        zXingScannerView.resumeCameraPreview(this);
        Intent goBack = new Intent(this,RentActivity.class);
        goBack.putExtra("numberISBN", result.toString());
        setResult(Activity.RESULT_OK,goBack);
        finish();
    }

}
