package com.forumbelajar.gasik.forumbelajar;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by obitogan on 6/7/2016.
 */
public class FragRegister extends Fragment {
    Communicator comm;
    Firebase accountRef;
    String vUsername,vPassword,vIdLPIA,vTempatLahir,vNoTelp,vAlamat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_register, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Register");

        final EditText iTanggalLahir = (EditText) getActivity().findViewById(R.id.tanggal_lahir);
        iTanggalLahir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Calendar mcurrentDate= Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        iTanggalLahir.setText(""+selectedday+"/"+selectedmonth+"/"+selectedyear);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        Button register = (Button) getActivity().findViewById(R.id.submit_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean saveData() {
        Firebase.setAndroidContext(this.getActivity());
        accountRef = new Firebase("https://forum-belajar.firebaseio.com/accounts");

        EditText iUsername = (EditText) getActivity().findViewById(R.id.username);
        EditText iPassword = (EditText) getActivity().findViewById(R.id.password);
        EditText iIdLPIA = (EditText) getActivity().findViewById(R.id.no_id);
        EditText iTempatLahir = (EditText) getActivity().findViewById(R.id.tempat_lahir);
        EditText iNoTelp = (EditText) getActivity().findViewById(R.id.no_telp);
        EditText iAlamat = (EditText) getActivity().findViewById(R.id.alamat);

        vUsername = iUsername.getText().toString();
        vPassword = iPassword.getText().toString();
        vIdLPIA = iIdLPIA.getText().toString();
        vTempatLahir = iTempatLahir.getText().toString();
        vNoTelp = iNoTelp.getText().toString();
        vAlamat = iAlamat.getText().toString();
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
        if(vIdLPIA.equals("")){
            Toast.makeText(getActivity(), "Please input Id LPIA", Toast.LENGTH_SHORT).show();
            iIdLPIA.requestFocus();
            return false;
        }

        accountRef.child(vUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Firebase accountData = accountRef.child(vUsername);
                    accountData.child("password").setValue(vPassword);
                    accountData.child("id_lpia").setValue(vIdLPIA);
                    accountData.child("tempat_lahir").setValue(vTempatLahir);
                    accountData.child("no_telp").setValue(vNoTelp);
                    accountData.child("alamat").setValue(vAlamat);
                    Toast.makeText(getActivity(), "Success register account", Toast.LENGTH_SHORT).show();
                    comm = (Communicator) getActivity();
                    comm.respond("login_page");
                }else {
                    Toast.makeText(getActivity(), "Username "+dataSnapshot.getKey().toString()+" already register, please use another one!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        return false;
    }
}
