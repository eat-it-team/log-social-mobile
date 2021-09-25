package ru.ircover.socialmobile.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ircover.socialmobile.R
import ru.ircover.socialmobile.model.Period
import ru.ircover.socialmobile.model.Subsidy

class SubsidiesViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    constructor(viewGroup: ViewGroup)
            : this(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_subsidy, viewGroup, false))

    private val descriptionTextView = itemView.findViewById<TextView>(R.id.tvDescription)
    private val periodTextView = itemView.findViewById<TextView>(R.id.tvPeriod)

    fun bind(subsidy: Subsidy) {
        descriptionTextView.text = subsidy.description
        periodTextView.text = periodTextView.context.getString(when(subsidy.period) {
            Period.Once -> R.string.subsidy_period_once
            Period.Monthly -> R.string.subsidy_period_monthly
        })
    }
}

class SubsidiesAdapter : RecyclerView.Adapter<SubsidiesViewHolder>() {
    var subsidies: Array<Subsidy> = arrayOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubsidiesViewHolder(parent)

    override fun onBindViewHolder(holder: SubsidiesViewHolder, position: Int) {
        holder.bind(subsidies[position])
    }

    override fun getItemCount() = subsidies.size
}