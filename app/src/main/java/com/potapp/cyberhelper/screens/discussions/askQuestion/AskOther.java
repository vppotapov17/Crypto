package com.potapp.cyberhelper.screens.discussions.askQuestion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.potapp.cyberhelper.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AskOther extends Fragment {

    public AskOther(){}

    public static AskOther newInstance(){
        return new AskOther();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.dialog_ask_other_question, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();



        TextInputEditText questionText = getActivity().findViewById(R.id.inputField);
        MaterialButton publication = getActivity().findViewById(R.id.publication);


    }
}
