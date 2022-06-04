package com.potapp.cyberhelper.screens.discussions.askQuestion;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class AskComponentsSelection extends Fragment {

    public AskComponentsSelection(){}

    public static AskComponentsSelection newInstance(){
        return new AskComponentsSelection();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        MaterialButton publication = getActivity().findViewById(R.id.publication);
        TextInputEditText budgetField = getActivity().findViewById(R.id.budgetValue);
        TextView comment_text = getActivity().findViewById(R.id.commentText);

        comment_text.setMovementMethod(new ScrollingMovementMethod());


        // диалоговое окно ввода комментария

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_comment, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText insertComment = view.findViewById(R.id.insertComment);
        MaterialButton done = view.findViewById(R.id.done);
        done.setOnClickListener(view1 -> {
            comment_text.setText(insertComment.getText().toString());
            dialog.dismiss();
        });


        insertComment.requestFocus();
        comment_text.setClickable(true);
        comment_text.setOnClickListener(view2 -> {
            insertComment.setText(comment_text.getText().toString());
            insertComment.setSelection(insertComment.getText().length());
            dialog.show();
        });



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Data/Questions/ComponentsSelection");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                publication.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference reference = databaseReference.child(snapshot.getChildrenCount() + 1 + "");

                        // добавление в базу информации о вопросе
                        HashMap<String, String> map = new HashMap<>();

                        map.put("Author", MainActivity.current_user.getName());
                        map.put("Date", simpleDateFormat.format(new Date()));
                        map.put("Budget", budgetField.getText().toString());
                        map.put("Text", comment_text.getText().toString());

                        reference.setValue(map);

                        getFragmentManager().popBackStack();
                        getFragmentManager().popBackStack();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
