package com.gmail.byron.ullauri.acronymrecall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Map;

public class AcronymsActivity extends Activity implements AcronymFragment.OnDoneListener {
    private ScrollView scrollViewAcronyms;
    private TableLayout tableLayoutAcronyms;
    private float scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acronyms);

        scrollViewAcronyms = (ScrollView) findViewById(R.id.scrollViewAcronyms);
        tableLayoutAcronyms = (TableLayout) findViewById(R.id.tableLayoutAcronyms);
        scale = this.getResources().getDisplayMetrics().density;

        toggleAcronymFragment(false);

        SharedPreferences acronymsFile = getSharedPreferences(getString(R.string.file_acronyms), Context.MODE_PRIVATE);
        Map<String, ?> keys = acronymsFile.getAll();

        if (keys.size() >= 1) {
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                if (entry != null) {
                    createRow(entry.getKey(), entry.getValue().toString());
                }
            }
        } else {
            noAcronymsFound();
        }

    }

    private int getDP(int px) {
        return (int) (px * scale + 0.5f);
    }

    private void noAcronymsFound() {
        TableRow row = new TableRow(this);

        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(22);
        textView.setText(getString(R.string.description_no_acronyms) + "\n" + getString(R.string.suggestion_add_acronym));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WordsActivity.class));
            }
        });

        row.addView(textView);
        row.setGravity(Gravity.CENTER);
        tableLayoutAcronyms.addView(row);
    }

    private void toggleAcronymFragment(boolean show) {
        FragmentManager fragmentManager = getFragmentManager();

        AcronymFragment acronymFragment = (AcronymFragment) fragmentManager
                .findFragmentById(R.id.acronymFragment);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (show) {
            scrollViewAcronyms.setVisibility(View.GONE);
            fragmentTransaction.show(acronymFragment);
        } else {
            fragmentTransaction.hide(acronymFragment);
            scrollViewAcronyms.setVisibility(View.VISIBLE);
        }

        fragmentTransaction.commit();
    }

    public void createRow(final String acronym, final String keyWords) {
        final TableRow tableRow = new TableRow(this);

        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        textViewParams.setMargins(0, 0, getDP(5), 0);

        final TextView textView = new TextView(this);
        textView.setTextSize(22);
        textView.setText(acronym);
        textView.setLayoutParams(textViewParams);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcronymFragment acronymFragment = (AcronymFragment) getFragmentManager()
                        .findFragmentById(R.id.acronymFragment);
                acronymFragment.setAcronym(acronym, keyWords);

                toggleAcronymFragment(true);
            }
        });

        ImageButton imageButton = new ImageButton(this);

        int imgSize = getDP(20);
        TableRow.LayoutParams imgParams = new TableRow.LayoutParams(imgSize, imgSize);
        imgParams.setMargins(0, getDP(2), 0, 0);

        imageButton.setLayoutParams(imgParams);
        imageButton.setBackgroundResource(android.R.drawable.ic_delete);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                new AlertDialog.Builder(AcronymsActivity.this)
                        .setTitle("Remove '" + acronym + "'")
                        .setMessage("Do you want to remove this acronym?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences acronymsFile = getApplicationContext().getSharedPreferences(
                                        getString(R.string.file_acronyms), Context.MODE_PRIVATE);
                                acronymsFile.edit().remove(acronym).apply();

                                TableRow parentRow = (TableRow) v.getParent();
                                int removeIndex = tableLayoutAcronyms.indexOfChild(parentRow);
                                tableLayoutAcronyms.removeViewAt(removeIndex);

                                if (tableLayoutAcronyms.getChildCount() == 0) {
                                    noAcronymsFound();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });


        tableRow.addView(textView);
        tableRow.addView(imageButton);
        tableRow.setGravity(Gravity.CENTER);

        tableLayoutAcronyms.addView(tableRow);
    }

    @Override
    public void onDone() {
        toggleAcronymFragment(false);
    }
}
