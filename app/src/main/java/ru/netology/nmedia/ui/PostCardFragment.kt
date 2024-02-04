package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R

class PostCardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ru.netology.nmedia.databinding.PostCardBinding.inflate(
            inflater,
            container,
            false
        )
        val postId = arguments?.getString("id")
        if (postId.isNullOrBlank()) {
            Snackbar.make(
                binding.root, R.string.error_empty_content,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            ).show()
        }

        //arguments?.getString("content")?.let(binding.edPostText::setText)
        binding.tvTitlepost.setText(postId)

        return binding.root
    }
}