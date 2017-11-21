package com.gmail.byron.ullauri.acronymrecall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        Intent intent;

        if (preferences.getBoolean("isFirstTime", false)) {
            intent = new Intent(this, MenuActivity.class);
        } else {
            preferences.edit().putBoolean("isFirstTime", true).apply();
            intent = new Intent(this, WordsActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
