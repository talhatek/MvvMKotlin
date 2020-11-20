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
import androidx.recyclerview.widget.DividerItemDecoration
import live.tek.mvvm_kotlin.databinding.ActivityMainBinding
import live.tek.mvvm_kotlin.di.MainActivityDI
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.User
import live.tek.mvvm_kotlin.utils.Status
import live.tek.mvvm_kotlin.view_model.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()
    private val mainActivityDI: MainActivityDI by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //without koin and viewModelFactory
        // viewModel = ViewModelProvider(this@MainActivity).get(MainActivityViewModel::class.java)
        //with factory
        // viewModel=ViewModelProvider(this@MainActivity,ViewModelFactory(application)).get(MainActivityViewModel::class.java)

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
                        resource.data?.let { posts ->
                            mainActivityDI.aryPost = posts as ArrayList<Post>
                            supplyToAdapter(posts)
                        }
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
                        resource.data?.let { users ->
                            mainActivityDI.aryUser = users as ArrayList<User>
                            retrieveList(users)
                        }
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

        binding.rv.adapter = mainActivityDI.userAdapter

        var temp: List<Post>
        var tempUser: List<User>



        binding.rv.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayout.VERTICAL
            )
        )
        binding.ivSearch.setOnClickListener {

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
                    if (binding.rv.adapter == mainActivityDI.postAdapter) {
                        temp = mainActivityDI.aryPost.filter {
                            it.body.toLowerCase(Locale.ROOT).contains(s.toString())
                        }
                        mainActivityDI.postAdapter.apply {
                            supplyPost(temp)

                        }
                    } else {
                        tempUser = mainActivityDI.aryUser.filter {
                            it.name.toLowerCase(Locale.ROOT).contains(s.toString())
                        }
                        mainActivityDI.postAdapter.apply {
                            retrieveList(tempUser)

                        }
                    }

                }

            }
        )

    }

    private fun supplyToAdapter(posts: ArrayList<Post>) {
        mainActivityDI.postAdapter.apply {
            supplyPost(posts)
            notifyDataSetChanged()
            binding.rv.adapter = mainActivityDI.postAdapter
        }
    }

    private fun retrieveList(users: List<User>) {
        mainActivityDI.userAdapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }
}