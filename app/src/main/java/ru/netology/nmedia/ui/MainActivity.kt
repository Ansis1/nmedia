package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ru.netology.nmedia.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter({
            viewModel.likeById(it.id) //лайк
        }, {
            viewModel.shareById(it.id) // поделиться
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
            binding.editedTitle.setText(it.author)
            binding.editedPostText.setText(it.content)
            binding.etNewComment.setText(it.content)
            binding.editedPrevGroup.visibility = View.VISIBLE


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


}


