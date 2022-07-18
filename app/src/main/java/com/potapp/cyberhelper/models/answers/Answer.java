package com.potapp.cyberhelper.models.answers;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// класс ответа
public abstract class Answer {

    protected long id;                                                                              // идентификатор ответа
    protected long questionId;                                                                      // идентификатор вопроса, соответствующего данному ответу

    protected String author;                                                                        // автор
    protected String text;                                                                          // текст
    protected Date date;                                                                            // дата публикации

    protected int rating;                                                                           // рейтинг
    protected List<String> likeUsers;                                                               // пользователи, которые поставили лайк ответу
    protected List<String> dislikeUsers;                                                            // пользователи, которые поставили дизлайк ответу

    // конструктор класса
    public Answer(long id, long questionId)
    {
        this.id = id;
        this.questionId = questionId;

        likeUsers = new ArrayList<>();
        dislikeUsers = new ArrayList<>();

        getRatingReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                likeUsers.removeAll(likeUsers);
                dislikeUsers.removeAll(dislikeUsers);

                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if (snapshot1.getValue().toString().equals("like")) likeUsers.add(snapshot1.getKey());
                    else dislikeUsers.add(snapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdateRating();
                for (String s : likeUsers)
                {
                    Log.d("ContentValues", s);
                }

            }
        }, 400);
    }

    // обновление рейтинга ответа в зависимости от количества пользователей, поставивших лайк и дизлайк
    private void UpdateRating()
    {
        rating = likeUsers.size() - dislikeUsers.size();
    }

    // путь к рейтингу ответа в RealtimeDatabase
    public abstract DatabaseReference getRatingReference();

    // добавление пользователя, поставившего лайк
    public void AddLikeUser(String UID)
    {
        likeUsers.add(UID);
        //UpdateRating();

        getRatingReference().child(UID).setValue("like");
    }

    // добавление пользователя, поставившего дизлайк
    public void AddDislikeUser(String UID)
    {
        dislikeUsers.add(UID);
        //UpdateRating();

        getRatingReference().child(UID).setValue("dislike");
    }

    // удаление пользователя, поставившего лайк
    public void RemoveLikeUser(String UID)
    {
        int removeIndex = -1;

        for (String s : likeUsers)
            if (s.equals(UID)) removeIndex = likeUsers.indexOf(s);

        if (removeIndex != -1) likeUsers.remove(removeIndex);
        //UpdateRating();

        DatabaseReference reference = getRatingReference();

        reference.child(UID).removeValue();
    }

    // удаление пользователя, поставившего дизлайк
    public void RemoveDislikeUser(String UID)
    {
        int removeIndex = -1;

        for (String s : dislikeUsers)
            if (s.equals(UID)) removeIndex = dislikeUsers.indexOf(s);

        if (removeIndex != -1) dislikeUsers.remove(removeIndex);
        //UpdateRating();

        DatabaseReference reference = getRatingReference();

        reference.child(UID).removeValue();
    }

    // проверка, лайкнул ли данный пользователь этот ответ
    public boolean isAnswerLiked(String UID)
    {
        for (String s : likeUsers)
            if (s.equals(UID)) return true;

        return false;
    }

    // проверка, дизлайкнул ли данный пользователь этот ответ
    public boolean isAnswerDisliked(String UID)
    {
        for (String s : dislikeUsers)
            if (s.equals(UID)) return true;

        return false;
    }
    // ---------------------------------------------------------------------------------------------
    // get-методы
    // ---------------------------------------------------------------------------------------------


    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public long getQuestionId() {
        return questionId;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public List<String> getLikeUsers() {
        return likeUsers;
    }

    public List<String> getDislikeUsers() {
        return dislikeUsers;
    }

    // ---------------------------------------------------------------------------------------------
    // set-методы
    // ---------------------------------------------------------------------------------------------


    public void setId(long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLikeUsers(List<String> likeUsers) {
        this.likeUsers = likeUsers;
    }

    public void setDislikeUsers(List<String> dislikeUsers) {
        this.dislikeUsers = dislikeUsers;
    }
}
