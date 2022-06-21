package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.GlideImageLoader
import com.example.marvelapp.framework.imageloader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailViewArgs = args.detailViewArg
        binding.imageCharacter.run {
            transitionName = detailViewArgs.name
            imageLoader.load(this, detailViewArgs.imageUrl, R.drawable.ic_img_loading_error)
        }

        setSharedElementTransitionOnEnter()

        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            val log = when(uiState){
                    DetailViewModel.UIState.Loading -> "Loading..."
                    is DetailViewModel.UIState.Success -> uiState.comics.toString()
                    is DetailViewModel.UIState.Error -> "Error"
            }
            Log.d("LOG_STATE", ""+log)
        }

        viewModel.getComics(detailViewArgs.characterId)

    }

    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}