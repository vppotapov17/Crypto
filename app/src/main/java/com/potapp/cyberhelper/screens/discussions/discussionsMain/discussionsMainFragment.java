package com.potapp.cyberhelper.screens.discussions.discussionsMain;

import android.graphics.Color;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.myQuestionsAdapter;
import com.potapp.cyberhelper.adapters.DiscussionsAdapters.otherQuestionsAdapter;
import com.potapp.cyberhelper.models.questions.Question;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.template.HorizontalOffset;
import com.yandex.mobile.ads.nativeads.template.NativeBannerView;
import com.yandex.mobile.ads.nativeads.template.appearance.BannerAppearance;
import com.yandex.mobile.ads.nativeads.template.appearance.ButtonAppearance;
import com.yandex.mobile.ads.nativeads.template.appearance.NativeTemplateAppearance;
import com.yandex.mobile.ads.nativeads.template.appearance.TextAppearance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class discussionsMainFragment extends Fragment {

    private BannerAdView mBannerAdView;

    discussionsMainViewModel vm;

    private List<Question> myQuestionList;
    private List<Question> otherQuestionList;

    public discussionsMainFragment() {

    }

    public static discussionsMainFragment newInstance() {
      return new discussionsMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = ViewModelProviders.of(this).get(discussionsMainViewModel.class);
        myQuestionList = new ArrayList<>();
        otherQuestionList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discussions_main, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // задать вопрос

        ImageView askQuestionImage = getActivity().findViewById(R.id.askQuestionImage);
        askQuestionImage.setOnClickListener(view->{
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.fade_open, R.animator.fade_close);
            ft.replace(R.id.fragment_container, vm.OnAskQuestionImageFragment());
            ft.addToBackStack(null);
            ft.commit();
        });




        // обработка списка моих вопросов

        RecyclerView myQuestionsRV = getActivity().findViewById(R.id.myQuestionsRV);                // инициализация RecyclerView
        ShimmerFrameLayout myQuestionsShimmer = getActivity().findViewById(R.id.shimmer);           // shimmer-layout
        TextView myQuestionsTitle = getActivity().findViewById(R.id.myQuestionsTitle);              // заголовок


        // список моих вопросов

        // если аккаунт анонимный, скрываем список с моими вопросами
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
        {
            myQuestionsTitle.setVisibility(View.GONE);
            myQuestionsRV.setVisibility(View.GONE);
            myQuestionsShimmer.setVisibility(View.GONE);
        }
        // если пользователь авторизован, подписываемся на livedata со списком моих вопросов
        else {
            myQuestionsRV.setVisibility(View.GONE);
            myQuestionsShimmer.startShimmerAnimation();


            vm.getMyQuestionsLiveData().observe(this, s->{

                // очистка списка и заполнение его обновленными элементами
                myQuestionList.clear();
                myQuestionList.addAll(s);

                // если список вопросов пуст, скрываем список с моими вопросами
                if (myQuestionList.size() == 0)
                {
                    myQuestionsTitle.setVisibility(View.GONE);
                    myQuestionsRV.setVisibility(View.GONE);
                    myQuestionsShimmer.setVisibility(View.GONE);
                }
                // в списке есть вопросы
                else
                {
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(RecyclerView.HORIZONTAL);
                    myQuestionsRV.setLayoutManager(llm);                                                        // установка LayoutManager

                    SnapHelper snapHelper = new LinearSnapHelper();
                    myQuestionsRV.setOnFlingListener(null);

                    snapHelper.attachToRecyclerView(myQuestionsRV);

                    if (myQuestionsRV.getAdapter() == null)
                        myQuestionsRV.setAdapter(new myQuestionsAdapter(myQuestionList, getFragmentManager()));

                    else myQuestionsRV.getAdapter().notifyDataSetChanged();


                    myQuestionsTitle.setVisibility(View.VISIBLE);
                    myQuestionsRV.setVisibility(View.VISIBLE);
                    myQuestionsShimmer.stopShimmerAnimation();
                    myQuestionsShimmer.setVisibility(View.INVISIBLE);
                }

            });
        }


        // обработка списка чужих вопросов

        RecyclerView otherQuestionsRV = getActivity().findViewById(R.id.otherQuestionsRV);
        ShimmerFrameLayout otherQuestionsShimmer = getActivity().findViewById(R.id.shimmer2);
        TextView otherQuestionsTitle = getActivity().findViewById(R.id.otherQuestionsTitle);

        otherQuestionsRV.setVisibility(View.GONE);
        otherQuestionsShimmer.startShimmerAnimation();


        vm.getOtherQuestionsLiveData().observe(this, s->{
            otherQuestionList.clear();
            otherQuestionList.addAll(s);

            // список вопросов пуст
            if (otherQuestionList.size() == 0)
            {
                otherQuestionsTitle.setVisibility(View.GONE);
                otherQuestionsRV.setVisibility(View.GONE);
                otherQuestionsShimmer.setVisibility(View.GONE);
            }
            // в списке есть вопросы
            else {
                // LayoutManager
                LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
                llm2.setOrientation(RecyclerView.HORIZONTAL);
                otherQuestionsRV.setLayoutManager(llm2);

                // SnapHelper
                SnapHelper snapHelper2 = new LinearSnapHelper();
                otherQuestionsRV.setOnFlingListener(null);
                snapHelper2.attachToRecyclerView(otherQuestionsRV);

                if (otherQuestionsRV.getAdapter() == null)
                    otherQuestionsRV.setAdapter(new otherQuestionsAdapter(otherQuestionList, getFragmentManager()));
                else otherQuestionsRV.getAdapter().notifyDataSetChanged();

                otherQuestionsTitle.setVisibility(View.VISIBLE);
                otherQuestionsRV.setVisibility(View.VISIBLE);
                otherQuestionsShimmer.stopShimmerAnimation();
                otherQuestionsShimmer.setVisibility(View.INVISIBLE);
            }
        });
    }
}