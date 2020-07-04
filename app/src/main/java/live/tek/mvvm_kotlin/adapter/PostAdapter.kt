package live.tek.mvvm_kotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.viewholder.PostViewHolder

class PostAdapter : RecyclerView.Adapter<PostViewHolder>() {
    private lateinit var postList: ArrayList<Post>

    fun setList(list: ArrayList<Post>) {
        this.postList = list
        notifyDataSetChanged()

    }

    fun getList(): ArrayList<Post> {
        return postList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return postList.size

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }


}