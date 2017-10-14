package com.gmail.byron.ullauri.acronymrecall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsActivity extends AppCompatActivity {
    public static final String EXTRA_KEYWORDS = "com.gmail.byron.ullauri.KEYWORDS";
    private final int MIN_KEYWORD_COUNT = 4; // Minimum number of required letters for acronym
    private Intent intent;
    private RelativeLayout relativeLayoutOwl;
    private ImageButton imageButtonAddKeyWord;
    private FloatingActionButton fab;
    private ScrollView scrollViewKeyWords;
    private TableLayout tableLayoutKeyWords;
    private ArrayList<String> keyWords;
    private float scale;
    private final int TYPING_TIMEOUT = 2250;
    private Handler timeoutHandler;
    private Runnable typingRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        intent = new Intent(this, ChoicesActivity.class);
        scale = this.getResources().getDisplayMetrics().density;

        relativeLayoutOwl = (RelativeLayout) findViewById(R.id.relativeLayoutOwl);
        scrollViewKeyWords = (ScrollView) findViewById(R.id.scrollViewKeyWords);
        tableLayoutKeyWords = (TableLayout) findViewById(R.id.tableLayoutKeyWords);
        imageButtonAddKeyWord = (ImageButton) findViewById(R.id.imageButtonAddKeyWord);

        for (int i = 0; i < MIN_KEYWORD_COUNT; i++) {
            TableRow tableRow = createDefaultRow();
            tableLayoutKeyWords.addView(tableRow);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyWords = new ArrayList<>(tableLayoutKeyWords.getChildCount());

                for (int i = 0; i < tableLayoutKeyWords.getChildCount(); i++) {
                    TableRow currentRow = (TableRow) tableLayoutKeyWords.getChildAt(i);
                    String keyWord = ((EditText) currentRow.getChildAt(1)).getText().toString();
                    keyWord = keyWord.trim().toLowerCase();

                    if (!keyWord.equals("")) {
                        keyWords.add(keyWord);
                    }
                }

                if (keyWords.size() >= MIN_KEYWORD_COUNT) {
                    intent.putStringArrayListExtra(EXTRA_KEYWORDS, keyWords);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(view, getString(R.string.validation_msg_1) + " " + MIN_KEYWORD_COUNT + " " + getString(R.string.validation_msg_2), Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.BLACK);
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackbar.show();
                }
            }
        });

        timeoutHandler = new Handler();
        typingRunnable = new Runnable() {
            public void run() {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    view.clearFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    relativeLayoutOwl.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private int getDP(int px) {
        return (int) (px * scale + 0.5f);
    }

    private TableRow createDefaultRow() {
        final TableRow tableRow = new TableRow(this);

        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        textViewParams.setMargins(0, 0, getDP(5), 0);

        final TextView textView = new TextView(this);
        String text = String.valueOf(tableLayoutKeyWords.getChildCount() + 1) + ".)";
        textView.setText(text);
        textView.setLayoutParams(textViewParams);

        final EditText editText = new EditText(this);
        editText.setEms(10);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus || relativeLayoutOwl.getY() < imageButtonAddKeyWord.getY()) {
                    relativeLayoutOwl.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    timeoutHandler.removeCallbacks(typingRunnable);
                    timeoutHandler.postDelayed(typingRunnable, TYPING_TIMEOUT);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timeoutHandler.removeCallbacks(typingRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().trim().length() > 0) {
                    timeoutHandler.postDelayed(typingRunnable, TYPING_TIMEOUT);
                }
            }
        });

        tableRow.addView(textView);
        tableRow.addView(editText);

        return tableRow;
    }

    public void addKeyWord(View v) {
        TableRow tableRow = createDefaultRow();

        ImageButton imageButton = new ImageButton(this);

        int imgSize = getDP(20);
        TableRow.LayoutParams imgParams = new TableRow.LayoutParams(imgSize, imgSize);
        imgParams.setMargins(0, getDP(10), 0, 0);

        imageButton.setLayoutParams(imgParams);
        imageButton.setBackgroundResource(android.R.drawable.ic_delete);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TableRow parentRow = (TableRow) v.getParent();
                int removeIndex = tableLayoutKeyWords.indexOfChild(parentRow);
                tableLayoutKeyWords.removeViewAt(removeIndex);

                for (int i = removeIndex; i < tableLayoutKeyWords.getChildCount(); i++) {
                    TableRow updateRow = (TableRow) tableLayoutKeyWords.getChildAt(i);

                    TextView updateTextView = (TextView) updateRow.getChildAt(0);
                    String text = String.valueOf(i + 1) + ".)";
                    updateTextView.setText(text);
                }
            }
        });

        tableRow.addView(imageButton);

        tableLayoutKeyWords.addView(tableRow);
        // Stop ScrollBar from scrolling to top, instead scroll to bottom
        scrollViewKeyWords.post(new Runnable() {
            @Override
            public void run() {
                scrollViewKeyWords.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

}
