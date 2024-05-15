package com.google.mlkit.vision.demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.mlkit.vision.demo.R;

public class ShowResultsActivity extends AppCompatActivity {
    private TextView title, text;
    Button homeButton, retryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);
        text = findViewById(R.id.resultText);
        title = findViewById(R.id.resultTitle);
        homeButton = findViewById(R.id.resultToHomeButton);
        retryButton = findViewById(R.id.resultRetryButton);

        Intent intent = getIntent();
        float classificationResult = Float.parseFloat(intent.getStringExtra("classification"));

        if(classificationResult > 0.6){
            title.setText("Qualcosa è andato storo");
            text.setText("Non è stata riconosciuta la posa oppure non era corretta, riprova");
        }else if(classificationResult > 0.4){
            title.setText("Corretto");
            text.setText("hai eseguito la posa correttamente");
            retryButton.setVisibility(View.INVISIBLE);
        }else {
            title.setText("Perfetta");
            text.setText("Hai eseguito la posa perfettamente");
            retryButton.setVisibility(View.INVISIBLE);
        }

        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChooserActivity.class));
        });

        retryButton.setOnClickListener(v -> {
            finish();
        });
    }

}
