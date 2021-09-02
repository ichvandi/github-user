package com.vandoc.githubuser.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vandoc.githubuser.databinding.ItemUserBinding
import com.vandoc.githubuser.model.User

class UserAdapter(private val onItemClicked: (User) -> Unit) :
    ListAdapter<User, UserAdapter.ViewHolder>(UserDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class ViewHolder private constructor(
        private val binding: ItemUserBinding,
        private val onItemClicked: (User) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, onItemClicked: (User) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(view, onItemClicked)
            }
        }

        fun bind(user: User) {
            with(binding) {
                root.setOnClickListener { onItemClicked(user) }

                ivAvatar.load(user.avatar)

                tvId.text = user.id.toString()
                tvUsername.text = user.username
                tvEmail.text = user.email

                tvLocation.text = user.location
                tvCreatedAt.text = user.createdAt
            }
        }
    }

    class UserDiffUtil : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}