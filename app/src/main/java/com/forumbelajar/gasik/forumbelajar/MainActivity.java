package com.forumbelajar.gasik.forumbelajar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getFragmentManager();

        FragStart frag_start = new FragStart();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, frag_start, "start");
        transaction.commit();
    }

    @Override
    public void respond(String data) {
        if(data.equals("login_page")){
            goto_login();
        }else{

        }
    }

    public void goto_login() {
        FragLogin frag_login = new FragLogin();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment, frag_login, "login");
        transaction.commit();
    }
}
