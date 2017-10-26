package com.gmail.byron.ullauri.acronymrecall;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String locale = Locale.getDefault().getLanguage();
        AssetManager assetManager = this.getAssets();

        try {
            InputStream inputStream = assetManager.open(locale + "_words.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            AcronymUtil util = AcronymUtil.INSTANCE;
            util.init(inputStreamReader);
            Thread.sleep(2000);
        } catch (IOException e) {
            System.err.println("Words File Not Found For: " + locale);
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(this, WordsActivity.class);
        startActivity(intent);
        finish();
    }
}
