package com.potapp.cyberhelper.adapters.DiscussionsAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.models.questions.AdviceQuestion;
import com.potapp.cyberhelper.models.questions.ComponentsSelectionQuestion;
import com.potapp.cyberhelper.models.questions.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewAdvice;
import com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewComponentsSelection;
import com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewOther;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class myQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Question> myQuestions;
    FragmentManager fm;

    public myQuestionsAdapter(List<Question> myQuestions, FragmentManager fm){
        this.myQuestions = myQuestions;
        this.fm = fm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.discussions_rv_my_question, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TextView date = holder.itemView.findViewById(R.id.date);
        TextView questionText = holder.itemView.findViewById(R.id.questionText);
        TextView category = holder.itemView.findViewById(R.id.category);
        TextView answersQuantity = holder.itemView.findViewById(R.id.answersQuantity);

        Question current_question = myQuestions.get(position);

        // дата публикации
        date.setText(transformDate(current_question.getDate()));

        // текст вопроса
        questionText.setText(current_question.getText());

        // определение категории вопроса
        String current_category;
        String current_path;

        if (current_question instanceof AdviceQuestion){
            current_category = "Оценка и советы";
            current_path = "Data/Questions/Advice/" + current_question.getId();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fm.beginTransaction().replace(R.id.fragment_container, ViewAdvice.newInstance((AdviceQuestion) current_question)).commit();

                }
            });
        }
        else if (current_question instanceof ComponentsSelectionQuestion){
            current_category = "Подбор комплектующих";
            current_path = "Data/Questions/ComponentsSelection/" + current_question.getId();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fm.beginTransaction().replace(R.id.fragment_container, ViewComponentsSelection.newInstance((ComponentsSelectionQuestion) current_question)).commit();

                }
            });
        }
        else{
            current_category = "Прочее";
            current_path = "Data/Questions/Other/" + current_question.getId();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fm.beginTransaction().replace(R.id.fragment_container, ViewOther.newInstance(current_question)).commit();

                }
            });
        }

        category.setText(current_category);

        // количество ответов
        FirebaseDatabase.getInstance().getReference(current_path + "/Answers/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                answersQuantity.setText(snapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("AAA", "Error");
                answersQuantity.setText("0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return myQuestions.size();
    }


    String transformDate(Date publicationDate) {

        String result;

        Date currentDate = new Date();

        // тест

//        try {
//            publicationDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("22.05.2003 00:00");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        long difference = currentDate.getTime() - publicationDate.getTime();

        // количество суток, прошедших с момента публикации вопроса
        long diffDays = difference / (1000 * 60 * 60 * 24);

        int currentDay = Integer.parseInt(new SimpleDateFormat("dd").format(currentDate));
        int publicationDay = Integer.parseInt(new SimpleDateFormat("dd").format(publicationDate));


        // сегодня
        if (diffDays == 0 && currentDay == publicationDay)
        {
            result = "сегодня в " + new SimpleDateFormat("HH:mm").format(publicationDate);
        }
        // вчера
        else if (diffDays == 0 || (diffDays == 1 && currentDay - publicationDay == 1))
        {
            result = "вчера в " + new SimpleDateFormat("HH:mm").format(publicationDate);
        }
        else {
            if (diffDays < 7)
            {
                if (diffDays == 1) result = diffDays + " день назад";
                else if (diffDays < 5) result = diffDays + " дня назад";
                else result = diffDays + " дней назад";
            }
            else if (diffDays < 28){
                if (diffDays / 7 == 1) result = "неделю назад";
                else result = (diffDays / 7) + " недели назад";
            }
            else if (diffDays < 365)
            {
                long d = (long) (diffDays / 30.4167);
                if (d == 1) result = d + " месяц назад";
                else if (d < 5) result = d + " месяца назад";
                else result = d + " месяцев назад";
            }
            else {
                long d = (long) (diffDays / 365.2425);
                if (d == 1) result = d + " год назад";
                else if (d < 5) result = d + " года назад";
                else if (d <= 20) result = d + " лет назад";
                else if (d % 10 == 1) result = d + " год назад";
                else if (d % 10 < 5) result = d + " года назад";
                else result = d + " лет назад";
            }
        }

        return result;
    }
}
