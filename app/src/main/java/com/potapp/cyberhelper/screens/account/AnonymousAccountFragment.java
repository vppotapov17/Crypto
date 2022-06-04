package com.potapp.cyberhelper.screens.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.potapp.cyberhelper.R;

public class AnonymousAccountFragment extends Fragment {

    public AnonymousAccountFragment() {}

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

        LinearLayout loginLayout = getActivity().findViewById(R.id.login_layout);

        loginLayout.setClickable(true);
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_top_open, R.animator.fade_close);
                ft.replace(R.id.fragment_container, LoginFragment.newInstance());
                ft.commit();
            }
        });
    }

}
