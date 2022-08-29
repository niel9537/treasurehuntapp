package com.dicoding.picodiploma.treasurehunt_kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class AccountFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences // deklarasi fitur shared preference
    private val preferencesName = "treasureHunt" //key shared preference app

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE) //inisialisasi fitur shared preference


        view.findViewById<Button>(R.id.sign_out).setOnClickListener{
            val dialogView = View.inflate(context,R.layout.dialog_sign_out, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val cancel = dialogView.findViewById<Button>(R.id.cancel_sign_out)
            cancel?.setOnClickListener {
                dialog.dismiss()

                //val intent = Intent(dialogView.context, LoginActivity::class.java)
                //startActivity(intent)
            }

            val yesSignOut = dialogView.findViewById<Button>(R.id.yes_sign_out)
            yesSignOut.setOnClickListener {
                logOut()

                val intent = Intent(dialogView.context, LoginActivity::class.java)
                startActivity(intent)
            }

        //Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_loginFragment)

        }

        view.findViewById<TextView>(R.id.txtChangePassword).setOnClickListener{
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)

            //Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }

        view.findViewById<TextView>(R.id.txtAbout).setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<TextView>(R.id.txtTermsCondition).setOnClickListener {
            val intent = Intent(requireContext(), TermsAndConditionsActivity::class.java)
            startActivity(intent)
        }

        val uri = "https://images.unsplash.com/photo-1655874184076-c75fce971b46?ixlib=rb-1.2.1&dl=lance-reis-CsO0RhSdc-I-unsplash.jpg&w=640&q=80&fm=jpg&crop=entropy&cs=tinysrgb"
        view.findViewById<CircleImageView>(R.id.imgProfil).setImageURI(Uri.parse(uri))

        // https://images.unsplash.com/photo-1655874184076-c75fce971b46?ixlib=rb-1.2.1&dl=lance-reis-CsO0RhSdc-I-unsplash.jpg&w=640&q=80&fm=jpg&crop=entropy&cs=tinysrgb
        return view
    }

    private fun logOut(){
        val user : SharedPreferences.Editor = sharedPreferences.edit()

        user.clear() //menghapus semua value yang disimpan di shared preference
        user.apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun signOut(){
        val signOutButton = view?.findViewById<Button>(R.id.sign_out)
        signOutButton?.setOnClickListener {

                val dialogView = View.inflate(context,R.layout.dialog_sign_out, null)
                val builder = AlertDialog.Builder(context)
                builder.setView(dialogView)

                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val cancel = dialogView.findViewById<Button>(R.id.cancel_sign_out)
            cancel?.setOnClickListener {
                    dialog.dismiss()

                    //val intent = Intent(dialogView.context, LoginActivity::class.java)
                    //startActivity(intent)
                }

            val yesSignOut = dialogView.findViewById<Button>(R.id.yes_sign_out)
            yesSignOut.setOnClickListener {
                logOut()

                val intent = Intent(dialogView.context, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }
}