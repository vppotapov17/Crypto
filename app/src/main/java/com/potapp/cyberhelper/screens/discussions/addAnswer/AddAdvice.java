package com.potapp.cyberhelper.screens.discussions.addAnswer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddAdvice extends Fragment {

    private long questionID;                                                                        // ID вопроса, на который дается ответ

    public AddAdvice(){}

    public static AddAdvice newInstance(long ID){
        AddAdvice fragment = new AddAdvice();

        Bundle bundle = new Bundle();
        bundle.putLong("questionID", ID);
        fragment.setArguments(bundle);
        LiveData<String> d = new LiveData<String>() {
            @Override
            public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super String> observer) {
                super.observe(owner, observer);
            }
        };

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        questionID = getArguments().getLong("questionID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.discussions_add_advice, container, false);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        ChipGroup chipGroup = getActivity().findViewById(R.id.chip_group);
        MaterialButton publication = getActivity().findViewById(R.id.publication);
        TextView comment_text = getActivity().findViewById(R.id.commentText);
        comment_text.setMovementMethod(new ScrollingMovementMethod());

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


        publication.setOnClickListener(view3 -> {

            final long[] answerID = new long[1];

            // определение ID ответа
            FirebaseDatabase.getInstance().getReference("Data/Questions/Advice/" + questionID + "/Answers/").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // id = количество уже существующих ответов + 1
                    answerID[0] = snapshot.getChildrenCount() + 1;
                    Log.d("ContentValues", "Success! " + answerID[0]);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("ContentValues", "Error!");
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data/Questions/Advice/" + questionID + "/Answers/" + answerID[0]);
                    HashMap<String, String> map = new HashMap<>();

                    map.put("QuestionId", questionID + "");
                    map.put("Author", "@" + MainActivity.current_user.getName());
                    map.put("Date", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
                    map.put("Text", comment_text.getText().toString());

                    // оценка данной конфигурации
                    int mark;

                    if (chipGroup.getCheckedChipId() == R.id.chipBad) mark = 0;
                    else if (chipGroup.getCheckedChipId() == R.id.chipNeutral) mark = 1;
                    else mark = 2;

                    map.put("Mark", mark + "");

                    reference.setValue(map);

                    try {
                        getFragmentManager().popBackStack();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getContext(), "Произошла ошибка!", Toast.LENGTH_SHORT);
                    }

                }
            }, 300);

        });

    }
}
