package com.forumbelajar.gasik.forumbelajar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        manager = getFragmentManager();

        FragStart frag_start = new FragStart();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, frag_start, "start");
        transaction.addToBackStack("addstart");
        transaction.commit();
    }

//    @Override
//    public boolean onSupportNavigateUp(){
//        finish();
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void respond(String data) {
        if(data.equals("login_page")){
            goto_login();
        }else if (data.equals("register_page")){
            goto_register();
        }
    }

    @Override
    public void goTo(String data) {
        switch (data) {
            case "SecondActivity":
                Log.d("notif","masuk cuy");
//                Intent intent = new Intent(this, SecondActivity.class);
//                startActivity(intent);
                break;
        }
    }

    public void goto_login() {
        FragLogin frag_login = new FragLogin();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment, frag_login, "login");
        transaction.addToBackStack("addlogin");
        transaction.commit();
    }

    public void goto_register() {
        FragRegister frag_register = new FragRegister();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment, frag_register, "register");
        transaction.addToBackStack("addregister");
        transaction.commit();
    }
}
