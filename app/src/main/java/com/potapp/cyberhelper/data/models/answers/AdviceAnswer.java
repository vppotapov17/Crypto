package com.potapp.cyberhelper.data.models.answers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdviceAnswer extends Answer{

    public AdviceAnswer (long id, long questionId)
    {
        super(id, questionId);
    }

    private int advice;                                                                             // оценка (0 - плохо, 1 - нейтрально, 2 - отлично)

    // переопределённые методы

    @Override
    public DatabaseReference getRatingReference()
    {
        return FirebaseDatabase.getInstance().getReference("Data/Questions/Advice/" + questionId + "/Answers/" + id + "/Rating");
    }

    public int getAdvice() {
        return advice;
    }

    public void setAdvice(int advice) {
        this.advice = advice;
    }
}
