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
import edu.iesam.aqui_no_hay_quien_viva_api.core.presentation.errors.ErrorAppFactory
import edu.iesam.aqui_no_hay_quien_viva_api.databinding.FragmentCharacterListBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterListViewModel by viewModel()
    private val errorFactory by lazy { ErrorAppFactory(requireContext()) }

    private val adapter = CharacterAdapter { character ->
        navigateToDetail(character.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.characterList.adapter = adapter
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

    private fun updateUI(state: CharacterListUiState) {
        binding.loadingIndicator.isVisible = state.isLoading
        binding.characterList.isVisible = state.showContent
        binding.emptyStateView.isVisible = state.showEmpty
        binding.errorView.isVisible = state.error != null

        if (state.showContent) {
            adapter.submitList(state.characters)
        }

        state.error?.let { error ->
            val errorUI = errorFactory.build(error) {
                viewModel.retry()
            }
            binding.errorView.render(errorUI)
        }
    }

    private fun navigateToDetail(characterId: String) {
        val action = CharacterListFragmentDirections
            .actionCharacterListFragmentToCharacterDetailFragment(characterId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
