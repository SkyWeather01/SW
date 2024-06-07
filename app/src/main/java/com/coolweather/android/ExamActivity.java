package com.coolweather.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.coolweather.android.databinding.ActivityExamBinding;

public class ExamActivity extends AppCompatActivity {

    ActivityExamBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.but1.setOnClickListener(v->{
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        });
        binding.but2.setOnClickListener(v->{
            Intent intent=new Intent(this,Activity2.class);
            startActivity(intent);
        });
        binding.but4.setOnClickListener(v->{
            Intent intent=new Intent(this,Activity4.class);
            startActivity(intent);
        });
//        setContentView(R.layout.activity_exam);

    }
}