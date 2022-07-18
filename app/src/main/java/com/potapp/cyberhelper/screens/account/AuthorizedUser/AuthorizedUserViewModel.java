package com.potapp.cyberhelper.screens.account.AuthorizedUser;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potapp.cyberhelper.models.User;

public class AuthorizedUserViewModel extends ViewModel {

    private MutableLiveData<User> currentUserLiveData;

    public AuthorizedUserViewModel(){
        currentUserLiveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance().collection("users/").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot documentSnapshot = task.getResult();
               if (documentSnapshot.exists())
               {
                   User user = new User();
                   user.setName(documentSnapshot.getData().get("username").toString());
                   user.setRating(Integer.parseInt(documentSnapshot.getData().get("rating").toString()));

                   currentUserLiveData.postValue(user);

                   Log.d("AAA", "name: " + user.getName());
                   Log.d("AAA", "rating: " + user.getRating());
               }
               else currentUserLiveData.postValue(null);
           }
           else currentUserLiveData.postValue(null);
        });

    }

    public LiveData<User> getCurrentUserLiveData() {
        return currentUserLiveData;
    }
}
