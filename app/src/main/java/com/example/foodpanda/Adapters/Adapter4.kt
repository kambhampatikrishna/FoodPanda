package com.example.foodpanda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.foodpanda.Models.ItemsModel
import android.widget.*
import com.example.foodpanda.Models.Image
import com.example.foodpanda.Models.OnClickInterface
import com.example.foodpanda.R
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class Adapter4(private val context: Context, private var data:List<ItemsModel>,val clicklistener: OnClickInterface): RecyclerView.Adapter<Adapter4.ImageViewHolder>(),
    Filterable {
    var filterdata:List<ItemsModel> = data

    class ImageViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val itemimg=itemView.findViewById<ImageView>(R.id.itemimage)
        val itemname=itemView.findViewById<TextView>(R.id.itemname)
        val itemcost=itemView.findViewById<TextView>(R.id.itemcost)
        val quantity = itemView.findViewById<TextView>(R.id.t_count)
//        val addBtn = itemView.findViewById<Button>(R.id.additem)
        val category:ImageView = itemView.findViewById(R.id.type)
        val plus = itemView.findViewById<ImageView>(R.id.i_plus)
        val minus = itemView.findViewById<ImageView>(R.id.i_minus)
        fun bindView(image: ItemsModel)
        {
            Picasso.get().load(image.imagefood).into(itemimg)
            itemname.setText(image.itemname)
            itemcost.setText(image.itemcost)
            Picasso.get().load(image.category).into(category)

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.items_row_view,parent,false))


    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindView(data[position])

//        holder.addBtn.setOnClickListener {
//            clicklistener.onAdd(position)
//
//        }

        holder.plus.setOnClickListener{

            val quantity1 = (holder.quantity.text.toString().toInt()+1).toString()
            data[position].item_quantity += 1
            holder.quantity.setText(quantity1)

            clicklistener.addQuantity(position,quantity1)
        }
        holder.minus.setOnClickListener {

            var quantity1 = holder.quantity.text.toString().toInt()
            if(quantity1 <= 0 ){

            }
            else {
                clicklistener.decreaseQuantity(position,quantity1.toString())
                quantity1 -= 1
                data[position].item_quantity  -= 1
                holder.quantity.setText(quantity1.toString())

            }


//            clicklistener.decreaseQuantity(position)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val search=constraint.toString()
                if(search.isEmpty())
                {
                    data=filterdata
                }
                else
                {
                    val result= ArrayList<ItemsModel>()
                    for (row in data)
                    {
                        if (row.itemcost.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)))
                        {
                            result.add(row)
                        }

                    }
                    data=result

                }
                val filterresults=FilterResults()
                filterresults.values=data
                return filterresults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    data=results.values as ArrayList<ItemsModel>
                }
            }

        }
    }

}
