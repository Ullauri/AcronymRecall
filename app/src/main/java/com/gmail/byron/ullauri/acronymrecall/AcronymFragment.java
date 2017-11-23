package com.gmail.byron.ullauri.acronymrecall;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AcronymFragment extends Fragment {
    private TextView textViewAcronym;
    private TextView textViewKeyWords;
    private OnDoneListener mListener;

    public AcronymFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acronym, container, false);
        textViewAcronym = (TextView) view.findViewById(R.id.textViewAcronym);
        textViewKeyWords = (TextView) view.findViewById(R.id.textViewKeyWords);

        Button button = (Button) view.findViewById(R.id.buttonDone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDone();
            }
        });

        return view;
    }

    public void setAcronym(String acronym, String keyWords) {
        textViewAcronym.setText(acronym);
        List<String> keyWordsList = new ArrayList<>(Arrays.asList(keyWords.split(getString(R.string.constant_delimiter))));
        String acronymKeyWordLetter = "";

        for (char letter : acronym.toCharArray()) {
            if (letter == ' ') {
                acronymKeyWordLetter += "\n";
                continue;
            }

            acronymKeyWordLetter += Character.toUpperCase(letter) + ": ";

            for (String word : keyWordsList) {
                if (word.charAt(0) == Character.toLowerCase(letter)) {
                    acronymKeyWordLetter += word.substring(0, 1).toUpperCase() + word.substring(1);
                    keyWordsList.remove(word);
                    break;
                }
            }

            acronymKeyWordLetter += "\n";
        }

        textViewKeyWords.setText(acronymKeyWordLetter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDoneListener) {
            mListener = (OnDoneListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDoneListener {
        void onDone();
    }
}
