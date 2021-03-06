package com.kuzmin.nirvana

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuzmin.nirvana.Adapter.PostAdapter
import com.kuzmin.nirvana.api.App
import com.kuzmin.nirvana.model.PostModel
import com.kuzmin.nirvana.other.Helper
import com.kuzmin.nirvana.other.isFirstTime
import com.kuzmin.nirvana.other.setNotFirstTime
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.item_tool_post.*
import kotlinx.android.synthetic.main.item_tool_post.view.*
import kotlinx.coroutines.launch

class PostActivity : AppCompatActivity()  ,
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnRepostsBtnClickListener,
    PostAdapter.OnLoadMoreBtnClickListener, PostAdapter.OnDisLikeBtnClickListener,PostAdapter.OnViewLikeClickListener {

    private var dialog: ProgressDialog? = null
    var myadapter = PostAdapter(ArrayList<PostModel>())
    var items = ArrayList<PostModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val id = intent.getStringExtra("id")
        val popMenu = PopupMenu(this,fab)

        popMenu.menuInflater.inflate(R.menu.popup_menu,popMenu.menu)
        popMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if(id == R.id.user){
                goToUser()
            }
            if(id == R.id.createPost){
                goToNewPost()
            }
            false
        }

        lifecycleScope.launch {
            val resp = id?.let { App.repository.getPostId(it.toLong()) }
            val post = resp?.body()
            when (post?.attachment?.mediaType) {
                PostModel.AttachmentType.IMAGE -> loadImage(photoImg, post.attachment.url)
            }

        }
        fab.setOnClickListener {
            popMenu.show()
            fab.setBackgroundResource(R.drawable.ic_baseline_close_24)
        }
        swipeContainer.setOnRefreshListener {
            refreshData()
        }
        date()
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
                        dislikeBtnClickListener = this@PostActivity
                        viewLike = this@PostActivity
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

            if(item.like || item.dislike){
                Toast.makeText(this@PostActivity, "???? ?????? ?????????????????? ?????????????? ", Toast.LENGTH_SHORT).show()
            }
            with(container) {
                item.likeActionPerforming = true
                adapter?.notifyItemChanged(position)
                val response = App.repository.likedByMe(item.id)
                item.likeActionPerforming = false
                if (response.isSuccessful) {
                    item.updatePost(response.body()!!)
                }
                adapter?.notifyItemChanged(position)
            }

        }
    }

    override fun onDisLikeBtnClicked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            if(item.like || item.dislike){
                Toast.makeText(this@PostActivity, "???? ?????? ?????????????????? ????????", Toast.LENGTH_SHORT).show()
            }
            item.dislikeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response = App.repository.dislike(item.id)
                item.dislikeActionPerforming = false
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

    fun goToViewLike() {

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

    fun goToUser() {
        val intent = Intent(this@PostActivity, UserActivity::class.java)
        startActivity(intent)
    }

    override fun onLoadMoreBtnClickListener(last: Long, size: Int) {

    }
    private fun loadImage(photoImg: ImageView, imageUrl: String) {
        val requestOptionsCompat =  RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.common_google_signin_btn_icon_dark)
        Glide.with(photoImg.context)
            .load(imageUrl)
            .into(photoImg)
    }




    fun date(): String {
        val data: Long = 0
        val publishedAgo =  (System.currentTimeMillis() - data) / 1000
        val toMin = if (publishedAgo > 3599) {
            publishedAgo / 3600
        } else {
            publishedAgo / 60
        }
        return when (publishedAgo) {
            in 0..59 -> "?????????? ???????????? ??????????"
            in 60..179 -> "???????????? ??????????"
            in 180..299 -> "$toMin ???????????? ??????????"
            in 300..3599 -> "$toMin ?????????? ??????????"
            in 3600..17999 -> "$toMin ???????? ??????????"
            else -> "$toMin ?????????? ?????????? "
        }
    }

    override fun onViewLikeBtn(item: PostModel) {
        val intent = Intent(this@PostActivity, LikeActivity::class.java)
        startActivity(intent)
    }

}