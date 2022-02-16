package com.kuzmin.nirvana.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuzmin.nirvana.R
import com.kuzmin.nirvana.dto.LikeDislikeDto
import com.kuzmin.nirvana.dto.LikeType
import com.kuzmin.nirvana.model.PostModel
import kotlinx.android.synthetic.main.item_tool_like.view.*
import java.util.ArrayList

class LikeAdapter(val likeList: MutableList<LikeDislikeDto>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val postView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tool_like, parent, false)
        return onLike(this,postView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val likeIndex = position
        when (holder) {
            is onLike -> holder.bind(likeList[likeIndex])
        }
    }

    override fun getItemCount(): Int {
       return likeList.size
    }

   class onLike(likeAdapter: LikeAdapter, postView: View): RecyclerView.ViewHolder(postView) {
       fun bind(likeDislikeDto: LikeDislikeDto) {
           with(itemView){
               nameAuthor.text = likeDislikeDto.author.toString()
               date.text = likeDislikeDto.date.toString()
               when(likeDislikeDto.author.attachment?.mediaType){
                   PostModel.AttachmentType.IMAGE -> loadImageAuthor(imageAutor,likeDislikeDto.author.attachment.url)
               }
               when(likeDislikeDto.type){
                   LikeType.Like -> {
                       imageLike.setImageResource(R.drawable.ic_baseline_thumb_up_true)

                   }
                   LikeType.Dislike -> {
                       imageLike.setImageResource(R.drawable.ic_baseline_thumb_down_true)
                   }
               }
           }

       }
       private fun loadImageAuthor(photoImg: ImageView, imageUrl: String) {
           val requestOptionsCompat =  RequestOptions()
               .placeholder(R.drawable.ic_launcher_background)
               .error(R.drawable.common_google_signin_btn_icon_dark)
           Glide.with(photoImg.context)
               .load(imageUrl)
               .into(photoImg)
       }

   }

}