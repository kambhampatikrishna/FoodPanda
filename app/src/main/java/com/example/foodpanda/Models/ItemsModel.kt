package com.example.foodpanda.Models

data class ItemsModel(val imagefood:String,
                      val itemname:String,
                      val itemcost:String,
                      var item_quantity:Int,
                      var category:String)