package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.viewmodel.PostViewModel


class EditPostFragment : Fragment() {

    private val model: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ru.netology.nmedia.databinding.FragmentEditpostBinding.inflate(
            inflater,
            container,
            false
        )

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            model.cancelEditing() //так как завязано на LiveData, нужно обновить модель.
            findNavController().navigateUp()
        }
        callback.isEnabled = true


        val text = arguments?.getString("content")
        if (text.isNullOrBlank()) {
            Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE).show()
        }
        //arguments?.getString("content")?.let(binding.edPostText::setText)
        binding.edPostText.setText(text)
        binding.edPostText.requestFocus()

        binding.ibSavepost.setOnClickListener {

            if (binding.edPostText.text.isBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE).show()
            } else {
                model.changeContent(binding.edPostText.text.toString())
            }
            findNavController().navigateUp()
        }
        return binding.root
    }


}