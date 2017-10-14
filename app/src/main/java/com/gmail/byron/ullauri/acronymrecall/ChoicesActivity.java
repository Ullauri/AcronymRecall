package com.gmail.byron.ullauri.acronymrecall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ChoicesActivity extends AppCompatActivity {
    private AcronymUtil acronymUtil;
    private ArrayList<String> keyWords;
    private ArrayList<String> choices;
    private TableLayout tableLayoutChoices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);

        Intent intent = getIntent();
        keyWords = intent.getStringArrayListExtra(WordsActivity.EXTRA_KEYWORDS);

        acronymUtil = AcronymUtil.INSTANCE;
        choices = acronymUtil.getAcronymWords(keyWords);
        Collections.sort(choices);

        tableLayoutChoices = (TableLayout) findViewById(R.id.tableLayoutChoices);

        for (String choice : choices) {
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);

            TextView textView = new TextView(this);
            textView.setTextSize(22);
            textView.setText(choice);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String choice = ((TextView) v).getText().toString();
                    int choiceIndex = choices.indexOf(choice);


                }
            });

            row.addView(textView);
            tableLayoutChoices.addView(row);
        }
    }


}
