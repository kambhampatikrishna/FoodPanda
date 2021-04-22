package com.example.foodpanda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodpanda.Adapters.CartAdapter
import com.example.foodpanda.Adapters.MyOrdersAdapter
import com.example.foodpanda.Models.CartModel
import com.example.foodpanda.Models.OrderHistoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderHistory : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = auth.currentUser!!.uid
    lateinit var adapt:MyOrdersAdapter
    private var database = FirebaseDatabase.getInstance()
    private var myorders_database: DatabaseReference? = database.getReference().child("MyOrders").child(userId)

    var data1= ArrayList<OrderHistoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        myorders_database?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {
                        data1.add(OrderHistoryModel(data.child("items_list").getValue().toString(), data.child("total_cost").getValue().toString(), data.child("rest_name").getValue().toString(), data.child("rest_loc").getValue().toString(), data.child("order_date").getValue().toString()))

                    }

                    adapt.notifyDataSetChanged()
                }



        })
        val recyclervieww= findViewById<RecyclerView>(R.id.order_history)
        recyclervieww.layoutManager= LinearLayoutManager(this)
        recyclervieww.setHasFixedSize(true)
        adapt = MyOrdersAdapter(this,data1)
        recyclervieww.adapter = adapt

    }
}