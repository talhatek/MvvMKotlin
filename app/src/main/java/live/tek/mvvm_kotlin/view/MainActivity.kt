package live.tek.mvvm_kotlin.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import live.tek.mvvm_kotlin.databinding.ActivityMainBinding
import live.tek.mvvm_kotlin.di.MainActivityDI
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.model.User
import live.tek.mvvm_kotlin.utils.*
import live.tek.mvvm_kotlin.view_model.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()
    private val mainActivityDI: MainActivityDI by inject()


    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //without koin and viewModelFactory
        // viewModel = ViewModelProvider(this@MainActivity).get(MainActivityViewModel::class.java)
        //with factory
        // viewModel=ViewModelProvider(this@MainActivity,ViewModelFactory(application)).get(MainActivityViewModel::class.java)



        viewModel.getUsers().observe(this@MainActivity, {
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
                        binding.rv.visibility = INVISIBLE
                        binding.searchProgress.visibility = GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.searchProgress.visibility = VISIBLE
                        binding.rv.visibility = INVISIBLE
                    }
                }
            }
        })

        binding.rv.adapter = mainActivityDI.userAdapter

        binding.rv.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayout.VERTICAL
            )
        )

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                Log.e("scrollState", "state -> $newState")

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    Log.e("scrollState", "up")
                    hideKeyboard()
                } else {
                    Log.e("scrollState", "down")

                }
            }
        })
        binding.etSearch.getQueryTextChangeFlow()
            .observe(this@MainActivity, {
                Log.e("searchResSent", it)
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        Log.e("searchDataSent", it)
                        viewModel.queryChannel.send(it)

                    }
                }
            })


        viewModel.searchLiveData.observe(this@MainActivity, {
            when (it.status) {
                Status.ERROR -> {
                    binding.searchProgress.visibility = GONE;Toast.makeText(
                        applicationContext,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.LOADING -> {
                    binding.searchProgress.visibility = VISIBLE
                }
                Status.SUCCESS -> {
                    binding.searchProgress.visibility = GONE

                    mainActivityDI.userAdapter.apply {
                        addUsers(it.data!!)
                        notifyDataSetChanged()
                    }
                }
            }

        })



    }

    private fun retrieveList(users: List<User>) {
        viewModel.userList=users
        mainActivityDI.userAdapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }


}
