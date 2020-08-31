package live.tek.mvvm_kotlin.view

import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.onesignal.OneSignal
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.tek.mvvm_kotlin.R
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
        initTouchHelper()
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

    private fun initTouchHelper() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        userAdapter.deleteOne(viewHolder.adapterPosition)
                        binding.rv.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                //when we swipe 40 percent of the screen it will remove
                val display = this@MainActivity.windowManager.defaultDisplay
                val point = Point()
                display.getSize(point)
                val x = point.x
                val temp = x / 10 * 4

                if (dX < -temp) {
                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.colorDelete
                            )
                        )
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_white)
                        .create()
                        .decorate()
                } else {
                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.colorDeleteUpper
                            )
                        )
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_white)
                        .create()
                        .decorate()
                }


                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.4f
            }
        }

        val myItemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        myItemTouchHelper.attachToRecyclerView(binding.rv)
    }

    private fun supplyToAdapter(posts: ArrayList<Post>) {
        postAdapter.apply {
            supplyPost(posts)
            notifyDataSetChanged()
            binding.rv.adapter = postAdapter
        }
    }

    private fun retrieveList(users: List<User>) {
        userAdapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }
}