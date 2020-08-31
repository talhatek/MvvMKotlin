package live.tek.mvvm_kotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import live.tek.mvvm_kotlin.model.User
import live.tek.mvvm_kotlin.viewholder.UserViewHolder

class UserAdapter (private val users:ArrayList<User>) : RecyclerView.Adapter<UserViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
      return  UserViewHolder(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(user = users[position])
    }

    override fun getItemCount(): Int {
      return  users.size
    }

    fun addUsers(users: List<User>) {
        this.users.apply {
            clear()
            addAll(users)
        }

    }

    fun deleteOne(position: Int) {
        users.removeAt(position)
    }

}