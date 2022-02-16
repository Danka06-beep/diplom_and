package com.kuzmin.nirvana.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuzmin.nirvana.R
import com.kuzmin.nirvana.dto.LikeDislikeDto
import com.kuzmin.nirvana.model.PostModel
import kotlinx.android.synthetic.main.activity_repost.*
import kotlinx.android.synthetic.main.item_tool_post.view.*
import kotlinx.android.synthetic.main.item_tool_post.view.likeBtn
import kotlinx.android.synthetic.main.item_tool_post.view.repostsTv
import kotlinx.android.synthetic.main.item_tool_post.view.shareBtn
import kotlinx.android.synthetic.main.item_tool_repost.view.*

class PostAdapter(val list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var dislikeBtnClickListener: OnDisLikeBtnClickListener? = null
    var repostsBtnClickListener: OnRepostsBtnClickListener? = null
    var viewLike: OnViewLikeClickListener? = null
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

    interface OnDisLikeBtnClickListener {
        fun onDisLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnViewLikeClickListener {
        fun onViewLikeBtn(item: PostModel)
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
            likeBtnTv.setOnClickListener {
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
            dislikeBtnTv.setOnClickListener {
                val currentPositionUs = adapterPosition
                if (currentPositionUs != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPositionUs]
                    if (item.dislikeActionPerforming) {
                        context.getString(R.string.progress)
                    } else {
                        adapter.dislikeBtnClickListener?.onDisLikeBtnClicked(item, currentPositionUs)
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
            dislikeTxtTv.text = post.dislikeTxt.toString()
            repostsTv.text = post.shareTxt.toString()
            autorRP.text = post.author

            when {
                post.likeActionPerforming -> likeBtnTv.setImageResource(R.drawable.ic_baseline_thumb_up_true)
                post.like -> {
                    likeBtnTv.setImageResource(R.drawable.ic_baseline_thumb_up_true)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.green))
                }
                else -> {
                    likeBtnTv.setImageResource(R.drawable.ic_baseline_thumb_up_24)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }

            when {
                post.dislikeActionPerforming -> dislikeBtnTv.setImageResource(R.drawable.ic_baseline_thumb_down_true)
                post.dislike -> {
                    dislikeBtnTv.setImageResource(R.drawable.ic_baseline_thumb_down_true)
                    dislikeTxtTv.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    dislikeBtnTv.setImageResource(R.drawable.ic_baseline_thumb_down_false)
                    dislikeTxtTv.setTextColor(ContextCompat.getColor(context, R.color.black))
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

            dislikeBtn.setOnClickListener {
                val currentPositionUs = adapterPosition
                if (currentPositionUs != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPositionUs]
                    if (item.dislikeActionPerforming) {
                        context.getString(R.string.progress)
                    } else {
                        adapter.dislikeBtnClickListener?.onDisLikeBtnClicked(item, currentPositionUs)
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
            likeAndDslikeResultBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    adapter.viewLike?.onViewLikeBtn(item)
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            author.text = post.author
            text.text = post.txt
            dislikeTxt.text = post.dislikeTxt.toString()
            likeTxt.text = post.likeTxt.toString()
            repostsTv.text = post.shareTxt.toString()

            when {
                post.likeActionPerforming -> likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_true)
                post.like -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_true)
                    likeTxt.setTextColor(ContextCompat.getColor(context, R.color.green))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_24)
                    likeTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }

            when {
                post.dislikeActionPerforming -> dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_true)
                post.dislike -> {
                    dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_true)
                    dislikeTxt.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_false)
                    dislikeTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
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
        val requestOptionsCompat =  RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.common_google_signin_btn_icon_dark)
        Glide.with(photoImg.context)
            .load(imageUrl)
            .into(photoImg)
    }

}