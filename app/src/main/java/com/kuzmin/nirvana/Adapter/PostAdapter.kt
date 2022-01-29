package com.kuzmin.nirvana.Adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color.red
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuzmin.nirvana.R
import com.kuzmin.nirvana.model.PostModel
import kotlinx.android.synthetic.main.activity_repost.*
import kotlinx.android.synthetic.main.item_tool_post.view.*
import kotlinx.android.synthetic.main.item_tool_post.view.likeBtn
import kotlinx.android.synthetic.main.item_tool_post.view.repostsTv
import kotlinx.android.synthetic.main.item_tool_post.view.shareBtn
import kotlinx.android.synthetic.main.item_tool_repost.view.*

class PostAdapter (val list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostsBtnClickListener: OnRepostsBtnClickListener? = null
    var loadMoreBtnClickListener: OnLoadMoreBtnClickListener? = null
    private val ITEM_TYPE_POST = 1
    private val ITEM_TYPE_REPOST = 2
    private val ITEM_FOOTER = 3;



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_POST) {
            val postView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tool_post, parent, false)
            PostViewHolder(this, postView)
        } else if (viewType == ITEM_TYPE_REPOST) {
            val repostView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tool_repost, parent, false)
            RepostViewHolder(this, repostView)
        } else {
            PostViewHolder.FooterViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(R.layout.item_tool_more, parent, false)
            )
        }
    }

    override fun getItemCount() = list.size + 1



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postIndex = position
        when (holder) {
            is PostViewHolder -> holder.bind(list[postIndex])
            is RepostViewHolder -> holder.bind(list[postIndex])
            else -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == list.size -> ITEM_FOOTER
            list[position].repost == null -> ITEM_TYPE_POST
            else -> ITEM_TYPE_REPOST
        }
    }



    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnRepostsBtnClickListener {
        fun onRepostsBtnClicked(
            item: PostModel,
            position: Int,
            it: String
        )
    }

    interface OnLoadMoreBtnClickListener {
        fun onLoadMoreBtnClickListener(last:Long, size: Int)
    }

    fun newRecentPosts(list: List<PostModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


}


class RepostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.getString(R.string.progress)
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.repost?.author
            contentRp.text = post.txt
            contentTv.text = post.repost?.txt
            likesTv.text = post.likeTxt.toString()
            repostsTv.text = post.shareTxt.toString()
            autorRP.text = post.author

            when {
                post.likeActionPerforming -> likeBtn.setImageResource(R.drawable.ic_baseline_favorite_true)
                post.like -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_true)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_false)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.grey))
                }
            }
            when {
                post.repostActionPerforming -> {
                    shareBtn.setImageResource(R.drawable.ic_baseline_share_active)
                }
                post.share -> {
                    shareBtn.setImageResource(R.drawable.ic_baseline_share_active)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    shareBtn.setImageResource(R.drawable.ic_baseline_share_24)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.grey))
                }
            }
        }
    }

}


class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition ]
                    if (item.likeActionPerforming) {
                        context.getString(R.string.progress)
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            shareBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.share) {
                        context.getString(R.string.repost_repost)
                    } else {
                        showDialog(context) {
                            adapter.repostsBtnClickListener?.onRepostsBtnClicked(
                                item,
                                currentPosition,
                                it
                            )
                        }
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.author
            contentTv.text = post.txt
            likesTv.text = post.likeTxt.toString()
            repostsTv.text = post.shareTxt.toString()

            when {
                post.likeActionPerforming -> likeBtn.setImageResource(R.drawable.ic_baseline_favorite_true)
                post.like -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_true)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_true)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.grey))
                }
            }
            when {
                post.repostActionPerforming -> {
                    shareBtn.setImageResource(R.drawable.ic_baseline_share_24)
                }
                post.share -> {
                    shareBtn.setImageResource(R.drawable.ic_baseline_share_24)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    shareBtn.setImageResource(R.drawable.ic_baseline_share_24)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.grey))
                }
            }
            when (post.attachment?.mediaType) {
                PostModel.AttachmentType.IMAGE -> loadImage(photoImg, post.attachment.url) }
        }
    }

    class FooterViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {

    }


    fun showDialog(context: Context, createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.activity_repost)
            .show()
        dialog.createRepostBtn.setOnClickListener {
            createBtnClicked(dialog.contentEdtRepost.text.toString())
            dialog.dismiss()
        }
    }

    private fun loadImage(photoImg: ImageView, imageUrl: String) {
        Glide.with(photoImg.context)
            .load(imageUrl)
            .into(photoImg)
    }

}