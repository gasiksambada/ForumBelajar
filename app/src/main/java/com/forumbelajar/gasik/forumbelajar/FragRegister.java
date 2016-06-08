package com.forumbelajar.gasik.forumbelajar;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by obitogan on 6/7/2016.
 */
public class FragRegister extends Fragment {

    String firebase_server = getResources().getString(R.string.firebase);
    Firebase mRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_register, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Register");

        Button register = (Button) getActivity().findViewById(R.id.submit_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void saveData() {
        Firebase.setAndroidContext(this.getContext());
        Firebase mRef = new Firebase(firebase_server);
        Firebase usersRef = mRef.child("accounts");

        Map<String, String> account_data = new HashMap<String, String>();
        account_data.put("author", "gracehop");
        account_data.put("title", "Announcing COBOL, a New Programming Language");
        usersRef.push().setValue(account_data);
    }
}
