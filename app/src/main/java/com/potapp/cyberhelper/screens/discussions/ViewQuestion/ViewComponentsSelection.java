package com.potapp.cyberhelper.screens.discussions.ViewQuestion;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.ViewQuestionAdapters.ViewComponentsSelectionAdapter;
import com.potapp.cyberhelper.data.models.answers.ComponentsSelectionAnswer;
import com.potapp.cyberhelper.data.models.questions.ComponentsSelectionQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewComponentsSelection extends Fragment {

    private ComponentsSelectionQuestion current_question;                                           // просматриваемый вопрос

    public ViewComponentsSelection(){}

    public static ViewComponentsSelection newInstance(ComponentsSelectionQuestion question){
        Bundle bundle = new Bundle();
        bundle.putSerializable("current_question", (Serializable) question);
        ViewComponentsSelection fragment = new ViewComponentsSelection();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        current_question = (ComponentsSelectionQuestion) getArguments().getSerializable("current_question");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.discussions_view_question, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ImageButton backButton = getActivity().findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        RecyclerView rv = getActivity().findViewById(R.id.rv);
        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);

        List<ComponentsSelectionAnswer> answerList = new ArrayList<>();                             // список ответов на данный вопрос

        // чтение ответов из RealtimeDatabase и добавление их в answerList
        FirebaseDatabase.getInstance().getReference("Data/Questions/ComponentsSelection/" + current_question.getId() + "/Answers/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = answerList.size() + 1; i <= snapshot.getChildrenCount(); ++i)
                {
                    DataSnapshot snapshot1 = snapshot.child(i + "");

                    ComponentsSelectionAnswer answer = new ComponentsSelectionAnswer(Long.parseLong(snapshot1.getKey()),
                            Long.parseLong(snapshot1.child("QuestionId").getValue().toString()));

                    answer.setAuthor(snapshot1.child("Author").getValue().toString());          // автор ответа
                    answer.setText(snapshot1.child("Text").getValue().toString());              // текст

                    // конфигурация
                    HashMap<String, String> configuration = (HashMap<String, String>) snapshot1.child("Configuration").getValue();
                    answer.setConfiguration(Configuration.CreateFromFirebase(configuration, getContext()));

                    // дата публикации
                    try {
                        answer.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(snapshot1.child("Date").getValue().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    answerList.add(answer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(new ViewComponentsSelectionAdapter(answerList, current_question, getFragmentManager()));
            }
        }, 700);
    }
}
