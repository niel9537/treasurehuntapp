package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestRegister;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.util.LoadingDialogBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentForgotPassword extends Fragment {
    ImageButton back;
    EditText emailInput;
    Button send;
    LoadingDialogBar loadingDialogBar;
    AlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password,container,false);
        loadingDialogBar = new LoadingDialogBar(getActivity());
        back = view.findViewById(R.id.back_forgot);
        emailInput = view.findViewById(R.id.email_input_forgot);
        send = view.findViewById(R.id.send_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FragmentLogin();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                send.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                send.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow));
                emailInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                send.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow));
                emailInput();
            }
        });
        return view;
    }

    private void emailInput() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!emailInput.getText().toString().isEmpty()){


                }
            }
        });
    }


}
