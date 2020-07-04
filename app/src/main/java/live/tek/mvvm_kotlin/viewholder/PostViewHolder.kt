package live.tek.mvvm_kotlin.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import live.tek.mvvm_kotlin.R
import live.tek.mvvm_kotlin.databinding.PostItemLayoutBinding
import live.tek.mvvm_kotlin.model.Post

class PostViewHolder(content: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(content.context).inflate(
        R.layout.post_item_layout,
        content,
        false
    )
) {
    private val binding = PostItemLayoutBinding.bind(itemView.rootView)
    fun bind(post: Post) {
        itemView.tag = post.id
        binding.titleHere.text = post.title
        binding.bodyHere.text = post.body
    }
}