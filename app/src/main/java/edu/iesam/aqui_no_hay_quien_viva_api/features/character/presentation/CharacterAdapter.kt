package edu.iesam.aqui_no_hay_quien_viva_api.features.character.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import edu.iesam.aqui_no_hay_quien_viva_api.R
import edu.iesam.aqui_no_hay_quien_viva_api.databinding.ViewCharacterItemBinding
import edu.iesam.aqui_no_hay_quien_viva_api.features.character.domain.Character


class CharacterAdapter(
    private val onItemClick: (Character) -> Unit
) : ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    class CharacterViewHolder(
        private val binding: ViewCharacterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, onItemClick: (Character) -> Unit) {
            binding.characterName.text = character.shortname.ifEmpty { character.name }
            binding.characterDescription.text = buildFullName(character)

            binding.characterAvatar.load(character.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.img_unknown_error)
                error(R.drawable.img_unknown_error)
                transformations(CircleCropTransformation())
            }

            binding.root.setOnClickListener { onItemClick(character) }
        }

        private fun buildFullName(character: Character): String {
            return listOf(character.name, character.surname, character.secondSurname)
                .filter { it.isNotBlank() }
                .joinToString(" ")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ViewCharacterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    private class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}
