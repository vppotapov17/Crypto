package com.potapp.cyberhelper;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
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

        ft.setCustomAnimations(R.anim.fade_open, R.anim.fade_close);
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

        BottomNavigationView bn = findViewById(R.id.navigation_bar);


        NavHostFragment fragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        NavController navController = fragment.getNavController();

        NavigationUI.setupWithNavController(bn, navController);

        MobileAds.initialize(this, new InitializationListener() {
            @Override
            public void onInitializationCompleted() {
                Log.d("YandexMobileAds", "SDK initialized");
            }
        });

        current_user = (User)getIntent().getSerializableExtra("current_user");


    }


    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting())return true;
        return false;
    }

}

