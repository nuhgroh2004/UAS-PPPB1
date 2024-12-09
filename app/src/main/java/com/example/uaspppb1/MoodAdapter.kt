package com.example.uaspppb1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uaspppb1.Model.Mood

class MoodAdapter(private val moodList: List<Mood>, private val onEditClick: (Mood) -> Unit, private val onDeleteClick: (Mood) -> Unit) :
    RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTime: TextView = itemView.findViewById(R.id.text_time)
        val textFeeling: TextView = itemView.findViewById(R.id.text_feeling)
        val imgPlaceholder: ImageView = itemView.findViewById(R.id.img_placeholder)
        val btnEdit: ImageView = itemView.findViewById(R.id.btn_edit)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moodList[position]
        holder.textTime.text = mood.timestamp
        holder.textFeeling.text = mood.mood

        // Set the appropriate image based on the mood
        val moodImageRes = when (mood.mood) {
            "Sedih" -> R.drawable.sedih
            "Marah" -> R.drawable.marah
            "Bahagia" -> R.drawable.bahagia
            else -> R.drawable.circle_placeholder
        }
        holder.imgPlaceholder.setImageResource(moodImageRes)

        holder.btnEdit.setOnClickListener { onEditClick(mood) }
        holder.btnDelete.setOnClickListener { onDeleteClick(mood) }
    }

    override fun getItemCount(): Int = moodList.size
}