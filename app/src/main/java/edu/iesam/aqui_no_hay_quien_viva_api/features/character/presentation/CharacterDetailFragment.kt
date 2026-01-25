package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import edu.iesam.aqui_no_hay_quien_viva_api.core.presentation.errors.ErrorAppFactory
import edu.iesam.aqui_no_hay_quien_viva_api.databinding.FragmentCharacterDetailBinding
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModel()
    private val errorFactory by lazy { ErrorAppFactory(requireContext()) }
    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()
        viewModel.loadCharacter(args.characterId)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: CharacterDetailUiState) {
        binding.progressBar.isVisible = state is CharacterDetailUiState.Loading
        binding.detailContent.isVisible = state is CharacterDetailUiState.Success
        binding.errorView.isVisible = state is CharacterDetailUiState.Error

        when (state) {
            is CharacterDetailUiState.Loading -> {
               
            }
            is CharacterDetailUiState.Success -> {
                renderCharacter(state.character)
            }
            is CharacterDetailUiState.Error -> {
                val errorUI = errorFactory.build(state.error) {
                    viewModel.retry(args.characterId)
                }
                binding.errorView.render(errorUI)
            }
        }
    }

    private fun renderCharacter(character: Character) {
        binding.apply {
            characterName.text = character.name
            
            headerSubtitle.setText(edu.iesam.aqui_no_hay_quien_viva_api.R.string.character_detail_details_title)

            headerAvatar.load(character.imageUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(edu.iesam.aqui_no_hay_quien_viva_api.R.drawable.img_unknown_error)
                error(edu.iesam.aqui_no_hay_quien_viva_api.R.drawable.img_unknown_error)
            }

            characterImage.load(character.imageUrl) {
                 crossfade(true)
            }
            chipGroup.removeAllViews()
            if (character.shortname.isNotBlank()) {
                addChip(character.shortname)
            }

            characterFullName.text = buildFullName(character)

            if (character.nickname.isNotBlank()) {
                nicknameLabel.isVisible = true
                characterNickname.isVisible = true
                characterNickname.text = character.nickname
            } else {
                nicknameLabel.isVisible = false
                characterNickname.isVisible = false
            }
            
            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun addChip(text: String) {
        if (text.isBlank()) return
        val chip = com.google.android.material.chip.Chip(requireContext()).apply {
            this.text = text
            isCheckable = false
        }
        binding.chipGroup.addView(chip)
    }

    private fun buildFullName(character: Character): String {
        return listOf(character.name, character.surname, character.secondSurname)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    // Removed buildSurname as it is not used directly anymore

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
