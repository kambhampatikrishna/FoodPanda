package com.example.foodpanda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodpanda.Adapters.Adapter3
import com.example.foodpanda.Adapters.Adapter4
import com.example.foodpanda.Models.ItemsModel
import com.example.foodpanda.Models.OnClickInterface
import com.example.foodpanda.Models.RestaurantDataModel
import com.google.firebase.database.*

class MenuItemsActivity : AppCompatActivity(),OnClickInterface {
    private var database = FirebaseDatabase.getInstance()
    private var cart_database:DatabaseReference? = database.getReference().child("Cart")
    private var root = database.getReference().child("Menu")
    var TOTAL_QUANTITY = 0
    var TOTAL_COST = 0
    var order_no = 0
    var data1= ArrayList<ItemsModel>()

    lateinit var adapt: Adapter4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_items)
        val view_cart = findViewById<Button>(R.id.b_cart)
        val name=findViewById<TextView>(R.id.restname)
        val rate=findViewById<TextView>(R.id.restrate)
        val cost=findViewById<TextView>(R.id.restcost)
        val loc =  findViewById<TextView>(R.id.rest_location)
        val dist = findViewById<TextView>(R.id.rest_dist)
        name.text=intent.getStringExtra("restname")
        rate.text=intent.getStringExtra("restrating")
        cost.text=intent.getStringExtra("cost")
        loc.text = intent.getStringExtra("rest_location")
        dist.text = intent.getStringExtra("rest_dist")
        val id = intent.getStringExtra("res_id")
        root = root.child(id.toString())

        root.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    print(item.child("item_name").getValue().toString()+" "+item.child("item_price").getValue().toString())
                    data1.add(ItemsModel(item.child("item_image").getValue().toString(),item.child("item_name").getValue().toString(),item.child("item_price").getValue().toString(),0,item.child("category").getValue().toString()))
                }
                adapt.notifyDataSetChanged()

            }

        })

        val recyclervieww= findViewById<RecyclerView>(R.id.recyle)
        recyclervieww.layoutManager= LinearLayoutManager(this)
        recyclervieww.setHasFixedSize(true)
        adapt = Adapter4(this,data1,this)
        recyclervieww.adapter = adapt

        view_cart.setOnClickListener {
            cart_database?.removeValue()
            val hashMap = HashMap<String,String>()
            for(item in data1){
                if(item.item_quantity > 0 ){
                    hashMap.put("category",item.category)
                    hashMap.put("res_id",id.toString())
                    hashMap.put("item_name",item.itemname)
                    hashMap.put("item_price",item.itemcost)
                    hashMap.put("item_quantity",item.item_quantity.toString())
                    hashMap.put("rest_name",name.text.toString())
                    hashMap.put("rest_loc",loc.text.toString())
                    cart_database!!.push().setValue(hashMap)

                }
            }


            val intent = Intent(this,CartActivity::class.java)
            intent.putExtra("TOTAL_COST",TOTAL_COST.toString())
            intent.putExtra("TOTAL_QUANTITY",TOTAL_QUANTITY.toString())
            startActivity(intent)

        }

    }

    override fun onClick(name: Int) {

    }

    override fun onAdd(addItemIndex: Int) {

   }

    override fun addQuantity(position: Int,quantity:String) {
        val TotalQuantity = findViewById<TextView>(R.id.t_cart_count)
        val ToatlPrice = findViewById<TextView>(R.id.t_total_price)
        TOTAL_COST = TOTAL_COST+data1[position].itemcost.toInt()
        TOTAL_QUANTITY = TOTAL_QUANTITY + 1
        ToatlPrice.setText(TOTAL_COST.toString())
        TotalQuantity.setText(TOTAL_QUANTITY.toString())
    }

    override fun decreaseQuantity(position: Int,quantity: String) {
        val TotalQuantity = findViewById<TextView>(R.id.t_cart_count)
        val ToatlPrice = findViewById<TextView>(R.id.t_total_price)
        if(TOTAL_QUANTITY - 1 <= 0)
        {
            TOTAL_COST = 0
            TOTAL_QUANTITY = 0
            ToatlPrice.setText(TOTAL_COST.toString())
            TotalQuantity.setText(TOTAL_QUANTITY.toString())

        }
        else {
            TOTAL_QUANTITY =  TOTAL_QUANTITY - 1
            TOTAL_COST = TOTAL_COST - data1[position].itemcost.toInt()
            ToatlPrice.setText(TOTAL_COST.toString())
            TotalQuantity.setText(TOTAL_QUANTITY.toString())
        }

    }

    fun setViewCart(){
        val cartView = findViewById<Toolbar>(R.id.cart_view)
        cartView.visibility = View.VISIBLE
    }


}