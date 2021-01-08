package com.septian.githubuserfinal.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.septian.githubuserfinal.CustomOnItemClickListener
import com.septian.githubuserfinal.DetailActivity
import com.septian.githubuserfinal.R
import com.septian.githubuserfinal.databinding.ItemRowUserBinding
import com.septian.githubuserfinal.dataclass.User

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFav = ArrayList<User>()
        set(listFav) {
            if (listFav.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFav)
            notifyDataSetChanged()
        }

    fun addItem(user: User) {
        this.listFav.add(user)
        notifyItemInserted(this.listFav.size - 1)
    }

    fun removeItem(position: Int) {
        this.listFav.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, this.listFav.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavoriteViewHolder(view)
    }


    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = this.listFav.size

    inner class FavoriteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(user: User) {
            with(binding) {
                tvNameIRW.text = user.login
                tvHtmlIRW.text = user.html
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgIRW)
                itemView.setOnClickListener(
                    CustomOnItemClickListener(adapterPosition,
                        object : CustomOnItemClickListener.OnitemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_USER, user)
                                activity.startActivity(intent)
                            }

                        })
                )
            }
        }
    }
}
