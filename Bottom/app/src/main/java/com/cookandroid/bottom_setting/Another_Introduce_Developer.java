package com.cookandroid.bottom_setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Another_Introduce_Developer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another_introduce_developer);

        //Intent 받아오기
        Intent intent_introduce_developer = new Intent(this.getIntent());
    }
}
