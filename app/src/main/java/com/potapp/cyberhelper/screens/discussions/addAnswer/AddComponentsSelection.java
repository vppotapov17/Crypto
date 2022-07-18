package com.potapp.cyberhelper.screens.discussions.addAnswer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.models.components.Ozu;
import com.potapp.cyberhelper.screens.configurator.creatingConfiguration.creatingConfigurationFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddComponentsSelection extends Fragment {

    private static String commentText;

    private long questionID;                                                                        // ID вопроса, на который дается ответ
    private int budgetValue;                                                                        // бюджет сборки
    private Configuration suggested_configuration;                                                  // рекомендуемая конфигурация

    public AddComponentsSelection(){}

    public static AddComponentsSelection newInstance(long ID, int budget){
        AddComponentsSelection fragment = new AddComponentsSelection();

        Bundle bundle = new Bundle();
        bundle.putLong("questionID", ID);
        bundle.putInt("budget", budget);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        questionID = getArguments().getLong("questionID");
        budgetValue = getArguments().getInt("budget");
        suggested_configuration = new Configuration();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (suggested_configuration.isReady) return inflater.inflate(R.layout.discussions_add_compsel2, container, false);
        return inflater.inflate(R.layout.discussions_add_compsel1, container, false);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        MaterialButton publication = getActivity().findViewById(R.id.publication);                  // кнопка публикации ответа
        TextView comment_text = getActivity().findViewById(R.id.inputField);                             // комментарий
        comment_text.setMovementMethod(new ScrollingMovementMethod());
        comment_text.setText(commentText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_comment, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText insertComment = view.findViewById(R.id.insertComment);
        MaterialButton done = view.findViewById(R.id.done);
        done.setOnClickListener(view1 -> {
            commentText = insertComment.getText().toString();
            comment_text.setText(commentText);
            dialog.dismiss();
        });

        insertComment.requestFocus();
        comment_text.setClickable(true);
        comment_text.setOnClickListener(view2 -> {
            insertComment.setText(comment_text.getText().toString());
            insertComment.setSelection(insertComment.getText().length());
            dialog.show();
        });


        publication.setOnClickListener(view3 -> {
            if (suggested_configuration.isReady)
            {
                final long[] answerID = new long[1];

                // определение ID ответа
                FirebaseDatabase.getInstance().getReference("Data/Questions/ComponentsSelection/" + questionID + "/Answers/").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // id = количество уже существующих ответов + 1
                        answerID[0] = snapshot.getChildrenCount() + 1;
                        Log.d("ContentValues", "Success! " + snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("ContentValues", "Error!");
                    }
                });

                // добавление ответа в RealtimeDatabase
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data/Questions/ComponentsSelection/" + questionID + "/Answers/" + answerID[0]);

                        HashMap<String, String> map = new HashMap<>();

                        // общие характеристики ответа
                        map.put("QuestionId", questionID + "");
                        map.put("Author", "@" + MainActivity.current_user.getName());
                        map.put("Date", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
                        map.put("Text", comment_text.getText().toString());

                        reference.setValue(map);

                        // предлагаемая конфигурация
                        map = suggested_configuration.convertToFirebase();
                        reference.child("Configuration").setValue(map);

                        try {
                            getFragmentManager().popBackStack();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), "Произошла ошибка!", Toast.LENGTH_SHORT);
                        };
                    }
                }, 300);
            }
            else {}
        });

        if (!suggested_configuration.isReady)
        {
            MaterialButton create = getActivity().findViewById(R.id.create_configuration);          // кнопка создания сборки
            TextView budget = getActivity().findViewById(R.id.budget);

            budget.setText("до " + budgetValue + " ₽");

            create.setOnClickListener(view4 -> {
                TextInputEditText confName = getActivity().findViewById(R.id.confName);

                if (confName.getText().length() == 0)
                    confName.setError("Введите имя!");
                else {
                    suggested_configuration = new Configuration();
                    suggested_configuration.name = confName.getText().toString();


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, creatingConfigurationFragment.newInstance(suggested_configuration));
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                    confName.setText("");
                }
            });
        }
        else
        {
            TextView name = getActivity().findViewById(R.id.name);                                  // имя конфигурации
            TextView price = getActivity().findViewById(R.id.fullPrice);                            // стоимость сборки

            MaterialButton delete_button = getActivity().findViewById(R.id.button1);                // кнопка удаления сборки
            CardView cardView = getActivity().findViewById(R.id.cardview);

            name.setText(suggested_configuration.name);
            price.setText(suggested_configuration.getFullPrice() + "₽");


            delete_button.setOnClickListener(view5 -> {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                suggested_configuration = new Configuration();
                ft.replace(R.id.fragment_container, AddComponentsSelection.newInstance(questionID, budgetValue));
                ft.addToBackStack(null);
                ft.commit();

                getFragmentManager().popBackStack();
            });


            // краткие характеристики сборки
            TextView cpuValue = getActivity().findViewById(R.id.spec1_value);                       // процессор
            TextView gpuValue = getActivity().findViewById(R.id.spec2_value);                       // видеокарта
            TextView ozuValue = getActivity().findViewById(R.id.spec3_value);                       // ОЗУ

            cpuValue.setText(suggested_configuration.mCpu.getModel());
            gpuValue.setText(suggested_configuration.mGpu.getModel());

            Ozu OZU = suggested_configuration.mOzu;
            OZU.setItemQuantity(1);    // убрать потом
            ozuValue.setText((OZU.getItemQuantity() * OZU.getModulesQuantity()) + "x" +
                    (OZU.getCapacity() / OZU.getModulesQuantity() / OZU.getItemQuantity()) + " Гб");
        }
    }
}
