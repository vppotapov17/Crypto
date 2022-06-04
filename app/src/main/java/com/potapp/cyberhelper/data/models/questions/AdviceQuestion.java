package com.potapp.cyberhelper.data.models.questions;

import com.potapp.cyberhelper.data.models.Configuration;

public class AdviceQuestion extends Question{

    Configuration configuration;                                                                    // оцениваемая конфигурация

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
