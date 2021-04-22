package com.example.foodpanda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodpanda.Models.OnClickInterface
import com.example.foodpanda.R
import com.example.foodpanda.Models.RestaurantDataModel
import com.squareup.picasso.Picasso

class Adapter3(private val context: Context, private val data:List<RestaurantDataModel>, val clicklistener: OnClickInterface): RecyclerView.Adapter<Adapter3.ImageViewHolder>() {

    class ImageViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val resimg=itemView.findViewById<ImageView>(R.id.imageView)
        val restname=itemView.findViewById<TextView>(R.id.textView)
        val restadd=itemView.findViewById<TextView>(R.id.textView2)
        val resratingim=itemView.findViewById<ImageView>(R.id.imageView2)
        val resrating=itemView.findViewById<TextView>(R.id.textView3)
        val cost=itemView.findViewById<TextView>(R.id.textView4)
        val dist:Button = itemView.findViewById(R.id.distance)

        fun bindView(image: RestaurantDataModel)
        {
            Picasso.get().load(image.imagesrc1).into(resimg)
            restname.setText(image.name)
            restadd.setText(image.location)
            resratingim.setImageResource(image.imagesrc2)
            resrating.setText(image.rating.toString())
            cost.setText(image.cost)
            dist.setText(image.distance)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurants_row_view,parent,false))


    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindView(data[position])
        holder.itemView.setOnClickListener {
            clicklistener.onClick(position)
        }

    }

}


