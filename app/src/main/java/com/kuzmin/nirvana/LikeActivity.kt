package com.kuzmin.nirvana

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuzmin.nirvana.Adapter.PostAdapter
import com.kuzmin.nirvana.api.App
import com.kuzmin.nirvana.dto.LikeDislikeDto
import com.kuzmin.nirvana.model.PostModel
import kotlinx.android.synthetic.main.activity_like.*
import kotlinx.coroutines.launch

class LikeActivity : AppCompatActivity() {
    var items = ArrayList<LikeDislikeDto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)
        loading()
    }

    private fun loading() {
        val bundle = intent.extras
        val id = bundle?.getString("LikeDislike")?.toLong()
        lifecycleScope.launch {
            try {
                with(recyclerView) {
                    if (id != null) {
                        val result = App.repository.getPostsAfter(id)
                        items = result.body()?.postLike as ArrayList<LikeDislikeDto>
                        layoutManager = LinearLayoutManager(this@LikeActivity)
                        adapter = PostAdapter(items as MutableList<PostModel>)
                    } else {
                        Toast.makeText(this@LikeActivity, "Ошибка", Toast.LENGTH_SHORT).show()
                    }

                }
            } catch (e: Exception) {
                Toast.makeText(this@LikeActivity, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }

    }
}