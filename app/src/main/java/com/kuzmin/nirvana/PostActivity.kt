package com.kuzmin.nirvana

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuzmin.nirvana.Adapter.PostAdapter
import com.kuzmin.nirvana.api.App
import com.kuzmin.nirvana.model.PostModel
import com.kuzmin.nirvana.other.Helper
import com.kuzmin.nirvana.other.isFirstTime
import com.kuzmin.nirvana.other.setNotFirstTime
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.coroutines.launch

class PostActivity : AppCompatActivity()  ,
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnRepostsBtnClickListener,
    PostAdapter.OnLoadMoreBtnClickListener {
    private var dialog: ProgressDialog? = null
    var myadapter = PostAdapter(ArrayList<PostModel>())
    var items = ArrayList<PostModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        fab.setOnClickListener {
            goToNewPost()
        }
        swipeContainer.setOnRefreshListener {
            refreshData()
        }

    }

    private fun refreshData() {
        lifecycleScope.launch {
            val newData = App.repository.getPosts()
            swipeContainer.isRefreshing = false
            if (newData.isSuccessful) {
                myadapter?.newRecentPosts(newData.body()!!)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            dialog = ProgressDialog(this@PostActivity).apply {
                setMessage(this@PostActivity.getString(R.string.please_wait))
                Toast.makeText(this@PostActivity, getString(R.string.download_post), Toast.LENGTH_LONG).show()
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result = App.repository.getPosts()
            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container) {
                    items = result.body() as ArrayList<PostModel>
                    layoutManager = LinearLayoutManager(this@PostActivity)
                    adapter = myadapter.apply {
                        likeBtnClickListener = this@PostActivity
                        repostsBtnClickListener = this@PostActivity
                        loadMoreBtnClickListener = this@PostActivity
                    }
                    myadapter.newRecentPosts(items)
                }
            } else {
                Toast.makeText(this@PostActivity, getString(R.string.error_occured), Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            item.likeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response = if (item.like) {
                    App.repository.cancelMyLike(item.id)
                } else {
                    App.repository.likedByMe(item.id)
                }
                item.likeActionPerforming = false
                if (response.isSuccessful) {
                    item.updatePost(response.body()!!)
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onRepostsBtnClicked(item: PostModel, position: Int, it: String) {

        lifecycleScope.launch {
            item.repostActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response = App.repository.createRepost(it, item)
                item.repostActionPerforming = false
            }
            refreshData()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (isFirstTime(this)) {
            Helper.comeBackNotification(this)
            setNotFirstTime(this)
        }
    }

    fun goToNewPost() {
        val intent = Intent(this@PostActivity, CreatePostActivity::class.java)
        startActivity(intent)
    }

    override fun onLoadMoreBtnClickListener(last: Long, size: Int) {

    }
}