package com.example.foodpanda

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodpanda.Adapters.Adapter3
import com.example.foodpanda.Adapters.DashBoardoffersAdapter
import com.example.foodpanda.Models.Image
import com.example.foodpanda.Models.OnClickInterface
import com.example.foodpanda.Models.RestaurantDataModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class DashBoard : AppCompatActivity(), OnClickInterface {
    var data= ArrayList<RestaurantDataModel>()
    lateinit var adapt:Adapter3
    var rImage: ImageView? = null
    lateinit var city: String
    private var database = FirebaseDatabase.getInstance()
    private var root = database.getReference().child("restaurants")
    private var myLoc_database: DatabaseReference? = database.getReference().child("MyLocation")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        val loc_address = findViewById<TextView>(R.id.edittext1)

        myLoc_database?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    loc_address.text = item.child("address").getValue().toString()
                }
            }

        })
        root.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (value in snapshot.children){
                            data.add(
                                RestaurantDataModel(
                                    value.child("image").getValue().toString(),
                                    value.child("name").getValue().toString(),
                                    value.child("place").getValue().toString(),
                                    R.drawable.ic_star,
                                    value.child("rating").getValue().toString().toDouble(),
                                    value.child("min_price").getValue().toString(),
                                    value.child("res_id").getValue().toString().toInt(),
                                    value.child("distance").getValue().toString()
                                )
                            )
                }
                adapt.notifyDataSetChanged()

            }

        })
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.cart -> {
                    startActivity(Intent(applicationContext, OrderHistory::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.account -> {
                    startActivity(Intent(applicationContext, Profile::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            true
        })
        val imagess= listOf<Image>(
            Image(R.drawable.onee),
            Image(R.drawable.two),
            Image(R.drawable.three),
            Image(R.drawable.four),
            Image(R.drawable.five),
            Image(R.drawable.six)
        )
        val recycler= findViewById<RecyclerView>(R.id.recyclerview)
        val HLinearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler.layoutManager=LinearLayoutManager(this)
        recycler.layoutManager=HLinearLayoutManager
        recycler.setHasFixedSize(true)
        recycler.adapter = DashBoardoffersAdapter(this, imagess)
        val recycler1= findViewById<RecyclerView>(R.id.recyclerviewactivity)
        recycler1.layoutManager= LinearLayoutManager(this)
        recycler1.setHasFixedSize(true)
        adapt = Adapter3(this,data,this)
        recycler1.adapter = adapt
    }

    override fun onClick(position: Int) {
        val intent=Intent(Intent(this,MenuItemsActivity::class.java))
        intent.putExtra("restname",data[position].name)
        intent.putExtra("restrating",(data[position].rating).toString())
        intent.putExtra("cost",data[position].cost)
        intent.putExtra("res_id",(data[position].res_id).toString())
        intent.putExtra("rest_dist",data[position].distance)
        intent.putExtra("rest_location",data[position].location)
        startActivity(intent)
    }

    override fun onAdd(addItemIndex: Int) {

    }

    override fun addQuantity(position: Int,str:String) {
        TODO("Not yet implemented")
    }

    override fun decreaseQuantity(position: Int,quantity :  String) {
        TODO("Not yet implemented")
    }




}