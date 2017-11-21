package com.gmail.byron.ullauri.acronymrecall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void menuClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewStudy:
            case R.id.textViewStudy:

                break;
            case R.id.imageViewCreate:
            case R.id.textViewCreate:
                startActivity(new Intent(this, WordsActivity.class));
                break;
            case R.id.imageViewAcronyms:
            case R.id.textViewAcronyms:
                
                break;
            case R.id.imageViewInfo:
            case R.id.textViewInfo:

                break;
            default:
                System.err.println("UNREGISTERED CLICK");
                break;
        }
    }
}
