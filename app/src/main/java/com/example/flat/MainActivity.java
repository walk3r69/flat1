package com.example.flat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: MainActivity started");

        // Botão para abrir a tela GNSS View
        Button gnssButton = findViewById(R.id.btn_gnss);
        gnssButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre a GNSSActivity
                Intent intent = new Intent(MainActivity.this, GNSSActivity.class);
                startActivity(intent);
            }
        });
    }
}
