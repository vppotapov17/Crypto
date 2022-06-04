package com.potapp.cyberhelper.screens.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.yandex.mobile.ads.banner.BannerAdView;


public class AccountFragment extends Fragment {

    BannerAdView banner;

    public AccountFragment() {

    }

    public static AccountFragment newInstance() {
       return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                banner.setAdUnitId("R-M-1648144-3");
//                banner.setAdSize(AdSize.BANNER_320x50);
//
//                banner.setBannerAdEventListener(new BannerAdEventListener() {
//                    @Override
//                    public void onAdLoaded() {
//                        Log.d("YandexMobileAds", "Ad Loaded");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
//                        Log.d("YandexMobileAds", "Ad Failed");
//                        Log.d("YandexMobileAds", adRequestError.getDescription());
//                        Log.d("YandexMobileAds", adRequestError.getCode()+"");
//                    }
//
//                    @Override
//                    public void onAdClicked() {
//
//                    }
//
//                    @Override
//                    public void onLeftApplication() {
//
//                    }
//
//                    @Override
//                    public void onReturnedToApplication() {
//
//                    }
//
//                    @Override
//                    public void onImpression(@Nullable ImpressionData impressionData) {
//
//                    }
//                });
//
//                banner.loadAd(new AdRequest.Builder().build());
//            }
//        }, 300);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_user_auth, container, false);
    }

    @Override

    public void onStart()
    {
        super.onStart();

        //MaterialButton signOut = getActivity().findViewById(R.id.signout_button);                   // кнопка выхода из аккаунта
        TextView username = getActivity().findViewById(R.id.username);
        TextView userRating = getActivity().findViewById(R.id.rating);

        username.setText(MainActivity.current_user.getName());
        userRating.setText(MainActivity.current_user.getRating() + "");

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

        banner = getActivity().findViewById(R.id.banner);




    }


}