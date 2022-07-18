package com.potapp.cyberhelper.screens.discussions.askQuestion.AskAdvice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.firebase.auth.FirebaseAuth;
import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.models.Configuration;
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

import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;

public class AskAdviceFragment extends Fragment {

    private List<Configuration> configurationList;                      // список готовых конфигураций
    private AskAdviceViewModel vm;

    private AlertDialog noConfigurationsDialog;

    public AskAdviceFragment() {}

    public static AskAdviceFragment newInstance(){
        return new AskAdviceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configurationList = new ArrayList<>();
        vm = ViewModelProviders.of(this).get(AskAdviceViewModel.class);
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

        // элементы интерфейса
        TextView selectConfig = getActivity().findViewById(R.id.select_config);
        RecyclerView rv = getActivity().findViewById(R.id.rv);
        TextView details = getActivity().findViewById(R.id.details);
        TextView commentText = getActivity().findViewById(R.id.commentText);
        MaterialButton publication = getActivity().findViewById(R.id.publication);
        ImageButton back = getActivity().findViewById(R.id.back_button);

        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);

        back.setOnClickListener(view -> getFragmentManager().popBackStack());

        // подписка на livedata со списком готовых сборок
        vm.getConfigurationListLiveData().observe(this, configurations -> {
            // скрываем ProgressBar
            progressBar.setVisibility(View.INVISIBLE);

            // если список пуст, показываем диалог
            if (configurations.isEmpty()){
                getNoConfigurationsDialog().show();
            }
            else {
                // видимость элементов
                selectConfig.setVisibility(View.VISIBLE);
                rv.setVisibility(View.VISIBLE);
                details.setVisibility(View.VISIBLE);
                commentText.setVisibility(View.VISIBLE);
                publication.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.INVISIBLE);

                // заполнение списка
                configurationList.clear();
                configurationList.addAll(configurations);

                // обработка RecyclerView
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(RecyclerView.HORIZONTAL);
                rv.setLayoutManager(llm);

                LinearSnapHelper snapHelper = new LinearSnapHelper();
                rv.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(rv);

                rv.setAdapter(new AdviceConfigsAdapter(configurationList));



                // ввод комментария
                commentText.setMovementMethod(new ScrollingMovementMethod());

                // диалоговое окно

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_comment, null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                EditText insertComment = view.findViewById(R.id.insertComment);
                MaterialButton done = view.findViewById(R.id.done);
                done.setOnClickListener(view1 -> {
                    commentText.setText(insertComment.getText().toString());
                    dialog.dismiss();
                });


                insertComment.requestFocus();
                commentText.setClickable(true);
                commentText.setOnClickListener(view2 -> {
                    insertComment.setText(commentText.getText().toString());
                    insertComment.setSelection(insertComment.getText().length());
                    dialog.show();
                });

                publication.setOnClickListener(view1 -> {
                    vm.publishQuestion(FirebaseAuth.getInstance().getUid(), insertComment.getText().toString(),
                            configurationList.get(llm.findFirstVisibleItemPosition()));
                    getFragmentManager().popBackStack();
                    getFragmentManager().popBackStack();
                });

            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        getNoConfigurationsDialog().dismiss();
    }

    private AlertDialog getNoConfigurationsDialog(){
        if (noConfigurationsDialog == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("У вас нет завершённых сборок");
            builder.setPositiveButton("ОК", (dialogInterface, i) -> {
                getFragmentManager().popBackStack();
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    getFragmentManager().popBackStack();
                }
            });
            noConfigurationsDialog = builder.create();
        }
        return noConfigurationsDialog;
    }
}


