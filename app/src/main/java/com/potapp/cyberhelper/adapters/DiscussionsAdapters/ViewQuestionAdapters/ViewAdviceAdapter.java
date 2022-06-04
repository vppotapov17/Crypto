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

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.configurator.viewReadyConfiguration.viewReadyConfigurationFragment;
import com.potapp.cyberhelper.data.models.answers.AdviceAnswer;
import com.potapp.cyberhelper.screens.discussions.addAnswer.AddAdvice;
import com.potapp.cyberhelper.data.models.questions.AdviceQuestion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class ViewAdviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AdviceAnswer> answerList;
    private AdviceQuestion current_question;
    private FragmentManager fm;


    public ViewAdviceAdapter(List<AdviceAnswer> answerList, AdviceQuestion current_question, FragmentManager fm)
    {
        this.answerList = answerList;
        this.current_question = current_question;
        this.fm = fm;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0) return 0;
        if (position == getItemCount() - 1) return 2;
        return 1;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View current_view = null;

        switch (viewType)
        {
            case 0: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_advice_header, parent, false); break;
            case 1: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussions_rv_answer_advice, parent, false); break;
            case 2: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_footer, parent, false); break;
        }

        return new RecyclerView.ViewHolder(current_view){};
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position){
        //holder.setIsRecyclable(false);

        if (getItemViewType(position) == 0)
        {
            // характеристики вопроса
            TextView authorValue = holder.itemView.findViewById(R.id.authorValue);
            TextView dateValue = holder.itemView.findViewById(R.id.dateValue);
            TextView categoryValue = holder.itemView.findViewById(R.id.categoryValue);
            TextView text = holder.itemView.findViewById(R.id.questionText);

            CardView cardView = holder.itemView.findViewById(R.id.cardview);

            authorValue.setText(current_question.getAuthor());
            dateValue.setText(new SimpleDateFormat("dd.MM.yyyy").format(current_question.getDate()));
            categoryValue.setText("Оценка и советы");
            text.setText(current_question.getText());

            // характеристики конфигурации
            TextView configName = holder.itemView.findViewById(R.id.name);
            TextView cpuValue = holder.itemView.findViewById(R.id.spec1_value);
            TextView gpuValue = holder.itemView.findViewById(R.id.spec2_value);
            TextView ozuValue = holder.itemView.findViewById(R.id.spec3_value);
            TextView configPrice = holder.itemView.findViewById(R.id.fullPrice);


            configName.setText(current_question.getConfiguration().name);

            //cpuValue.setText(current_question.getConfiguration().mCpu.getModel());
            //gpuValue.setText(current_question.getConfiguration().mGpu.getModel());

            //Ozu OZU = current_question.getConfiguration().mOzu;
            //OZU.setItem_quantity(1);// это убрать потом
            //ozuValue.setText(OZU.getItem_quantity() + "x" + OZU.getCapacity() / OZU.getItem_quantity());

            configPrice.setText(current_question.getConfiguration().getFullPrice() + "");

            // нажатие на CardView
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.replace(R.id.fragment_container, viewReadyConfigurationFragment.newInstance(current_question.getConfiguration())).commit();
                }
            });
        }
        else if (getItemViewType(position) == 1)
        {
            AdviceAnswer current_answer = answerList.get(position - 1);

            TextView author = holder.itemView.findViewById(R.id.author);
            TextView date = holder.itemView.findViewById(R.id.dateTitle);
            TextView text = holder.itemView.findViewById(R.id.answerText);
            TextView grade =  holder.itemView.findViewById(R.id.grade);
            ImageView gradeImage = holder.itemView.findViewById(R.id.gradeImage);

            TextView rating = holder.itemView.findViewById(R.id.rating);
            ImageButton like = holder.itemView.findViewById(R.id.like);
            ImageButton dislike = holder.itemView.findViewById(R.id.dislike);

            author.setText(current_answer.getAuthor());                                                     // автор
            date.setText(new SimpleDateFormat("dd.MM.yyyy").format(current_answer.getDate()));      // дата публикации
            text.setText(current_answer.getText());                                                         // текст ответа


            // оценка конфигурации
            switch (current_answer.getAdvice())
            {
                case 0:
                    grade.setText("Плохо");
                    gradeImage.setImageResource(R.drawable.ic_sad);
                    break;
                case 1:
                    grade.setText("Нормально");
                    gradeImage.setImageResource(R.drawable.ic_neutral);
                    break;
                case 2:
                    grade.setText("Отлично");
                    gradeImage.setImageResource(R.drawable.ic_happy);
                    break;
            }

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
            ImageView addAnswer = holder.itemView.findViewById(R.id.add_answer);
            addAnswer.setOnClickListener(view -> {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, AddAdvice.newInstance(current_question.getId()));
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return answerList.size() + 2;
    }

}
