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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.util.LoadingDialogBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends Fragment{
    Button login, fb, google;
    TextView register;
    EditText emailInput,passInput;
    CallbackFragment callbackFragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_LOGIN = "key_login";
    LoadingDialogBar loadingDialogBar;
    public static String PREFS_NAME = "MyPrefsFile";
    @Override
    public void onAttach(@NonNull Context context) {
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        login = view.findViewById(R.id.logins_button);
        register = view.findViewById(R.id.register_login);
        emailInput = view.findViewById(R.id.email_input_login);
        passInput = view.findViewById(R.id.pass_input_login);
        google = view.findViewById(R.id.button);
        fb = view.findViewById(R.id.button_fb);
        google.setVisibility(View.INVISIBLE);
        fb.setVisibility(View.INVISIBLE);
        //"autoLogin" is a unique string to identify the instance of this shared preference
        sharedPreferences = getActivity().getSharedPreferences(KEY_LOGIN, Context.MODE_PRIVATE);
        int j = sharedPreferences.getInt(KEY_LOGIN, Config.KEEP_LOGIN);

        //Default is 0 so autologin is disabled
        if(j > 0){
            Intent activity = new Intent(getActivity(), ActivityHome.class);
            startActivity(activity);
        }
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FragmentRegister();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }


    private void login() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<LoginModel> loginCall = apiInterface.login(new RequestLogin(emailInput.getText().toString(),passInput.getText().toString()));
                loginCall.enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if(response.isSuccessful()){
                            //editor.putString(KEY_USERNAME,emailInput.getText().toString());
                            editor.putBoolean("hasLoggedIn",true);
                            editor.putInt(KEY_LOGIN,1);
                            editor.putString(KEY_TOKEN,"Bearer "+response.body().getData().getAccessToken().toString());
                            editor.apply();

                            Log.d("Token", "Bearer : " + response.body().getData().getAccessToken().toString());
                            startActivity(new Intent(getActivity(),ActivityHome.class));
                        }else{
                            Toast.makeText(getActivity(),"Email dan Password salah!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        Toast.makeText(getActivity(),"Gagal Login : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    public void setCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
    public void getCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
