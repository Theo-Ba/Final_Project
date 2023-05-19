package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.LoginActivity.Companion.EXTRA_USERID
import com.squareup.picasso.Picasso
import kotlinx.coroutines.NonDisposableHandle.parent
import weborb.util.ThreadContext.context

class CollectionAdapter(var dataSet: MutableList<Character>, var userId: String) :
    RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    companion object {
        val EXTRA_CHARACTER = "character"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewMaxHp: TextView
        val textViewAbility1: TextView
        val textViewAbility2: TextView
        val imageViewIcon: ImageView
        val textViewClass: TextView
        val layout: ConstraintLayout
        val textViewSupportEquipped: TextView
        val textViewEquipped: TextView

        init {
            textViewName = view.findViewById(R.id.textView_charItem_name)
            textViewMaxHp = view.findViewById(R.id.textView_charItem_health)
            textViewAbility1 = view.findViewById(R.id.textView_charItem_ability1)
            textViewAbility2 = view.findViewById(R.id.textView_charItem_ability2)
            imageViewIcon = view.findViewById(R.id.imageView_charItem_icon)
            textViewClass = view.findViewById(R.id.textView_charItem_class)
            layout = view.findViewById(R.id.layout_charItem)
            textViewSupportEquipped = view.findViewById(R.id.textView_charItem_supportEquipped)
            textViewEquipped = view.findViewById(R.id.textView_charItem_equipped)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_character_data, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val character = dataSet[position]
        val context = viewHolder.textViewClass.context
        viewHolder.textViewName.text = character.name
        viewHolder.textViewMaxHp.text = "Max Health: ${character.health}"
        viewHolder.textViewAbility1.text = "${character.ability1}: ${character.ability1Description}"
        viewHolder.textViewAbility2.text = "${character.ability2}: ${character.ability2Description}"
        viewHolder.textViewClass.text = "${character.archetype}"
        viewHolder.textViewSupportEquipped.text = "Support Equipped: ${character.supportEquipped}"
        if(character.equipped == "") {
            viewHolder.textViewEquipped.text = ""
        }
        else {
            viewHolder.textViewEquipped.text = "Equipped to ${character.equipped}"
        }
        Picasso.with(context).load(character.imageAddress).fit()
            .into(viewHolder.imageViewIcon)

        viewHolder.layout.setOnClickListener {
            val detailIntent = Intent(context, CharacterDetailActivity::class.java)
            detailIntent.putExtra(EXTRA_CHARACTER, character)
            detailIntent.putExtra(EXTRA_USERID, userId)
            context.startActivity(detailIntent)
        }
    }

    override fun getItemCount() = dataSet.size
}