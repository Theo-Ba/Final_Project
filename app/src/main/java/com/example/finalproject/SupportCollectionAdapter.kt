package com.example.finalproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.squareup.picasso.Picasso

class SupportCollectionAdapter(var dataSet: MutableList<SupportCharacter>, var hereToEquip: Boolean, var characterEquippedTo: Character?) :
    RecyclerView.Adapter<SupportCollectionAdapter.ViewHolder>() {

    companion object {
        val TAG = "SupportAdapter"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewPower: TextView
        val textViewEquipped: TextView
        val imageViewIcon: ImageView
        val layout: ConstraintLayout
        init {
            textViewName = view.findViewById(R.id.textView_sc_name)
            textViewPower = view.findViewById(R.id.textView_sc_power)
            textViewEquipped = view.findViewById(R.id.textView_sc_equipped)
            imageViewIcon = view.findViewById(R.id.imageView_sc_icon)
            layout = view.findViewById(R.id.layout_sc)
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
        if(hereToEquip) {
            viewHolder.layout.setOnClickListener {
                val whereClause = "name = '${characterEquippedTo!!.name}'"
                val queryBuilder = DataQueryBuilder.create()
                queryBuilder.whereClause = whereClause
                Backendless.Data.of(Character::class.java).find(queryBuilder, object :
                    AsyncCallback<MutableList<Character?>?> {
                    override fun handleResponse(response: MutableList<Character?>?) {
                        response!![0]!!.supportEquipped = supportCharacter.name
                        response[0]!!.equippedSupportsPower = supportCharacter.power
                        Backendless.Data.of(Character::class.java)
                            .save(response[0], object : AsyncCallback<Character> {
                                override fun handleResponse(response: Character?) {
                                    Log.d(TAG, "${supportCharacter.name} equipped to ${response!!.name}")
                                    (context as Activity).finish()
                                }
                                override fun handleFault(fault: BackendlessFault?) {
                                    Log.d(TAG, "handleFault: ${fault!!.message}")
                                }
                            })
                    }
                    override fun handleFault(fault: BackendlessFault?) {
                        Log.d(TAG, "handleFault: ${fault!!.message}")
                    }
                })
            }
        }
    }

    override fun getItemCount() = dataSet.size
}