package com.potapp.cyberhelper.data.models.questions;

import java.io.Serializable;
import java.util.Date;

// класс вопроса
public class Question implements Serializable, Comparable<Question>{

    private int id;                                                                                 // идентификатор вопроса

    private String author;                                                                          // автор
    private String text;                                                                            // текст
    private Date date;                                                                              // дата публикации

    // ---------------------------------------------------------------------------------------------
    // get-методы
    // ---------------------------------------------------------------------------------------------


    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    // ---------------------------------------------------------------------------------------------
    // set-методы
    // ---------------------------------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Question question) {
        return date.compareTo(question.getDate());
    }
}
