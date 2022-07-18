package com.potapp.cyberhelper.screens.discussions.askQuestion.AskComponentsSelection;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AskComponentsSelectionFragment extends Fragment {

    private AskComponentsSelectionViewModel vm;

    public AskComponentsSelectionFragment(){}

    public static AskComponentsSelectionFragment newInstance(){
        return new AskComponentsSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(this).get(AskComponentsSelectionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discussions_ask_compsel, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // элементы интерфейса
        MaterialButton publication = getActivity().findViewById(R.id.publication);
        TextInputEditText budgetField = getActivity().findViewById(R.id.budgetValue);
        TextView textField = getActivity().findViewById(R.id.textField);
        ImageButton back = getActivity().findViewById(R.id.back_button);

        back.setOnClickListener(view -> getFragmentManager().popBackStack());

        textField.setMovementMethod(new ScrollingMovementMethod());

        // диалоговое окно ввода комментария
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_comment, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText insertComment = view.findViewById(R.id.insertComment);
        MaterialButton done = view.findViewById(R.id.done);
        done.setOnClickListener(view1 -> {
            textField.setText(insertComment.getText().toString());
            dialog.dismiss();
        });

        insertComment.requestFocus();
        textField.setClickable(true);
        textField.setOnClickListener(view2 -> {
            insertComment.setText(textField.getText().toString());
            insertComment.setSelection(insertComment.getText().length());
            dialog.show();
        });


        publication.setOnClickListener(view1 -> {
            vm.publishQuestion(FirebaseAuth.getInstance().getUid(), budgetField.getText().toString(), textField.getText().toString());
            getFragmentManager().popBackStack();
        });
    }

}
