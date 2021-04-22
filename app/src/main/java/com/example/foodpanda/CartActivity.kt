package com.example.foodpanda

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodpanda.Adapters.Adapter4
import com.example.foodpanda.Adapters.CartAdapter
import com.example.foodpanda.Models.CartModel
import com.example.foodpanda.Models.ItemsModel
import com.example.foodpanda.Models.OnClickInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CartActivity : AppCompatActivity() ,OnClickInterface{
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = auth.currentUser!!.uid
    private var database = FirebaseDatabase.getInstance()
    private var order_database: DatabaseReference? = database.getReference().child("Cart")
    private var myorders_database: DatabaseReference? = database.getReference().child("MyOrders").child(userId)
    lateinit var rest_name1:String
    lateinit var rest_loc1:String
    var items:String = ""
    var TOTAL_QUANTITY = 0
    var TOTAL_COST = 0
    var order_no = 0
    var data1= ArrayList<CartModel>()
    lateinit var adapt:CartAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_activity_layout)

        TOTAL_COST = intent.getStringExtra("TOTAL_COST").toString().toInt()
        var TOTAL_QUANTITY = intent.getStringExtra("TOTAL_QUANTITY")
        val placeOrder = findViewById<Button>(R.id.placeOrder)
        val totalAmount = findViewById<TextView>(R.id.t_total)
        val youPay = findViewById<TextView>(R.id.t_grand_total)
        totalAmount.setText(TOTAL_COST.toString())
        youPay.setText(TOTAL_COST.toString())
        order_database?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    data1.add(CartModel(data.child("item_name").getValue().toString(),data.child("item_price").getValue().toString(),data.child("item_quantity").getValue().toString().toInt(),data.child("rest_name").getValue().toString(),data.child("rest_loc").getValue().toString(),data.child("category").getValue().toString()))
                }
                adapt.notifyDataSetChanged()
            }

        })
        val recyclervieww= findViewById<RecyclerView>(R.id.cart_list)
        recyclervieww.layoutManager= LinearLayoutManager(this)
        recyclervieww.setHasFixedSize(true)
        adapt = CartAdapter(this,data1,this)
        recyclervieww.adapter = adapt

        placeOrder.setOnClickListener {

            val hashMap = HashMap<String,String>()

           val order_date:Date = Calendar.getInstance().time
            val currentDateTime = LocalDateTime.now()
            val date=currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            val time=currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val date_time=date+" at "+ time

            for(item in data1){
                     items += item.item_quantity.toString() + " X " +item.itemname.toString()+"\n"
                     rest_name1 =  item.rest_name
                     rest_loc1 = item.rest_loc
                hashMap.put("items_list",items)
                hashMap.put("rest_name",rest_name1)
                hashMap.put("rest_loc",rest_loc1)
                hashMap.put("order_date",date_time.toString())
                hashMap.put("total_cost",TOTAL_COST.toString())

            }

            myorders_database?.push()?.setValue(hashMap)
            val intent = Intent(this,PaymentActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onClick(name: Int) {
        TODO("Not yet implemented")
    }

    override fun onAdd(addItemIndex: Int) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun addQuantity(position: Int, quantity:String) {
        val ToatlPrice = findViewById<TextView>(R.id.t_total)
        val youPay = findViewById<TextView>(R.id.t_grand_total)
        TOTAL_COST = TOTAL_COST+data1[position].itemcost.toInt()
        ToatlPrice.setText(TOTAL_COST.toString())
        youPay.setText(TOTAL_COST.toString())
        data1[position].item_quantity  = quantity.toInt()

    }

    @SuppressLint("SetTextI18n")
    override fun decreaseQuantity(position: Int, quantity: String) {
        val youPay = findViewById<TextView>(R.id.t_grand_total)
        val ToatlPrice = findViewById<TextView>(R.id.t_total)
        if(quantity.toInt()<=0){ }
        else {
            TOTAL_COST = TOTAL_COST - data1[position].itemcost.toInt()
            ToatlPrice.setText(TOTAL_COST.toString())
            youPay.setText(TOTAL_COST.toString())
        }
        data1[position].item_quantity  = quantity.toInt()



    }


}