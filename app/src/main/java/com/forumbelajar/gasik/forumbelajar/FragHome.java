package com.forumbelajar.gasik.forumbelajar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gasik on 6/8/2016.
 */
public class FragHome extends Fragment {
    Communicator comm;
    Firebase accountRef;
    JSONArray jsonArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Firebase.setAndroidContext(this.getActivity());
        accountRef = new Firebase("https://forum-belajar.firebaseio.com/questions/");
        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getActivity(), "Data kosong", Toast.LENGTH_SHORT).show();
                }else {

//                    String jsonString = "["+dataSnapshot.getValue().toString()+"]";
//                    Toast.makeText(getActivity(), jsonString.toString(), Toast.LENGTH_LONG).show();
//                    try {
//                        jsonArray = new JSONArray(jsonString);
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject json_obj = jsonArray.getJSONObject(i);
//                            Toast.makeText(getActivity(), json_obj.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getActivity(), "Error!"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
//                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button add_question = (Button) getActivity().findViewById(R.id.add_question);
        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comm = (Communicator) getActivity();
                comm.goTo("AddquestionActivity");
            }
        });
    }
}
