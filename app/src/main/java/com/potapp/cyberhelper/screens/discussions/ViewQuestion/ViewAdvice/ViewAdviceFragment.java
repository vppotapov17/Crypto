package com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewAdvice;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.potapp.cyberhelper.database.dbs.DB_COMPONENTS;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.ViewQuestionAdapters.ViewAdviceAdapter;
import com.potapp.cyberhelper.models.answers.AdviceAnswer;
import com.potapp.cyberhelper.models.questions.AdviceQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAdviceFragment extends Fragment {

    private AdviceQuestion current_question;                                                        // просматриваемый вопрос

    public ViewAdviceFragment(){}

    private ViewAdviceViewModel vm;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        current_question = (AdviceQuestion) getArguments().getSerializable("current_question");

        vm = ViewModelProviders.of(this, new ViewAdviceFactory(current_question, Room.databaseBuilder(getContext(), DB_COMPONENTS.class, "COMPONENTS").build())).get(ViewAdviceViewModel.class);
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

        ImageButton backButton = getView().findViewById(R.id.back_button);
        RecyclerView rv = getView().findViewById(R.id.rv);
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);

        backButton.setOnClickListener(view -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        vm.getAnswerListLiveData().observe(this, adviceAnswers -> {
            progressBar.setVisibility(View.INVISIBLE);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(new ViewAdviceAdapter(adviceAnswers, current_question, NavHostFragment.findNavController(this)));
        });
    }
}
