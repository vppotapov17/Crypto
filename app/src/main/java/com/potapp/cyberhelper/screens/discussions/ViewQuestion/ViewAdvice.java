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
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.ViewQuestionAdapters.ViewAdviceAdapter;
import com.potapp.cyberhelper.data.models.answers.AdviceAnswer;
import com.potapp.cyberhelper.data.models.questions.AdviceQuestion;
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

public class ViewAdvice extends Fragment {

    private AdviceQuestion current_question;                                                        // просматриваемый вопрос

    public ViewAdvice(){}


    public static ViewAdvice newInstance(AdviceQuestion question)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("current_question", (Serializable) question);
        ViewAdvice fragment = new ViewAdvice();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        current_question = (AdviceQuestion) getArguments().getSerializable("current_question");


        // инициализируем поле Configuration

        Configuration configuration = new Configuration();                                          // оцениваемая конфигурация

        String path = "Data/Questions/Advice/" + current_question.getId() + "/Configuration";       // путь к вопросу в RealtimeDatabase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);


        // получаем имя конфигурации и коды комплектующих конфигурации из RelatimeDatabase
        HashMap<String, String> componentsMap = new HashMap<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                componentsMap.put("name", snapshot.child("name").getValue().toString());            // имя конфигурации

                // коды комплектующих
                componentsMap.put("mb", snapshot.child("mb").getValue().toString());
                componentsMap.put("cpu", snapshot.child("cpu").getValue().toString());
                componentsMap.put("gpu", snapshot.child("gpu").getValue().toString());

                componentsMap.put("ozuQuantity", snapshot.child("ozuQuantity").getValue() + "");
                componentsMap.put("ozuCode", snapshot.child("ozuCode").getValue() + "");

                componentsMap.put("bp", snapshot.child("bp").getValue().toString());
                componentsMap.put("cooler", snapshot.child("cooler").getValue().toString());
                componentsMap.put("pccase", snapshot.child("pccase").getValue().toString());

                if (snapshot.child("hdd").getValue() != null)
                {
                    componentsMap.put("hddQuantity", snapshot.child("hddQuantity").getValue() + "");
                    componentsMap.put("hddCode", snapshot.child("hddCode").getValue() + "");
                }

                if (snapshot.child("ssd25").getValue() != null)
                {
                    componentsMap.put("ssd25Quantity", snapshot.child("ssd25Quantity").getValue() + "");
                    componentsMap.put("ssd25Code", snapshot.child("ssd25Code").getValue() + "");
                }

                if (snapshot.child("ssdM2").getValue() != null)
                {
                    componentsMap.put("ssdM2Quantity", snapshot.child("ssdM2Quantity").getValue() + "");
                    componentsMap.put("ssdM2Code", snapshot.child("ssdM2Code").getValue() + "");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // подключаемся к базам данных Room в фоновом потоке
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                current_question.setConfiguration(Configuration.CreateFromFirebase(componentsMap, getContext()));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t.start();
            }
        }, 500);

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

        List<AdviceAnswer> answerList = new ArrayList<>();                                          // список ответов на данный вопрос

        // чтение ответов из RealtimeDatabase и добавление их в answerList
        FirebaseDatabase.getInstance().getReference("Data/Questions/Advice/" + current_question.getId() + "/Answers/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = answerList.size() + 1; i <= snapshot.getChildrenCount(); ++i)
                {
                    DataSnapshot snapshot1 = snapshot.child(i + "");

                    AdviceAnswer answer = new AdviceAnswer(Long.parseLong(snapshot1.getKey()), Long.parseLong(snapshot1.child("QuestionId").getValue().toString()));

                    answer.setAuthor(snapshot1.child("Author").getValue().toString());                                  // автор ответа
                    answer.setAdvice(Integer.parseInt(snapshot1.child("Mark").getValue().toString()));                  // оценка
                    answer.setText(snapshot1.child("Text").getValue().toString());                                      // текст

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
                rv.setAdapter(new ViewAdviceAdapter(answerList, current_question, getFragmentManager()));
            }
        }, 700);

    }
}
