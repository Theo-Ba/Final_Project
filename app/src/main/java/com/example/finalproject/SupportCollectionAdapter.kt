package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SupportCollectionAdapter(var dataSet: MutableList<SupportCharacter>) :
    RecyclerView.Adapter<SupportCollectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewPower: TextView
        val textViewEquipped: TextView
        val imageViewIcon: ImageView
        init {
            textViewName = view.findViewById(R.id.textView_sc_name)
            textViewPower = view.findViewById(R.id.textView_sc_power)
            textViewEquipped = view.findViewById(R.id.textView_sc_equipped)
            imageViewIcon = view.findViewById(R.id.imageView_sc_icon)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_support_character, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val supportCharacter = dataSet[position]
        val context = viewHolder.textViewName.context
        viewHolder.textViewName.text = supportCharacter.name
        viewHolder.textViewPower.text = "Power: ${supportCharacter.power.toString()}"
        viewHolder.textViewEquipped.text = ""
        Picasso.with(context).load(supportCharacter.imageAddress).fit().into(viewHolder.imageViewIcon)
    }

    override fun getItemCount() = dataSet.size
}