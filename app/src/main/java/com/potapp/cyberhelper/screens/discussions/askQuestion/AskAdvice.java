package com.potapp.cyberhelper.screens.discussions.askQuestion;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.AdviceConfigsAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.potapp.cyberhelper.data.room.dbs.DB_CONFIGURATIONS;

public class AskAdvice extends Fragment {

    private List<Configuration> configurationList;

    public AskAdvice() {}

    public static AskAdvice newInstance(){
        return new AskAdvice();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configurationList = new ArrayList<>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Configuration configuration : Room.databaseBuilder(getContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS").build().getMyDao().getConfiguration())
                    if (configuration.isReady) configurationList.add(configuration);
            }
        });
        t.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discussions_ask_advice, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);

        final AdviceConfigsAdapter adapter = new AdviceConfigsAdapter(configurationList);

        final RecyclerView rv = getActivity().findViewById(R.id.rv);
        ProgressBar bar = getActivity().findViewById(R.id.progressBar);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                rv.setLayoutManager(llm);
                rv.setAdapter(adapter);
                bar.setVisibility(View.INVISIBLE);
            }
        }, 700);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        rv.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(rv);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        final TextView comment_text = getActivity().findViewById(R.id.commentText);
        MaterialButton publication = getActivity().findViewById(R.id.publication);

        comment_text.setMovementMethod(new ScrollingMovementMethod());

        // диалоговое окно ввода комментария

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_comment, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText insertComment = view.findViewById(R.id.insertComment);
        MaterialButton done = view.findViewById(R.id.done);
        done.setOnClickListener(view1 -> {
            comment_text.setText(insertComment.getText().toString());
            dialog.dismiss();
        });


        insertComment.requestFocus();
        comment_text.setClickable(true);
        comment_text.setOnClickListener(view2 -> {
            insertComment.setText(comment_text.getText().toString());
            insertComment.setSelection(insertComment.getText().length());
            dialog.show();
        });

        DatabaseReference databaseReference = firebaseDatabase.getReference("Data/Questions/Advice");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                publication.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Configuration current_configuration = configurationList.get(llm.findFirstVisibleItemPosition());

                        DatabaseReference reference = databaseReference.child(snapshot.getChildrenCount() + 1 + "");

                        // добавление в базу информации о вопросе
                        HashMap<String, String> map = new HashMap<>();

                        map.put("Author", MainActivity.current_user.getName());
                        map.put("Date", new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
                        map.put("Text", comment_text.getText().toString());

                        reference.setValue(map);

                        // добавление в базу информации о конфигурации
                        reference.child("Configuration").setValue(current_configuration.convertToFirebase());

                        getFragmentManager().popBackStack();
                        getFragmentManager().popBackStack();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


