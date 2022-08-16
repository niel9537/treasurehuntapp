package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.requestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.requestRegister;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRegister extends Fragment {
    ImageButton back;
    EditText nameInput, emailInput,passInput,confirmInput;
    Button regisButton;
    CallbackFragment callbackFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration,container,false);
        back = view.findViewById(R.id.back_register);
        nameInput = view.findViewById(R.id.name_input_regis);
        emailInput = view.findViewById(R.id.email_input_regis);
        confirmInput = view.findViewById(R.id.confirm_pass_input_regis);
        passInput = view.findViewById(R.id.pass_input_regis);
        regisButton = view.findViewById(R.id.regis_button);


        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }
        });
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }
        });
        passInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }
        });
        confirmInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                regisButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            }
        });
        regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        return view;
    }


    private void register() {
        if(!nameInput.getText().toString().isEmpty() && !emailInput.getText().toString().isEmpty() && !confirmInput.getText().toString().isEmpty() && !passInput.getText().toString().isEmpty()){
            if(confirmInput.getText().toString().equals(passInput.getText().toString())){
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<LoginModel> regisCall = apiInterface.registration(new requestRegister(
                        emailInput.getText().toString(),
                        passInput.getText().toString(),
                        nameInput.getText().toString(),
                        "",
                        ""));
                regisCall.enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if(response.isSuccessful()){
                            Log.d("Register",""+response.body().getMessage().toString());
                            startActivity(new Intent(getActivity(),ActivityLogin.class));
                            Toast.makeText(getActivity(), "Menambah user berhasil!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),"Gagal menambah user! "+response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        Toast.makeText(getActivity(),"Gagal  Registrasi : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(getActivity(), "Password dan Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Masukkan data yang diperlukan!", Toast.LENGTH_SHORT).show();
        }

    }
    public void setCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
    public void getCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
