package live.tek.mvvm_kotlin.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_item_layout.view.*
import live.tek.mvvm_kotlin.R
import live.tek.mvvm_kotlin.databinding.UserItemLayoutBinding
import live.tek.mvvm_kotlin.model.User

class UserViewHolder (content: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(content.context).inflate(
        R.layout.user_item_layout,
        content,
        false
    )
){
    private val binding=UserItemLayoutBinding.bind(itemView.rootView)

    fun bind(user: User){
        itemView.txtUserName.text=user.name
        itemView.txtEMail.text=user.email
    }
}
