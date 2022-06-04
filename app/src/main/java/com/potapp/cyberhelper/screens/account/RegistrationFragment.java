package com.potapp.cyberhelper.screens.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrationFragment extends Fragment {

    public RegistrationFragment()
    {}

    public static RegistrationFragment newInstance()
    {
        return new RegistrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.account_registration, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        ImageButton close_button = getActivity().findViewById(R.id.close_button);                   // кнопка закрытия фрагмента регистрации (крестик)

        final EditText username = getActivity().findViewById(R.id.username);                        // поле ввода имени пользователя
        final EditText email = getActivity().findViewById(R.id.email);                              // поле ввода email
        final EditText password1 = getActivity().findViewById(R.id.password);                       // поле ввода пароля
        final EditText repeatPassword = getActivity().findViewById(R.id.repeat_password);           // поле ввода для повтора пароля


        MaterialButton login_button = getActivity().findViewById(R.id.login_button);                // кнопка для перехода к фрагменту авторизации
        MaterialButton registration_button = getActivity().findViewById(R.id.registration_button);  // кнопка регистрации

        final ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);               // индикация загрузки
        progressBar.setVisibility(View.INVISIBLE);

        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // проверка регистрационных данных на соответствие требованиям
                if (username.getText().length() != 0 && email.getText().length() != 0 && password1.getText().length() >= 6
                        && password1.getText().toString().equals(repeatPassword.getText().toString()))
                {
                    progressBar.setVisibility(View.VISIBLE);

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
                            .addOnCompleteListener(getActivity(), task -> {
                                // если регистрация успешна
                                if (task.isSuccessful())
                                {
                                    progressBar.setVisibility(View.INVISIBLE);                      // убираем индикатор загрузки

                                    // добавляем пользователя в Cloud Firestore
                                    HashMap<String, String> user = new HashMap<>();
                                    user.put("rating", "0");
                                    user.put("username", username.getText().toString());

                                    FirebaseFirestore.getInstance()
                                            .collection("users")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .set(user);


                                    // открываем AccountFragment
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AccountFragment.newInstance()).commit();
                                }
                                // если произошла ошибка, возможны 2 варианта: отсутствует интернет, либо введён некорректный email
                                else
                                {
                                    if (!MainActivity.isOnline(getContext()))
                                        Toast.makeText(getContext(), "Отсутствует интернет-соединение", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(getContext(), "Некорректный email!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                }

                // если данные не соответствуют требованиям, выводятся соответствующие сообщения
                if (username.getText().length() == 0) username.setError("Введите имя!");
                if (email.getText().length() == 0) email.setError("Введите email!");
                if (password1.getText().length() == 0) password1.setError("Введите пароль!");
                if (password1.getText().length() < 6) password1.setError("Не менее 6 знаков!");
                if (repeatPassword.getText().length() == 0) repeatPassword.setError("Повторите пароль!");
                if (!password1.getText().toString().equals(repeatPassword.getText().toString())
                        && password1.getText().length() != 0 && repeatPassword.getText().length() != 0) repeatPassword.setError("Пароли не совпадают!");
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.scale_open, R.animator.scale_close);
                ft.replace(R.id.fragment_container, LoginFragment.newInstance());
                ft.commit();
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.fade_open, R.animator.slide_bottom_close);
                ft.replace(R.id.fragment_container, AnonymousAccountFragment.newInstance());
                ft.commit();
            }
        });
    }
}
