package com.potapp.cyberhelper.screens.discussions.askQuestion;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionCategories extends Fragment {

    public QuestionCategories(){}

    public static QuestionCategories newInstance(){
        return new QuestionCategories();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question_categories, container, false);
    }

    public void onStart()
    {
        super.onStart();

        ImageButton backButton = getActivity().findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        CardView componentsSelection = getActivity().findViewById(R.id.components_selection);
        CardView advise = getActivity().findViewById(R.id.advise);
        CardView other = getActivity().findViewById(R.id.other);

        componentsSelection.setOnClickListener(view -> loadFragment(AskComponentsSelection.newInstance()));

        advise.setOnClickListener(view -> loadFragment(AskAdvice.newInstance()));

        other.setOnClickListener(view -> {
            // диалоговое окно для ввода вопроса
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_other_question, null);

            EditText insertComment = dialogView.findViewById(R.id.insertComment);
            Button done = dialogView.findViewById(R.id.done);

            insertComment.requestFocus();

            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

            // обработка нажатия на кнопку "Опубликовать"
            done.setOnClickListener(view1 -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Data/Questions/Other");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DatabaseReference reference = databaseReference.child(snapshot.getChildrenCount() + 1 + "");

                            reference.child("Author").setValue(MainActivity.current_user.getName());
                            reference.child("Date").setValue(simpleDateFormat.format(new Date()));
                            reference.child("Text").setValue(insertComment.getText().toString());

                            dialog.dismiss();

                            getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            });


        });
    }

    public void loadFragment(Fragment fragment)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}
