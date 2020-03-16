package com.example.forground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText editTextInput;
    String input;
    double aData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput =findViewById(R.id.edit_text_input);

            input=editTextInput.getText().toString();
            Intent intent = new Intent(this,ExampleService.class);
            // intent.putExtra("InputExtra",input);
            startService(intent);

    }

    public void stopService(View v){
        Intent intent = new Intent(this,ExampleService.class);
        stopService(intent);
    }



}
