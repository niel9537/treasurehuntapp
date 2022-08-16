package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;

public class ActivityLogin extends AppCompatActivity implements CallbackFragment {
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragment();
    }

    public void addFragment(){
        fragment = new FragmentLogin();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainerView,fragment);
        fragmentTransaction.commit();
    }

    public void replaceFragment(){
        fragment = new FragmentRegister();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragmentContainerView,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void changeFragment(){
        replaceFragment();
    }
}
