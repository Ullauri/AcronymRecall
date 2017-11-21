package com.gmail.byron.ullauri.acronymrecall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

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
                    final String choice = ((TextView) v).getText().toString();
                    String result = "";

                    for (char letter : choice.toCharArray()) {
                        if (letter == ' ')
                            continue;

                        letter = Character.toLowerCase(letter);
                        result += Character.toUpperCase(letter) + ": ";

                        for (String keyword : keyWords) {
                            if (keyword.charAt(0) == letter) {
                                result += keyword;
                                keyWords.remove(keyword);
                                break;
                            }
                        }

                        result += "\n";
                    }

                    final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext())
                            .setTitle("Add Acronym")
                            .setMessage("Do you want to save this acronym?")
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences acronymsFile = getApplicationContext().getSharedPreferences(
                                            getString(R.string.file_acronyms), Context.MODE_PRIVATE);

                                    if (acronymsFile.getString(choice, "").isEmpty()) {
                                        String keywordsJoin = "";
                                        String DELIMITER = getString(R.string.constant_delimiter);

                                        for (String keyword : keyWords) {
                                            keywordsJoin += keyword + DELIMITER;
                                        }

                                        acronymsFile.edit()
                                        .putString(choice, keywordsJoin)
                                        .apply();


                                    } else {
                                        Toast.makeText(getApplicationContext(), choice + " is already in use!", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();

                }
            });

            row.addView(textView);
            tableLayoutChoices.addView(row);
        }
    }


}
