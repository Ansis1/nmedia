package ru.netology.nmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar

class EditMsgDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            ru.netology.nmedia.databinding.ActivityEditMsgDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.let {
/*            if (it.action != Intent.ACTION_SEND){
                return@let
            }*/
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }
                    .show()
                return@let
            }

            binding.edPostText.setText(text)
            binding.edPostText.requestFocus()

        }

        binding.ibSavepost.setOnClickListener {
            val intent = Intent()
            if (binding.edPostText.text.isBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edPostText.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }


    }

}