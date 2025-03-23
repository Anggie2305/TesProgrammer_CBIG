package com.example.tes_cbig.main

data class ItemResponse(
    val statusCode: Int,
    val message: String,
    val data: List<Item>
)

data class Item(
    val id: Int,
    val item_name: String,
    val stock: Int,
    val unit: String
)