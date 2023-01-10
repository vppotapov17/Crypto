package com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewAdvice;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.database.dbs.DB_COMPONENTS;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.models.answers.AdviceAnswer;
import com.potapp.cyberhelper.models.answers.Answer;
import com.potapp.cyberhelper.models.components.Bp;
import com.potapp.cyberhelper.models.components.Case;
import com.potapp.cyberhelper.models.components.Cooler;
import com.potapp.cyberhelper.models.components.Cpu;
import com.potapp.cyberhelper.models.components.Gpu;
import com.potapp.cyberhelper.models.components.Mb;
import com.potapp.cyberhelper.models.questions.AdviceQuestion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAdviceViewModel extends ViewModel {

    private AdviceQuestion question;
    private DB_COMPONENTS db_components;

    private MutableLiveData<List<AdviceAnswer>> answerListLiveData;

    public ViewAdviceViewModel(AdviceQuestion question, DB_COMPONENTS db_components){
        this.question = question;
        this.db_components = db_components;

        this.answerListLiveData = new MutableLiveData<>();

        // инициализация конфигурации для текущего вопроса
        Configuration configuration;                              // оцениваемая конфигурация

        String path = "Data/Questions/Advice/" + question.getId() + "/Configuration";       // путь к конфигурации вопроса в RealtimeDatabase
        DatabaseReference configurationReference = FirebaseDatabase.getInstance().getReference(path);

        // получаем имя конфигурации и коды комплектующих конфигурации из RelatimeDatabase

        configurationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                for (DataSnapshot s : snapshot1.getChildren()){
                    Configuration configuration = Configuration.createFromSnapshot(s);
                    configuration.name = s.getKey();

                    loadAnswerList();
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAnswerList(){
        List<AdviceAnswer> answerList = new ArrayList<>();                                          // список ответов на данный вопрос

        FirebaseDatabase.getInstance().getReference("Data/Questions/Advice/" + question.getId() + "/Answers/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 1; i <= snapshot.getChildrenCount(); ++i)
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
                answerListLiveData.setValue(answerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<List<AdviceAnswer>> getAnswerListLiveData(){
        return answerListLiveData;
    }
}
