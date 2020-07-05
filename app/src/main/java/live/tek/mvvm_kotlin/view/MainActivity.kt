package live.tek.mvvm_kotlin.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import live.tek.mvvm_kotlin.adapter.PostAdapter
import live.tek.mvvm_kotlin.databinding.ActivityMainBinding
import live.tek.mvvm_kotlin.model.Post
import live.tek.mvvm_kotlin.view_model.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var test: ArrayList<Post>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this@MainActivity).get(MainActivityViewModel::class.java)

        viewModel.showProgress.observe(this@MainActivity, Observer {
            if (it) {
                binding.searchProgress.visibility = VISIBLE
            } else {
                binding.searchProgress.visibility = GONE
            }
        })
        viewModel.postList.observe(this, Observer {
            postAdapter.setList(it)
            test = postAdapter.getList()
            if (binding.rv.adapter == null) {
                binding.rv.adapter = postAdapter
            }
        })
        postAdapter = PostAdapter()
        binding.ivSearch.setOnClickListener {
            binding.etSearch.clearFocus()
        }
        var temp: List<Post>
        viewModel.getAllPosts()


        //  val list=ArrayList<Post>()
        //  postAdapter.setList(list)

        binding.rv.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayout.VERTICAL
            )
        )

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
                    postAdapter.setList(ArrayList(temp))
                }

            }
        )
    }

    override fun onPause() {
        binding.etSearch.text.clear()
        super.onPause()
    }
}