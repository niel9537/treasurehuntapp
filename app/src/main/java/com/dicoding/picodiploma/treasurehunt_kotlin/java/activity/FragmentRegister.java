package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
import androidx.navigation.Navigation;


import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestRegister;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.util.LoadingDialogBar;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRegister extends Fragment {
    ImageButton back;
    EditText nameInput, emailInput,passInput,confirmInput;
    Button regisButton;
    CallbackFragment callbackFragment;
    LoadingDialogBar loadingDialogBar;
    AlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration,container,false);
        loadingDialogBar = new LoadingDialogBar(getActivity());
        back = view.findViewById(R.id.back_register);
        nameInput = view.findViewById(R.id.name_input_regis);
        emailInput = view.findViewById(R.id.email_input_regis);
        confirmInput = view.findViewById(R.id.confirm_pass_input_regis);
        passInput = view.findViewById(R.id.pass_input_regis);
        regisButton = view.findViewById(R.id.regis_button);

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

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private void register() {
        if(validEmail(emailInput.getText().toString())) {
            if (!nameInput.getText().toString().isEmpty() && !emailInput.getText().toString().isEmpty() && !confirmInput.getText().toString().isEmpty() && !passInput.getText().toString().isEmpty()) {
                if (confirmInput.getText().toString().equals(passInput.getText().toString())) {
                    ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                    Call<LoginModel> regisCall = apiInterface.registration(new RequestRegister(
                            emailInput.getText().toString(),
                            passInput.getText().toString(),
                            nameInput.getText().toString(),
                            "",
                            ""));
                    regisCall.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            if (response.isSuccessful()) {
                                Log.d("Register", "" + response.body().getMessage().toString());
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                                View mView = getActivity().getLayoutInflater().inflate(R.layout.registration_success_dialog_layout, null);
                                mBuilder.setView(mView);
                                Button OK = (Button) mView.findViewById(R.id.dialogOK_button);

                                OK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getActivity(), ActivityLogin.class));
                                    }
                                });

                                dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();


                                //Toast.makeText(getActivity(), "Menambah user berhasil!", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                                View mView = getActivity().getLayoutInflater().inflate(R.layout.registration_failed_dialog_layout, null);
                                mBuilder.setView(mView);
                                Button OK = (Button) mView.findViewById(R.id.dialogOK_button);

                                OK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog = mBuilder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                //Toast.makeText(getActivity(),"Gagal menambah user! "+response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            Toast.makeText(getActivity(), "Gagal  Registrasi : " + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Password dan Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Masukkan data yang diperlukan!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Format Email kurang tepat!", Toast.LENGTH_SHORT).show();
        }

    }
    public void setCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
    public void getCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
