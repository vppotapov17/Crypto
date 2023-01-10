package com.potapp.cyberhelper.screens.discussions.askQuestion;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.screens.discussions.askQuestion.AskAdvice.AskAdviceFragment;
import com.potapp.cyberhelper.screens.discussions.askQuestion.AskComponentsSelection.AskComponentsSelectionFragment;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class QuestionCategories extends Fragment {

    private NavController navController;
    private Integer publishedQuestionsQuantity;

    public QuestionCategories(){}

    public static QuestionCategories newInstance(){
        return new QuestionCategories();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
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

        componentsSelection.setOnClickListener(view -> loadFragment(AskComponentsSelectionFragment.newInstance()));

        advise.setOnClickListener(view -> loadFragment(AskAdviceFragment.newInstance()));

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

            // количество опубликованных вопросов
            FirebaseDatabase.getInstance().getReference("Data/Questions/Advice").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    publishedQuestionsQuantity = (int) snapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // обработка нажатия на кнопку "Опубликовать"
            done.setOnClickListener(view1 -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data/Questions/Other/" + (publishedQuestionsQuantity + 1));

                HashMap<String, String> map = new HashMap<>();

                map.put("AuthorUID", FirebaseAuth.getInstance().getUid());
                map.put("Date", new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
                map.put("Text", insertComment.getText().toString());

                reference.setValue(map);

                dialog.dismiss();

                Toast.makeText(getContext(), "Вопрос опубликован", Toast.LENGTH_SHORT).show();

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
