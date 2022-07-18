package com.potapp.cyberhelper.screens.discussions.askQuestion.AskComponentsSelection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.models.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AskComponentsSelectionViewModel extends ViewModel {

    private int publishedQuestionsQuantity;

    public AskComponentsSelectionViewModel(){

        // количество опубликованных вопросов
        FirebaseDatabase.getInstance().getReference("Data/Questions/ComponentsSelection").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                publishedQuestionsQuantity = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // добавление в базу информации о вопросе
    public void publishQuestion(String authorUID, String budget, String text){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data/Questions/ComponentsSelection/" + (publishedQuestionsQuantity + 1));

        HashMap<String, String> map = new HashMap<>();

        map.put("AuthorUID", authorUID);
        map.put("Budget", budget);
        map.put("Date", new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
        map.put("Text", text);

        reference.setValue(map);
    }

}
