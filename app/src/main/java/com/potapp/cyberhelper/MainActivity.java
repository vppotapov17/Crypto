package com.potapp.cyberhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.potapp.cyberhelper.models.User;
import com.potapp.cyberhelper.screens.account.AuthorizedUser.AuthorizedUserFragment;
import com.potapp.cyberhelper.screens.account.AnonymousUser.AnonymousAccountFragment;
import com.potapp.cyberhelper.screens.configurator.configurationList.configurationListFragment;
import com.potapp.cyberhelper.screens.discussions.discussionsMain.discussionsMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.yandex.mobile.ads.common.InitializationListener;
import com.yandex.mobile.ads.common.MobileAds;


public class MainActivity extends AppCompatActivity {

    public static User current_user;


    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        if (fm.findFragmentById(R.id.fragment_container) != null)
            if (fm.findFragmentById(R.id.fragment_container).getClass().getName().equals(fragment.getClass().getCanonicalName())) return;


        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.setCustomAnimations(R.animator.fade_open, R.animator.fade_close);
        ft.replace(R.id.fragment_container, fragment);
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new InitializationListener() {
            @Override
            public void onInitializationCompleted() {
                Log.d("YandexMobileAds", "SDK initialized");
            }
        });

        current_user = (User)getIntent().getSerializableExtra("current_user");

        BottomNavigationView bn = findViewById(R.id.navigation_bar);

        bn.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_configurator:
                    loadFragment(configurationListFragment.newInstance());
                    return true;
//                case R.id.navigation_readyconfigurations:
//                    loadFragment(MainReadyConfigurations.newInstance());
//                    return true;
                case R.id.navigation_questions:
                    loadFragment(discussionsMainFragment.newInstance());
                    return true;
                case R.id.navigation_account:
                    try {
                        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
                            loadFragment(AnonymousAccountFragment.newInstance());
                        else loadFragment(AuthorizedUserFragment.newInstance());
                        return true;
                    }
                    catch (NullPointerException e)
                    {}
                default: return false;
            }
        });

        loadFragment(configurationListFragment.newInstance());

    }

    @Override
    public void onResume(){
        super.onResume();

    }

    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting())return true;
        return false;
    }

}

