package com.example.foodpanda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : AppCompatActivity() {
    private lateinit var authListener:FirebaseAuth.AuthStateListener
    private lateinit var auth:FirebaseAuth
    private var database = FirebaseDatabase.getInstance()
    private var myProfile_database: DatabaseReference? = database.getReference().child("MyProfile")
    private var myLoc_database: DatabaseReference? = database.getReference().child("MyLocation")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val user_name = findViewById<TextView>(R.id.profile_name)
        val user_email = findViewById<TextView>(R.id.profile_email)
        val user_mobileNo = findViewById<TextView>(R.id.profile_mobileNo)
        val address = findViewById<TextView>(R.id.profile_address)
        var signOut = findViewById<TextView>(R.id.signoutBtn)
        myProfile_database?.child(userId)?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var name = snapshot.child("username").getValue()
                var email = snapshot.child("useremail").getValue()
                var mobileNum = snapshot.child("userMobileNumber").getValue()
                user_name.setText(name.toString())
                user_email.setText(email.toString())
                user_mobileNo.setText(mobileNum.toString())
            }

        })

        myLoc_database?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    address.text = item.child("address").getValue().toString()
                }
            }
        })


        signOut.setOnClickListener {
            auth.signOut()
            finishAffinity()
            startActivity(Intent(this,Login::class.java))

        }




    }



}