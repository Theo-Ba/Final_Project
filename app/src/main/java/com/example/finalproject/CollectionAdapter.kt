package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CollectionAdapter(var dataSet: MutableList<Character>) :
    RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewMaxHp: TextView
        val textViewAbility1: TextView
        val textViewAbility2: TextView
        val imageViewIcon: ImageView
        val textViewClass: TextView

        init {
            textViewName = view.findViewById(R.id.textView_charItem_name)
            textViewMaxHp = view.findViewById(R.id.textView_charItem_health)
            textViewAbility1 = view.findViewById(R.id.textView_charItem_ability1)
            textViewAbility2 = view.findViewById(R.id.textView_charItem_ability2)
            imageViewIcon = view.findViewById(R.id.imageView_charItem_icon)
            textViewClass = view.findViewById(R.id.textView_charItem_class)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_character_data, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val character = dataSet[position]
        viewHolder.textViewName.text = character.name
        viewHolder.textViewMaxHp.text = "Max Health: ${character.health}"
        viewHolder.textViewAbility1.text = "${character.ability1}: ${character.ability1Description}"
        viewHolder.textViewAbility2.text = "${character.ability2}: ${character.ability2Description}"
        viewHolder.textViewClass.text = "${character.archetype}"
        viewHolder.imageViewIcon
    }

    override fun getItemCount() = dataSet.size
}