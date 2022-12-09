package com.example.qrcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText editTextData;
    private Button buttonGenerate;
    private Button buttonScan;
    private TextView textViewQRResult;
    private ImageView imageViewQRResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                String data = editTextData.getText().toString().trim();
                if (data.isEmpty()){
                    Toast.makeText(MainActivity.this,
                            "Kötelező mező", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(
                                data, BarcodeFormat.QR_CODE, 250, 250
                        );
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageViewQRResult.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator =
                        new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setPrompt("QR Code scanning by me");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, requestCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this,"Kiléptél a QR code scanner-ből", Toast.LENGTH_SHORT).show();
            } else {
                textViewQRResult.setText(result.getContents());

                
                //ha URL van a QR codeban
                Uri uri = Uri.parse(result.getContents());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void init(){
        editTextData = findViewById(R.id.editTextQRdata);
        buttonGenerate = findViewById(R.id.buttonGenerate);
        buttonScan = findViewById(R.id.buttonScan);
        textViewQRResult = findViewById(R.id.textViewQRResult);
        imageViewQRResult = findViewById(R.id.imageViewQRResult);
    }
}