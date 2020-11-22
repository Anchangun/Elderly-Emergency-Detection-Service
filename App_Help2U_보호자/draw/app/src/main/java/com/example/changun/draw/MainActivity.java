package com.example.changun.draw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ... 코드 계속
        Button applyButton = (Button) findViewById(R.id.button) ;
        applyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText valueEditText = (EditText) findViewById(R.id.edit) ;

                try {
                    // 문자열을 숫자로 변환.
                    int value = Integer.parseInt(valueEditText.getText().toString());
                    if (value < 0 || value > 100) {
                        Toast toast = Toast.makeText(MainActivity.this, "0 to 100 can be input.",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        // 변환된 값을 프로그레스바에 적용.
                        ProgressBar progress = (ProgressBar) findViewById(R.id.progress) ;
                        progress.setProgress(value) ;
                    }
                } catch (Exception e) {
                    // 토스트(Toast) 메시지 표시.
                    Toast toast = Toast.makeText(MainActivity.this, "Invalid number format",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }) ;
    }
}

