package live.tek.mvvm_kotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.viewholder.PostViewHolder

class PostAdapter(private val postList:ArrayList<Post>) : RecyclerView.Adapter<PostViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return postList.size

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    fun supplyPost(posts:List<Post>){
        this.postList.apply {
            clear()
            addAll(posts)
        }

    }


}