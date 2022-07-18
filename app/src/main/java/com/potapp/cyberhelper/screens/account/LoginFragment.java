package com.potapp.cyberhelper.screens.account;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.annotations.NotNull;
import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.potapp.cyberhelper.screens.account.AnonymousUser.AnonymousAccountFragment;
import com.potapp.cyberhelper.screens.account.AuthorizedUser.AuthorizedUserFragment;


public class LoginFragment extends Fragment {

    public LoginFragment()
    {}

    public static LoginFragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.account_login, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        MaterialButton loginButton = getActivity().findViewById(R.id.login_button);                 // кнопка входа
        MaterialButton createAccount = getActivity().findViewById(R.id.create_account);             // кнопка перехода к фрагменту регистрации

        ImageButton close_button = getActivity().findViewById(R.id.close_button);                   // кнопка закрытия фрагмента авторизации (крестик)

        final EditText email = getActivity().findViewById(R.id.email);                              // поле ввода email
        final EditText password = getActivity().findViewById(R.id.password);                        // поле ввода пароля

        final ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);               // индикация загрузки
        progressBar.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // если данные для входа введены
                if (email.getText().length() != 0 && password.getText().length() != 0) {
                    loginButton.setClickable(false);
                    progressBar.setVisibility(View.VISIBLE);

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    // если авторизация успешна, открываем AccountFragment с данными о пользователе
                                    if (task.isSuccessful()) {
                                        // обновляем данные о пользователе, затем через 1 с запускаем AccountFragment (за 1 с данные успеют загрузиться)

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AuthorizedUserFragment.newInstance()).commit();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }, 1000);

                                    }
                                    // если произошла ошибка, то возможны 2 варианта: отсутствие интернета, либо неверные данные для входа
                                    else {
                                        loginButton.setClickable(true);
                                        if (MainActivity.isOnline(getContext()))
                                            Toast.makeText(getContext(), "Неверный email или пароль!", Toast.LENGTH_SHORT).show();
                                        else Toast.makeText(getContext(), "Отсутствует интернет-соединение!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                }

                // если данные для входа не введены
                if (email.getText().length() == 0) email.setError("Введите email!");
                if (password.getText().length() == 0) password.setError("Введите пароль!");
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.scale_open, R.animator.scale_close);
                ft.replace(R.id.fragment_container, RegistrationFragment.newInstance());
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
