package com.dicoding.picodiploma.treasurehunt_kotlin

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityPlayGameBinding

class PlayGameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlayGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


    }

    private fun shareDialog(){
        val dialogView = View.inflate(this,R.layout.dialog_share, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val share = dialogView.findViewById<Button>(R.id.share_button)
        share.setOnClickListener{
            val intent = packageManager.getLaunchIntentForPackage("com.instagram.android")
            val appUri = Uri.parse("https://instagram.com/_u/user_name")
            val browserUri = Uri.parse("https://instagram.com/_u/user_name")

            try {
                if(intent != null){
                    intent.action = Intent.ACTION_VIEW
                    intent.data = appUri;
                    startActivity(intent);
                }
            } catch (e : Exception){
                startActivity(Intent(Intent.ACTION_VIEW, browserUri))
            }

        }
    }

    private fun cameraDialog(){
        val dialogView = View.inflate(this,R.layout.dialog_kamera, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)


        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val share = dialogView.findViewById<Button>(R.id.buka_camera)
        share.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 200)
        }
    }

    private fun dismissDialog(){
        val builder = AlertDialog.Builder(this)
        val dialog = builder.create()

        dialog.dismiss()
    }
}