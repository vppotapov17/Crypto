package com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewAdvice;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.potapp.cyberhelper.database.dbs.DB_COMPONENTS;
import com.potapp.cyberhelper.models.questions.AdviceQuestion;
import com.yandex.metrica.impl.ob.T;

public class ViewAdviceFactory implements ViewModelProvider.Factory {

    AdviceQuestion question;
    DB_COMPONENTS db_components;

    public ViewAdviceFactory(AdviceQuestion question, DB_COMPONENTS db_components){
        this.question = question;
        this.db_components = db_components;
    }

    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new ViewAdviceViewModel(question, db_components);
    }
}
