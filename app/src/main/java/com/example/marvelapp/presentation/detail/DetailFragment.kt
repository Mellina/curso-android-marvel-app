package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.marvelapp.databinding.FragmentDetailBinding
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
            imageLoader.load(this, detailViewArgs.imageUrl)
        }

        setSharedElementTransitionOnEnter()

        observeUiState(detailViewArgs)
        observeFavoriteUiState()

        viewModel.getCharacterCategories(detailViewArgs.characterId)

        binding.imageFavoriteIcon.setOnClickListener {
            viewModel.updateFavorite(detailViewArgs)
        }
    }

    private fun observeUiState(detailViewArgs: DetailViewArg) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.flipperDetail.displayedChild = when (uiState) {
                DetailViewModel.UIState.Loading -> {
                    FLIPPER_CHILD_POSITION_LOADING
                }
                is DetailViewModel.UIState.Success -> {
                    binding.recyclerParentDetail.run {
                        setHasFixedSize(true)
                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                    }
                    FLIPPER_CHILD_POSITION_DETAIL
                }

                is DetailViewModel.UIState.Empty -> {
                    FLIPPER_CHILD_POSITION_EMPTY
                }
                is DetailViewModel.UIState.Error -> {
                    binding.includeErrorView.buttonRetry.setOnClickListener {
                        viewModel.getCharacterCategories(detailViewArgs.characterId)
                    }
                    FLIPPER_CHILD_POSITION_ERROR
                }
            }
        }
    }

    private fun observeFavoriteUiState() {
        viewModel.favoriteUiState.observe(viewLifecycleOwner) { favoriteUiState ->
            binding.flipperFavorite.displayedChild = when (favoriteUiState) {
                DetailViewModel.FavoriteUiState.Loading -> FLIPPER_FAVORITE_CHILD_POSITION_LOADING
                is DetailViewModel.FavoriteUiState.FavoriteIcon -> {
                    binding.imageFavoriteIcon.setImageResource(favoriteUiState.icon)
                    FLIPPER_FAVORITE_CHILD_POSITION_SUCCESS
                }
            }
        }
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

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_DETAIL = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3

        private const val FLIPPER_FAVORITE_CHILD_POSITION_SUCCESS = 0
        private const val FLIPPER_FAVORITE_CHILD_POSITION_LOADING = 1
    }
}