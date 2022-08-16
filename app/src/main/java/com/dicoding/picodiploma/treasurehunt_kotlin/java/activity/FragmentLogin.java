package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.requestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends Fragment{
    Button login, register;
    EditText emailInput,passInput;
    CallbackFragment callbackFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        login = view.findViewById(R.id.logins_button);
        emailInput = view.findViewById(R.id.email_input_login);
        passInput = view.findViewById(R.id.pass_input_login);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow));

                login();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                login.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow));

                login();
            }
        });
        passInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow));

                login();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                login.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow));

                login();
            }
        });
        return view;
    }


    private void login() {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<LoginModel> loginCall = apiInterface.login(new requestLogin(emailInput.getText().toString(),passInput.getText().toString()));
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if(response.isSuccessful()){
                    Log.d("Token", "Bearer : " + response.body().getData().getAccessToken().toString());
                }else{
                    Toast.makeText(getActivity(),"Gagal Login",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(getActivity(),"Gagal Login",Toast.LENGTH_SHORT);
            }
        });

    }

    public void getCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
