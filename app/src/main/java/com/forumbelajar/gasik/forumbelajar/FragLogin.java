package com.forumbelajar.gasik.forumbelajar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gasik on 6/6/2016.
 */
public class FragLogin extends Fragment {
    Communicator comm;
    Firebase accountRef;
    String vUsername,vPassword,fPassword;
    JSONObject jsonObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Login");

        comm = (Communicator) getActivity();
        Button login = (Button) getActivity().findViewById(R.id.submit_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginData();
                Toast.makeText(getActivity(), "Login success", Toast.LENGTH_SHORT).show();
                comm.createSession("username",vUsername);
                comm.goTo("SecondActivity");
            }
        });
    }

    public boolean loginData() {
        EditText iUsername = (EditText) getActivity().findViewById(R.id.username);
        final EditText iPassword = (EditText) getActivity().findViewById(R.id.password);
        vUsername = iUsername.getText().toString();
        vPassword = iPassword.getText().toString();
        if(vUsername.equals("")){
            Toast.makeText(getActivity(), "Please input username", Toast.LENGTH_SHORT).show();
            iUsername.requestFocus();
            return false;
        }
        if(vPassword.equals("")){
            Toast.makeText(getActivity(), "Please input password", Toast.LENGTH_SHORT).show();
            iPassword.requestFocus();
            return false;
        }

        Firebase.setAndroidContext(this.getActivity());
        accountRef = new Firebase("https://forum-belajar.firebaseio.com/accounts/");
        accountRef.child(vUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getActivity(), "Login failed!", Toast.LENGTH_SHORT).show();
                }else {
                    String jsonString = dataSnapshot.getValue().toString();
                    try {
                        jsonObject = new JSONObject(jsonString);
                        if(jsonObject != null){
                            fPassword = jsonObject.getString("password");
                            if(fPassword.equals(vPassword)){
                                Toast.makeText(getActivity(), "Login success", Toast.LENGTH_SHORT).show();
                                comm.createSession("username",vUsername);
                                comm.goTo("SecondActivity");
                            }else{
                                Toast.makeText(getActivity(), "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        return false;
    }
}
