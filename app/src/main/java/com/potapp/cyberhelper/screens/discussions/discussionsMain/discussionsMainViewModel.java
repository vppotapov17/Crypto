package com.potapp.cyberhelper.screens.discussions.discussionsMain;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.models.questions.AdviceQuestion;
import com.potapp.cyberhelper.models.questions.ComponentsSelectionQuestion;
import com.potapp.cyberhelper.models.questions.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.screens.account.LoginFragment;
import com.potapp.cyberhelper.screens.discussions.askQuestion.QuestionCategories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class discussionsMainViewModel extends ViewModel {

    List<Question> myQuestions;
    List<Question> otherQuestions;

    private MutableLiveData<List<Question>> myQuestionsLiveData;
    private MutableLiveData<List<Question>> otherQuestionsLiveData;

    public discussionsMainViewModel() {

        myQuestions = new ArrayList<>();
        otherQuestions = new ArrayList<>();

        myQuestionsLiveData = new MutableLiveData<>();
        otherQuestionsLiveData = new MutableLiveData<>();

        new Handler().postDelayed(() -> loadQuestions(), 600);
    }


    // загрузка вопросов из сети

    void loadQuestions(){

        // путь к вопросам
        DatabaseReference questionsReference = FirebaseDatabase.getInstance().getReference("Data/Questions");



        // получение данных из RealtimeDatabase
        questionsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("AAA", "Cganhr");

                myQuestions.clear();
                otherQuestions.clear();

                SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy");
                for (DataSnapshot dataSnapshot : snapshot.child("ComponentsSelection").getChildren())
                {
                    ComponentsSelectionQuestion question = new ComponentsSelectionQuestion();

                    question.setId(Integer.valueOf(dataSnapshot.getKey()));                         // id вопроса
                    question.setAuthor(dataSnapshot.child("AuthorUID").getValue() + "");            // автор вопроса
                    question.setText(dataSnapshot.child("Text").getValue() + "");                   // текст вопроса
                    question.setBudget(Integer.parseInt(dataSnapshot.child("Budget")             // бюджет сборки
                            .getValue() + ""));

                    // дата публикации
                    try {
                        question.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(dataSnapshot.child("Date").getValue().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // добавление вопроса в один из списков
                    if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
                    {
                        if (question.getAuthor().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) myQuestions.add(question);
                        else otherQuestions.add(question);

                    }
                    else otherQuestions.add(question);

                }

                for (DataSnapshot dataSnapshot : snapshot.child("Advice").getChildren())
                {
                    AdviceQuestion question = new AdviceQuestion();

                    question.setId(Integer.valueOf(dataSnapshot.getKey()));                         // id вопроса
                    question.setAuthor(dataSnapshot.child("AuthorUID").getValue() + "");            // автор вопроса
                    question.setText(dataSnapshot.child("Text").getValue() + "");                   // текст вопроса

                    // дата публикации
                    try {
                        question.setDate(parser.parse(dataSnapshot.child("Date").getValue().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // добавление вопроса в один из списков
                    if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
                    {
                        if (question.getAuthor().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) myQuestions.add(question);
                        else otherQuestions.add(question);
                    }
                    else otherQuestions.add(question);
                }

                for (DataSnapshot dataSnapshot : snapshot.child("Other").getChildren())
                {
                    Question question = new Question();

                    question.setId(Integer.valueOf(dataSnapshot.getKey()));                         // id вопроса
                    question.setAuthor(dataSnapshot.child("AuthorUID").getValue() + "");            // автор вопроса
                    question.setText(dataSnapshot.child("Text").getValue() + "");                   // текст вопроса

                    // дата публикации
                    try {
                        question.setDate(parser.parse(dataSnapshot.child("Date").getValue().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // добавление вопроса в один из списков
                    if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
                    {
                        if (question.getAuthor().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) myQuestions.add(question);
                        else otherQuestions.add(question);
                    }
                    else otherQuestions.add(question);
                }

                myQuestionsLiveData.postValue(myQuestions);
                otherQuestionsLiveData.postValue(otherQuestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("AAA", error.getMessage());
            }

        });
    }

    public Fragment OnAskQuestionImageFragment(){
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
            return LoginFragment.newInstance();
        return QuestionCategories.newInstance();
    }

    public LiveData<List<Question>> getMyQuestionsLiveData(){
        return myQuestionsLiveData;
    }

    public LiveData<List<Question>> getOtherQuestionsLiveData(){
        return otherQuestionsLiveData;
    }

}
