package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.EditMsgDialog
import ru.netology.nmedia.R
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ru.netology.nmedia.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val newPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }
        val adapter = PostsAdapter({
            viewModel.likeById(it.id) //лайк
        }, {
            viewModel.shareById(it.id, this.applicationContext) // поделиться
        }, {
            viewModel.removeById(it.id) //удалить (popup)
        }, {
            viewModel.setEditedValue(it) //изменить (popup)
        })

        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                binding.editedPrevGroup.visibility = View.GONE
                return@observe
            }

            newPostLauncher.launch(it.content)
            /*   binding.editedTitle.setText(it.author)
               binding.editedPostText.setText(it.content)
               binding.etNewComment.setText(it.content)
               binding.editedPrevGroup.visibility = View.VISIBLE*/


        }


        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        //Сохранение или изменение (действие).
        binding.ibChangeOrAdd.setOnClickListener {
            with(binding.etNewComment) {

                if (text.isNullOrBlank()) {

                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.not_empty_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }

        //Отмена редактирования
        binding.ibCancelEditing.setOnClickListener {
            with(binding) {
                editedPrevGroup.visibility = View.GONE
                etNewComment.setText("")
            }
            viewModel.cancelEditing()
        }
    }

    class EditPostResultContract : ActivityResultContract<String, String?>() {

        override fun createIntent(context: Context, input: String): Intent {
            val intent = Intent(context, EditMsgDialog::class.java)
            intent.putExtra(Intent.EXTRA_TEXT, input)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(Intent.EXTRA_TEXT)
            } else {
                null
            }

    }

}





