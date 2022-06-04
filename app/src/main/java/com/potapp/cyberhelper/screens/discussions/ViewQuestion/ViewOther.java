package com.potapp.cyberhelper.screens.discussions.ViewQuestion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.data.models.answers.AdviceAnswer;
import com.potapp.cyberhelper.data.models.questions.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewOther extends Fragment {

    private Question current_question;                                                         // просматриваемый вопрос

    public ViewOther(){}

    public static ViewOther newInstance(Question question){
        Bundle bundle = new Bundle();
        bundle.putSerializable("current_question", (Serializable) question);
        ViewOther fragment = new ViewOther();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        current_question = (Question) getArguments().getSerializable("current_question");
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


        List<AdviceAnswer> answerList = new ArrayList<>();


        RecyclerView rv = getActivity().findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //rv.setAdapter(new ViewAdviceQuestionAdapter(answerList, getFragmentManager()));
    }



}
