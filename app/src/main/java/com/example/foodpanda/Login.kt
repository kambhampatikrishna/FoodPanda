package com.example.foodpanda

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login2)
        val sighninbutton: TextView = findViewById(R.id.textViewSignUp)
        val login: Button = findViewById(R.id.btnlogin)
        auth = FirebaseAuth.getInstance()
        sighninbutton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        login.setOnClickListener {
            doLogin()
        }

    }



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }

    }
   private fun updateUI(currentUser:FirebaseUser?){
        if(currentUser!=null){
            startActivity(Intent(this,LocationActivity::class.java))

        }
       else{
            Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show()
        }

    }
    private fun doLogin(){
        val email = findViewById<EditText>(R.id.inputEmail)
        val pwd = findViewById<EditText>(R.id.inputPassword)
        if(email.text.isEmpty()){
            email.error= "Please enter the email"
            email.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error= "Please enter Valid Mail Id"
            email.requestFocus()
            return
        }
        if(pwd.text.isEmpty()){
            email.error= "Please enter the password"
            email.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(email.text.toString(), pwd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                   val user = auth.currentUser
                    if(user!=null) {
                        updateUI(user)
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }




    }

}