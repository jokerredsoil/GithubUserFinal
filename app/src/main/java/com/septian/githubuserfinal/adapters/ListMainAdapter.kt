package com.septian.githubuserfinal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.septian.githubuserfinal.databinding.ItemRowUserBinding
import com.septian.githubuserfinal.dataclass.User


class ListMainAdapter : RecyclerView.Adapter<ListMainAdapter.ListMainViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val listUser = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListMainViewHolder {
        val mView = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListMainViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListMainViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListMainViewHolder(private val mView: ItemRowUserBinding) : RecyclerView.ViewHolder(mView.root) {

        fun bind(user: User) {
            with(mView) {
                tvNameIRW.text = user.login
                tvHtmlIRW.text = user.html
                Glide.with(itemView.context)
                        .load(user.avatar)
                        .apply(RequestOptions().override(55, 55))
                        .into(imgIRW)
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

}