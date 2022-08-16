package com.dicoding.picodiploma.treasurehunt_kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.dicoding.picodiploma.treasurehunt_kotlin.api.RetrofitClient
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.AuthInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.login.LoginBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {


    private lateinit var sharedPreferences: SharedPreferences // copas ini
    private val preferencesName = "treasureHunt" //copas ini
    private val tokenKey = "key_token" //copas ini

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.actionBar?.hide()

        //copas baris bawah ini
        sharedPreferences = requireActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE) //inisialisasi fitur shared preference

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val emailInput = view.findViewById<EditText>(R.id.email_input_login)
        val passInput = view.findViewById<EditText>(R.id.pass_input_login)
        val login = view.findViewById<Button>(R.id.logins_button)

        emailInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                login.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.login_gray))
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                login.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.yellow))


                login()
            }

            override fun afterTextChanged(s: Editable?) {
                login.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.yellow))

                login()
            }

        })

        passInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                login.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.login_gray))
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                login.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.yellow))


                login()
            }

            override fun afterTextChanged(s: Editable?) {
                login.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.yellow))

                login()
            }

        })

        val register = view.findViewById<TextView>(R.id.register_login)

        register?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        val forgot = view.findViewById<TextView>(R.id.forgot_login)

        forgot.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun login() {
        val emailInput = view?.findViewById<EditText>(R.id.email_input_login)?.text.toString()
        val passInput = view?.findViewById<EditText>(R.id.pass_input_login)?.text.toString()
        val login = view?.findViewById<Button>(R.id.logins_button)

        val viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]//inisialisasi fitur viewmodel

        login?.setOnClickListener {
            if (emailInput.isNotEmpty() && passInput.isNotEmpty()) {

                viewModel.login(emailInput, passInput)

                viewModel.loginResponse().observe(viewLifecycleOwner) {
                    if (it != null){
                        saveTokenUser("Bearer "+it.data?.access_token)
                        Log.d("API-login: ", it.data?.access_token.toString())

                        val intent = Intent(activity, MainActivity::class.java)

                        startActivity(intent)
                        requireActivity().finish()
                    }
                    else {
                        Toast.makeText(activity,"Email dan Password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //copas ini utk save token.. cara manggil token liat di home fragment line 165
    private fun saveTokenUser(token : String) {
        val user: SharedPreferences.Editor = sharedPreferences.edit()

        user.putString(tokenKey, token)
        user.apply()
    }

}