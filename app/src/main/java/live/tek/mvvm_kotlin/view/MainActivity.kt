package live.tek.mvvm_kotlin.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.tek.mvvm_kotlin.adapter.PostAdapter
import live.tek.mvvm_kotlin.adapter.UserAdapter
import live.tek.mvvm_kotlin.databinding.ActivityMainBinding
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.User
import live.tek.mvvm_kotlin.utils.Status
import live.tek.mvvm_kotlin.view_model.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var userAdapter: UserAdapter
    private lateinit var test: ArrayList<Post>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this@MainActivity).get(MainActivityViewModel::class.java)
       // viewModel=ViewModelProvider(this@MainActivity,ViewModelFactory(application)).get(MainActivityViewModel::class.java)
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
        viewModel.showProgress.observe(this@MainActivity, Observer {
            if (it) {
                binding.searchProgress.visibility = VISIBLE
            } else {
                binding.searchProgress.visibility = GONE
            }
        })
        viewModel.postList.observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.rv.visibility = VISIBLE
                        binding.searchProgress.visibility = GONE
                        resource.data?.let { posts -> supplyToAdapter(posts as ArrayList<Post>) }
                    }
                    Status.ERROR -> {
                        binding.rv.visibility = VISIBLE
                        binding.searchProgress.visibility = GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.searchProgress.visibility = VISIBLE
                        binding.rv.visibility = GONE
                    }
                }
            }
        })


        viewModel.getUsers().observe(this@MainActivity, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.rv.visibility = VISIBLE
                        binding.searchProgress.visibility = GONE
                        resource.data?.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        binding.rv.visibility = VISIBLE
                        binding.searchProgress.visibility = GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.searchProgress.visibility = VISIBLE
                        binding.rv.visibility = GONE
                    }
                }
            }
        })
        postAdapter = PostAdapter(arrayListOf())
        userAdapter= UserAdapter((arrayListOf()))
        binding.rv.adapter=userAdapter

        var temp: List<Post>



        binding.rv.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayout.VERTICAL
            )
        )
        binding.ivSearch.setOnClickListener {
            // viewModel.doToast()
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.getAllPosts()
            }
        }

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    temp = test.filter {
                        it.body.contains(s.toString())
                    }
                    postAdapter.apply {
                        supplyPost(temp)
                        notifyDataSetChanged()
                    }
                }

            }
        )

    }

    private fun supplyToAdapter(posts: ArrayList<Post>) {
        postAdapter.apply {
            supplyPost(posts)
            notifyDataSetChanged()
            binding.rv.adapter = postAdapter
        }
    }

    private fun retrieveList(users:List<User>){
        userAdapter.apply {
            addUsers(users)
            notifyDataSetChanged()
          //  binding.rv.adapter=userAdapter
        }
    }
}