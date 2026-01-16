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
        binding.loadingIndicator.isVisible = state is CharacterDetailUiState.Loading
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
            characterAvatar.load(character.imageUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }

            characterName.text = character.shortname.ifEmpty { character.name }
            characterFullName.text = buildFullName(character)

            characterNickname.text = character.nickname.ifEmpty { "-" }
            characterShortname.text = character.shortname.ifEmpty { "-" }
            characterSurname.text = buildSurname(character)
        }
    }

    private fun buildFullName(character: Character): String {
        return listOf(character.name, character.surname, character.secondSurname)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    private fun buildSurname(character: Character): String {
        return listOf(character.surname, character.secondSurname)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifEmpty { "-" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
