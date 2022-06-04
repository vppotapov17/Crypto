package com.potapp.cyberhelper.data.models.answers;

import com.potapp.cyberhelper.data.models.Configuration;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComponentsSelectionAnswer extends Answer{

    public ComponentsSelectionAnswer(long id, long questionId)
    {
        super(id, questionId);
    }

    private Configuration configuration;                                                            // конфигурация, предложенная автором ответа

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public DatabaseReference getRatingReference()
    {
        return FirebaseDatabase.getInstance().getReference("Data/Questions/ComponentsSelection/" + questionId + "/Answers/" + id + "/Rating");
    }
}
