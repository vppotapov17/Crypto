package com.potapp.cyberhelper.screens.account.AnonymousUser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.account.LoginFragment;
import com.potapp.cyberhelper.screens.account.RegistrationFragment;

public class AnonymousAccountFragment extends Fragment {

    private NavController navController;
    public AnonymousAccountFragment() {}

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);

        navController = NavHostFragment.findNavController(this);

        Log.d("AAA", "OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.account_user_guest, container, false);
    }

    public static AnonymousAccountFragment newInstance()
    {
        return new AnonymousAccountFragment();
    }

    public void onStart()
    {
        super.onStart();

        Log.d("AAA", "OnStart");
        if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            Log.d("AAA", "Перенаправление на авторизованного пользователя");
            NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.accountMain, true).build();
            navController.navigate(R.id.action_accountMain_to_authorizedUserFragment, null, options);
        }

        LinearLayout loginLayout = getView().findViewById(R.id.login_layout);
        MaterialButton create_account = getView().findViewById(R.id.create_account_anonymous);


        Fragment f = this;
        loginLayout.setClickable(true);
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(f).navigate(R.id.action_accountMain_to_loginFragment);
            }
        });

        create_account.setOnClickListener(view -> {
            NavHostFragment.findNavController(f).navigate(R.id.action_accountMain_to_registrationFragment);
        });
    }

}
