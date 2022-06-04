package com.potapp.cyberhelper.screens.discussions.discussionsMain;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.myQuestionsAdapter;
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.otherQuestionsAdapter;
import com.potapp.cyberhelper.data.models.questions.Question;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;


public class discussionsMainFragment extends Fragment {

    discussionsMainViewModel vm;

    private List<Question> myQuestionList;
    private List<Question> otherQuestionList;

    public discussionsMainFragment() {

    }

    public static discussionsMainFragment newInstance() {
      return new discussionsMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = ViewModelProviders.of(this).get(discussionsMainViewModel.class);
        myQuestionList = new ArrayList<>();
        otherQuestionList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discussions_main, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();


        // обработка списка моих вопросов

        RecyclerView myQuestionsRV = getActivity().findViewById(R.id.myQuestionsRV);                // инициализация RecyclerView
        ShimmerFrameLayout myQuestionsShimmer = getActivity().findViewById(R.id.shimmer);           // shimmer-layout

        myQuestionsRV.setVisibility(View.GONE);
        myQuestionsShimmer.startShimmerAnimation();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);

        myQuestionsRV.setLayoutManager(llm);                                                        // установка LayoutManager

        SnapHelper snapHelper = new LinearSnapHelper();
        myQuestionsRV.setOnFlingListener(null);

        snapHelper.attachToRecyclerView(myQuestionsRV);

        vm.myQuestionsLiveData.observe(this, s->{

            myQuestionList.clear();
            myQuestionList.addAll(s);

            Log.d("AAA", "s: " + s.size());

            if (myQuestionsRV.getAdapter() == null)
            {
                myQuestionsRV.setAdapter(new myQuestionsAdapter(myQuestionList));
            }
            else {
                myQuestionsRV.getAdapter().notifyDataSetChanged();
            }

            myQuestionsRV.setVisibility(View.VISIBLE);
            myQuestionsShimmer.stopShimmerAnimation();
            myQuestionsShimmer.setVisibility(View.INVISIBLE);
        });


        // обработка списка чужих вопросов

        RecyclerView otherQuestionsRV = getActivity().findViewById(R.id.otherQuestionsRV);
        ShimmerFrameLayout otherQuestionsShimmer = getActivity().findViewById(R.id.shimmer2);


        otherQuestionsRV.setVisibility(View.GONE);
        otherQuestionsShimmer.startShimmerAnimation();

        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        llm2.setOrientation(RecyclerView.HORIZONTAL);

        otherQuestionsRV.setLayoutManager(llm2);                                                    // установка LayoutManager

        SnapHelper snapHelper2 = new LinearSnapHelper();
        otherQuestionsRV.setOnFlingListener(null);

        snapHelper2.attachToRecyclerView(otherQuestionsRV);


        vm.otherQuestionsLiveData.observe(this, s->{
            otherQuestionList.clear();
            otherQuestionList.addAll(s);


            if (otherQuestionsRV.getAdapter() == null)
            {
                otherQuestionsRV.setAdapter(new otherQuestionsAdapter(otherQuestionList));
            }
            else {
                otherQuestionsRV.getAdapter().notifyDataSetChanged();
            }

            otherQuestionsRV.setVisibility(View.VISIBLE);
            otherQuestionsShimmer.stopShimmerAnimation();
            otherQuestionsShimmer.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("AAA", "Destroy");
    }
}