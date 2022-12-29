package com.potapp.cyberhelper.adapters.DiscussionsAdapters.ViewQuestionAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.configurator.viewReadyConfiguration.viewReadyConfigurationFragment;
import com.potapp.cyberhelper.models.answers.ComponentsSelectionAnswer;
import com.potapp.cyberhelper.screens.discussions.addAnswer.AddComponentsSelection;
import com.potapp.cyberhelper.models.questions.ComponentsSelectionQuestion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class ViewComponentsSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ComponentsSelectionAnswer> answerList;
    private ComponentsSelectionQuestion current_question;
    private FragmentManager fm;

    public ViewComponentsSelectionAdapter(List<ComponentsSelectionAnswer> answerList, ComponentsSelectionQuestion current_question, FragmentManager fm)
    {
        this.answerList = answerList;
        this.current_question = current_question;
        this.fm = fm;
    }

    @Override
    public int getItemViewType(int position)
    {
        // если мой вопрос и ответы есть
        if (current_question.getAuthor().equals(FirebaseAuth.getInstance().getUid()) && answerList.size() != 0)
        {
            if (position == 0) return 0;
            return 1;
        }
        // остальные случаи
        else {
            if (position == 0) return 0;
            if (position == getItemCount() - 1) return 2;
            return 1;
        }
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View current_view = null;

        switch (viewType)
        {
            case 0: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_components_selection_header, parent, false); break;
            case 1: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussions_rv_answer_compsel, parent, false); break;
            case 2: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_footer, parent, false); break;
        }

        return new RecyclerView.ViewHolder(current_view){};
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        if (getItemViewType(position) == 0)
        {
            // характеристики вопроса
            TextView authorValue = holder.itemView.findViewById(R.id.authorValue);
            TextView dateValue = holder.itemView.findViewById(R.id.dateValue);
            TextView categoryValue = holder.itemView.findViewById(R.id.categoryValue);
            TextView budgetValue = holder.itemView.findViewById(R.id.budgetValue);
            TextView text = holder.itemView.findViewById(R.id.questionText);


            authorValue.setText(current_question.getAuthor());
            dateValue.setText(simpleDateFormat.format(current_question.getDate()));
            categoryValue.setText("Подбор комплектующих");
            budgetValue.setText(current_question.getBudget() + " ₽");
            text.setText(current_question.getText());

        }
        else if (getItemViewType(position) == 1)
        {
            ComponentsSelectionAnswer current_answer = answerList.get(position - 1);

            TextView author = holder.itemView.findViewById(R.id.author);
            TextView date = holder.itemView.findViewById(R.id.dateValue);
            TextView text = holder.itemView.findViewById(R.id.answerText);

            TextView rating = holder.itemView.findViewById(R.id.rating);
            ImageButton like = holder.itemView.findViewById(R.id.like);
            ImageButton dislike = holder.itemView.findViewById(R.id.dislike);

            author.setText(current_answer.getAuthor());                                                     // автор
            date.setText(new SimpleDateFormat("dd.MM.yyyy").format(current_answer.getDate()));      // дата публикации
            text.setText(current_answer.getText());                                                         // текст ответа

            // предлагаемая конфигурация
            CardView showConfiguration = holder.itemView.findViewById(R.id.showConfig);
            showConfiguration.setOnClickListener(view -> {
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragment_container, viewReadyConfigurationFragment.newInstance(current_answer.getConfiguration()));
                ft.commit();
                ft.addToBackStack(null);
            });


            // рейтинг ответа
            String currentUID = FirebaseAuth.getInstance().getUid();

            if (current_answer.isAnswerLiked(currentUID))
            {
                like.setImageResource(R.drawable.ic_like_filled);
            }
            else if (current_answer.isAnswerDisliked(currentUID)) dislike.setImageResource(R.drawable.ic_dislike_filled);

            like.setOnClickListener(view -> {
                if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {

                    if (current_answer.isAnswerDisliked(currentUID))
                    {
                        current_answer.RemoveDislikeUser(currentUID);
                        current_answer.AddLikeUser(currentUID);

                        dislike.setImageResource(R.drawable.ic_dislike_outlined);
                        like.setImageResource(R.drawable.ic_like_filled);
                    }
                    else if (current_answer.isAnswerLiked(currentUID))
                    {
                        current_answer.RemoveLikeUser(currentUID);

                        like.setImageResource(R.drawable.ic_like_outlined);
                    }
                    else
                    {
                        current_answer.AddLikeUser(currentUID);

                        like.setImageResource(R.drawable.ic_like_filled);
                    }
                }
                else Toast.makeText(author.getContext(), "Авторизуйтесь для оценки", Toast.LENGTH_SHORT).show();
            });

            dislike.setOnClickListener(view -> {
                if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                    if (current_answer.isAnswerDisliked(currentUID))
                    {
                        current_answer.RemoveDislikeUser(currentUID);

                        dislike.setImageResource(R.drawable.ic_dislike_outlined);
                    }
                    else if (current_answer.isAnswerLiked(currentUID))
                    {
                        current_answer.RemoveLikeUser(currentUID);
                        current_answer.AddDislikeUser(currentUID);


                        like.setImageResource(R.drawable.ic_like_outlined);
                        dislike.setImageResource(R.drawable.ic_dislike_filled);
                    }
                    else
                    {
                        current_answer.AddDislikeUser(currentUID);

                        dislike.setImageResource(R.drawable.ic_dislike_filled);
                    }
                }
                else Toast.makeText(author.getContext(), "Авторизуйтесь для оценки", Toast.LENGTH_SHORT).show();
            });

            // изменение рейтинга в реальном времени
            current_answer.getRatingReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int ratingNumber = 0;

                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        if (snapshot1.getValue().toString().equals("like")) ratingNumber++;
                        else ratingNumber--;
                    }
                    rating.setText(ratingNumber + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else if (getItemViewType(position) == 2)
        {
            // если мой вопрос
            if (current_question.getAuthor().equals(FirebaseAuth.getInstance().getUid()))
            {
                ImageView noAnswers = holder.itemView.findViewById(R.id.add_answer);
                TextView noAnswersText = holder.itemView.findViewById(R.id.add_answer_text);

                noAnswers.setImageResource(R.drawable.undraw_no_answers);
                noAnswersText.setText("Ответов пока нет...");
            }
            // если чужой вопрос
            else {
                ImageView addAnswer = holder.itemView.findViewById(R.id.add_answer);
                addAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragment_container, AddComponentsSelection.newInstance(current_question.getId(), current_question.getBudget()));
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
                    }

                });
            }
        }
    }

    @Override
    public int getItemCount()
    {
        // если мой вопрос

        if (current_question.getAuthor().equals(FirebaseAuth.getInstance().getUid()))
        {
            if (answerList.size() != 0)
                return answerList.size() + 1;
            return 2;
        }

        // если чужой вопрос
        return answerList.size() + 2;
    }
}
