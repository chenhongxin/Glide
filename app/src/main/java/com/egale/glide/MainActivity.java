package com.egale.glide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView1 = findViewById(R.id.iv_image1);
        ImageView imageView2 = findViewById(R.id.iv_image2);
        ImageView imageView3 = findViewById(R.id.iv_image3);

        Glide.with(this).load("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3394263821,1263517319&fm=26&gp=0.jpg").loadding(R.mipmap.ic_launcher).into(imageView1);
        Glide.with(this).load("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=473619872,1560375257&fm=11&gp=0.jpg").loadding(R.mipmap.ic_launcher).into(imageView2);
        Glide.with(this).load("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3327248454,849738544&fm=26&gp=0.jpg").loadding(R.mipmap.ic_launcher).into(imageView3);

    }
}
