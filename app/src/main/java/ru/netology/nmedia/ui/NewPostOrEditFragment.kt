package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewOrEditPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostOrEditFragment : Fragment() {


    private val model: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewOrEditPostBinding.inflate(
            inflater,
            container,
            false
        )

        val text = arguments?.getString("content")
        val postId = arguments?.getLong("post_id") ?: 0L
        if (postId != 0L && text.isNullOrBlank()) {
            Snackbar.make(
                binding.root, R.string.error_empty_content,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            ).show()
        }
        binding.postCardFragment.etTextpost.setText(text)
        binding.postCardFragment.etTextpost.requestFocus()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (postId != 0L) {
                model.cancelEditing()
            }
            findNavController().navigateUp()
        }
        callback.isEnabled = true

        with(binding.postCardFragment) {
            ibOk.visibility = View.VISIBLE
            ibCancel.visibility = View.VISIBLE
            etTextpost.visibility = View.VISIBLE
            etUrlVideo.visibility = View.VISIBLE
            etUrlVideo.setHint(getString(R.string.hint_url_video_input))

            ibLiked.visibility = View.GONE
            ibLooked.visibility = View.GONE
            ibShared.visibility = View.GONE
            ivVideoPrew.visibility = View.GONE
            ivVideoPlay.visibility = View.GONE
            etTextpost.setHint(getString(R.string.hint_text_input))
        }

        binding.postCardFragment.ibOk.setOnClickListener {

            with(binding.postCardFragment) {

                if (etTextpost.text.isBlank()) {

                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.not_empty_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                model.changeContent(etTextpost.text.toString())
                model.save()
                it.visibility = View.GONE
                binding.postCardFragment.ibCancel.visibility = View.GONE
            }

            AndroidUtils.hideKeyboard(requireView())
                model.postCreated.observe(viewLifecycleOwner) {
                    model.loadPosts()
                    findNavController().navigateUp()
                }

        }

        binding.postCardFragment.ibCancel.setOnClickListener {
            model.cancelEditing()
            findNavController().navigateUp()

        }
        return binding.root
    }
}