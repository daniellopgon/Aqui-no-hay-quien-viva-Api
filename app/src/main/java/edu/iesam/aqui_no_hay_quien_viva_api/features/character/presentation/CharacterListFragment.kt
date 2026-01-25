package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import androidx.activity.OnBackPressedCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import edu.iesam.aqui_no_hay_quien_viva_api.core.presentation.errors.ErrorAppFactory
import edu.iesam.aqui_no_hay_quien_viva_api.databinding.FragmentCharacterListBinding
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation.CharacterWithFavorite
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterListViewModel by viewModel()
    private val errorFactory by lazy { ErrorAppFactory(requireContext()) }

    private val adapter = CharacterAdapter(
        onItemClick = { character ->
            navigateToDetail(character.id)
        },
        onFavoriteClick = { character ->
            viewModel.toggleFavorite(character)
        }
    )

    private var allCharacters: List<CharacterWithFavorite> = emptyList()

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUI(state)
                }
            }
        }
        setupRecyclerView()
        setupSearchView()
        setUpFilterChip()
        setUpBackPressHandler()
    }

    private fun setupRecyclerView() {
        binding.recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener { text ->
            filterList(text?.toString().orEmpty())
        }

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString()
            binding.searchBar.setText(query)
            binding.searchView.hide()
            filterList(query)
            false
        }
    }

    private fun setUpFilterChip() {
        binding.chipFavorites.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setFavoritesFilter(isChecked)
        }
    }

    private fun setUpBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.searchView.isShowing) {
                        binding.searchView.hide()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        isEnabled = true
                    }
                }
            }
        )
    }

    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            allCharacters
        } else {
            allCharacters.filter {
                it.character.name.contains(query, ignoreCase = true) ||
                        it.character.nickname.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filteredList)
    }

    private fun updateUI(state: CharacterListUiState) {
        binding.progressBar.isVisible = state.isLoading
        
        state.error?.let { error ->
            binding.errorView.visibility = View.VISIBLE
            binding.errorView.render(errorFactory.build(error) {
                viewModel.retry()
            })
            binding.recycler.visibility = View.GONE
            binding.emptyStateView.visibility = View.GONE
        } ?: run {
            binding.errorView.visibility = View.GONE
        }

        if (state.showContent) {
            binding.recycler.visibility = View.VISIBLE
            binding.emptyStateView.visibility = View.GONE
            
            // Keep reference to full list
            allCharacters = state.characters ?: emptyList()

            // Apply existing filter if any
            val query = binding.searchView.text.toString()
            if (query.isNotEmpty()) {
                filterList(query)
            } else {
                adapter.submitList(allCharacters)
            }
            
            // Sync chip (avoid triggering listener loop if possible, or just set)
             if (binding.chipFavorites.isChecked != state.showOnlyFavorites) {
                 binding.chipFavorites.isChecked = state.showOnlyFavorites
             }
        }

        if (state.showEmpty) {
            binding.recycler.visibility = View.GONE
            binding.emptyStateView.visibility = View.VISIBLE
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
