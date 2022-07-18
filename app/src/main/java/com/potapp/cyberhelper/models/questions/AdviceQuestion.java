package com.potapp.cyberhelper.models.questions;

import com.potapp.cyberhelper.models.Configuration;

public class AdviceQuestion extends Question{

    Configuration configuration;                                                                    // оцениваемая конфигурация

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
