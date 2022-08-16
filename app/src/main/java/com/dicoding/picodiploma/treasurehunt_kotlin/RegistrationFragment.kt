package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.dicoding.picodiploma.treasurehunt_kotlin.api.ApiBase
import com.dicoding.picodiploma.treasurehunt_kotlin.api.RetrofitClient
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.AuthInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.registration.RegisterBody
import com.dicoding.picodiploma.treasurehunt_kotlin.data.RegisterUserData
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        val binding = FragmentRegistrationBinding.inflate(layoutInflater)
        val back = view.findViewById<ImageButton>(R.id.back_register)

        back.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        val nameInput = view.findViewById<EditText>(R.id.name_input_regis)
        val emailInput = view.findViewById<EditText>(R.id.email_input_regis)
        val passInput = view.findViewById<EditText>(R.id.pass_input_regis)
        val confirmInput = view.findViewById<EditText>(R.id.confirm_pass_input_regis)
        val regisButton = view.findViewById<Button>(R.id.regis_button)

        val auth = RetrofitClient.init().create(AuthInterface::class.java)

        nameInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.login_gray))
                regisButton.isClickable = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            override fun afterTextChanged(p0: Editable?) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

        })

        emailInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.login_gray))
                regisButton.isClickable = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            override fun afterTextChanged(p0: Editable?) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

        })

        passInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.login_gray))
                regisButton.isClickable = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            override fun afterTextChanged(p0: Editable?) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

        })

        confirmInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.login_gray))
                regisButton.isClickable = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            override fun afterTextChanged(p0: Editable?) {
                regisButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

        })


        //fix
        regisButton.setOnClickListener {

            if (nameInput.text != null && emailInput.text != null && passInput.text != null && confirmInput.text != null){
                if (confirmInput.text.toString() == passInput.text.toString()){
                    GlobalScope.launch {
                        auth.registUser(RegisterBody(emailInput.text.toString(), passInput.text.toString(), nameInput.text.toString(), "", ""))
                    }

                    startActivity(Intent(requireActivity(), MainActivity::class.java))

                    Toast.makeText(activity, "Menambah user berhasil!", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(activity, "Password dan Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                }
            }

            else {
                Toast.makeText(activity, "Masukkan data yang diperlukan!", Toast.LENGTH_SHORT).show()
            }
        }

        // Inflate the layout for this fragment
        return view
    }

}