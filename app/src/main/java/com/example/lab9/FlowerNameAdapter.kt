package com.example.lab9

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.BaseAdapter
import com.example.lab9.databinding.FlowerListItem1Binding
import com.example.lab9.databinding.FlowerListItemBinding

class FlowerNameAdapter : BaseAdapter{
    lateinit var context: Context
    lateinit var flowerNames: ArrayList<String>
    lateinit var flowerImages: ArrayList<Int>

    constructor(context: Context,flowerNames: ArrayList<String>, flowerImages: ArrayList<Int>): super(){
        this.context = context
        this.flowerNames = flowerNames
        this.flowerImages = flowerImages
    }

    override fun getCount(): Int {
        return flowerNames.size
    }
    override fun getItem(position: Int): Any? {
        return flowerNames[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {

        val binding: FlowerListItem1Binding
        val itemView = if (convertView == null){
            binding = FlowerListItem1Binding.inflate(LayoutInflater.from(context),parent,false)
            binding.root.tag = binding
            binding.root
        }else{
            binding = convertView.tag as FlowerListItem1Binding
            convertView
        }
        binding.flowerName.text = getItem(position) as CharSequence?
        binding.flowerImage.setImageResource(flowerImages[position])

//        if (position %2 == 0){
//            itemView.setBackgroundColor(android.graphics.Color.GREEN)
//        } else{
//            itemView.setBackgroundColor(android.graphics.Color.LTGRAY)
//        }
        return itemView
    }

}