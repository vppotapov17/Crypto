package com.potapp.cyberhelper.screens.account.AuthorizedUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.potapp.cyberhelper.R;
import com.yandex.mobile.ads.banner.BannerAdView;


public class AuthorizedUserFragment extends Fragment {

    AuthorizedUserViewModel vm;

    public AuthorizedUserFragment() {

    }

    public static AuthorizedUserFragment newInstance() {
       return new AuthorizedUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(this).get(AuthorizedUserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_user_auth, container, false);
    }

    @Override

    public void onResume()
    {
        super.onResume();

        // элементы интерфейса

        // основной контент
        ScrollView scrollView = getActivity().findViewById(R.id.scrollView);

        //ProgressBar
        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);


        // элементы основного контента
        TextView username = getActivity().findViewById(R.id.username);
        TextView userRating = getActivity().findViewById(R.id.rating);

        vm.getCurrentUserLiveData().observe(this, user -> {

            if (user != null)
            {
                // видимость элементов
                progressBar.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);

                username.setText(user.getName());
                userRating.setText(user.getRating() + "");
            }
            else{
                Toast.makeText(getContext(), "Данные не загрузились", Toast.LENGTH_SHORT).show();
            }
        });

        //MaterialButton signOut = getActivity().findViewById(R.id.signout_button);                   // кнопка выхода из аккаунта





//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();                                               // выходим из аккаунта
//                FirebaseAuth.getInstance().signInAnonymously();                                     // авторизуемся как анонимный пользователь
//                MainActivity.current_user = null;
//
//                // через 1 с запускаем LoginFragment
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.replace(R.id.fragment_container, LoginFragment.newInstance()).commit();
//                        } catch (Exception e)
//                        {}
//                    }
//                }, 1000);
//            }
//        });




    }


}