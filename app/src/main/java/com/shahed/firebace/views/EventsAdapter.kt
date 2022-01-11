package com.shahed.firebace.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahed.firebace.databinding.ItemEmptyViewBinding
import com.shahed.firebace.databinding.ItemEventBinding
import com.shahed.firebace.network.model.Event

class EventsAdapter(
    var onClick: (place: Event) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: MutableList<Event> = ArrayList()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.isEmpty() || data.size == 0) VIEW_TYPE_EMPTY
        else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val binding = ItemEventBinding.inflate(inflater, parent, false)
                ViewHolder(binding)
            }
            VIEW_TYPE_EMPTY -> {
                val binding = ItemEmptyViewBinding.inflate(inflater, parent, false)
                EmptyViewHolder(binding)
            }

            else -> {
                val binding = ItemEmptyViewBinding.inflate(inflater, parent, false)
                EmptyViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.onBind(position)
        else if (holder is EmptyViewHolder) holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return if (data.size == 0) 1
        else data.size
    }

    inner class ViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {


            val bean = data[position]
            binding.eventId.text = bean.id
            binding.tvTitle.text = bean.title
            binding.tvDescription.text = bean.description


            Glide.with(itemView.context)
                .load(bean.imageUrl)
                .centerCrop()
                .into(binding.ivEvent)

            binding.root.setOnClickListener {
                onClick.invoke(bean)
            }

        }
    }

    inner class EmptyViewHolder(val binding: ItemEmptyViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {


        }


    }


}