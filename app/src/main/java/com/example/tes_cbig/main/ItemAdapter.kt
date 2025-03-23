package com.example.tes_cbig.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tes_cbig.R

class ItemAdapter(
    private val onEditClick: (com.example.tes_cbig.database.Item) -> Unit,
    private val onDeleteClick: (com.example.tes_cbig.database.Item) -> Unit
) : ListAdapter<com.example.tes_cbig.database.Item, ItemAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position) // Pastikan ini adalah database.Item
        holder.bind(item)

        holder.btnEdit.setOnClickListener {
            onEditClick(item)
        }

        // Tombol Delete
        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        fun bind(item: com.example.tes_cbig.database.Item) {
            // Bind data ke view
            itemView.findViewById<TextView>(R.id.itemName).text = item.item_name
            itemView.findViewById<TextView>(R.id.stock).text = "Stok: ${item.stock}"
            itemView.findViewById<TextView>(R.id.unit).text = "Stok: ${item.unit}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<com.example.tes_cbig.database.Item>() {
        override fun areItemsTheSame(oldItem: com.example.tes_cbig.database.Item, newItem: com.example.tes_cbig.database.Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: com.example.tes_cbig.database.Item, newItem: com.example.tes_cbig.database.Item): Boolean {
            return oldItem == newItem
        }
    }
}