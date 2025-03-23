package com.example.tes_cbig.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val item_name: String,
    val stock: Int,
    val unit: String
){
    companion object {
        fun fromItem(item: com.example.tes_cbig.main.Item): Item{
            return Item(
                id = item.id,
                item_name = item.item_name,
                stock = item.stock,
                unit = item.unit
            )
        }
    }
}
