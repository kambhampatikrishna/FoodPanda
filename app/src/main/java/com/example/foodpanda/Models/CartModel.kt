package com.example.foodpanda.Models


data class CartModel(var itemname:String,
                      var itemcost:String,
                      var item_quantity:Int,
                     var rest_name:String,
                     var rest_loc:String,
                     var category: String
              )