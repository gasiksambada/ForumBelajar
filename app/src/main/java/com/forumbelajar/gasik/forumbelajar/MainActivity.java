package com.forumbelajar.gasik.forumbelajar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager manager;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().setHomeButtonEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_main);
        manager = getFragmentManager();

        FragStart frag_start = new FragStart();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, frag_start, "start");
        transaction.addToBackStack("addstart");
        transaction.commit();
    }

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
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void createSession(String key, String Value) {
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, Value);
        editor.commit();
    }

    @Override
    public String getSession() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String vUsername = sharedpreferences.getString("username", "");
        return vUsername;
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

    public boolean loading(Boolean status) {
        ProgressDialog progress = new ProgressDialog(this);
        if(status){
            progress.setCancelable(false);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        }else{
            progress.dismiss();
        }

        return false;
    }
}
