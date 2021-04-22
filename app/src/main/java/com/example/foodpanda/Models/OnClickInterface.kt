package com.example.foodpanda.Models

import java.text.FieldPosition

interface OnClickInterface {
    fun onClick(name:Int)
    fun onAdd(addItemIndex:Int)
    fun addQuantity(position: Int,quantity:String)
    fun decreaseQuantity(position: Int,quantity: String)
}