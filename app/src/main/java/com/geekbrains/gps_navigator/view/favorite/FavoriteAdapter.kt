package com.geekbrains.gps_navigator.view.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.RecyclerviewItemBinding
import com.google.android.gms.maps.model.MarkerOptions

private const val delayButton = 400L

class FavoriteAdapter(
    private var onListItemClickListener: (MarkerOptions) -> Unit,
    private var playArticulationClickListener: (String) -> Unit,
    private var callbackRemove: (Int, MarkerOptions) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.RecyclerItemViewHolder>(), ItemTouchHelperAdapter {

    private var data: MutableList<MarkerOptions> = mutableListOf()
    fun setData(data: MutableList<MarkerOptions>) {
        this.data = mutableListOf()
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding =
            RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(val binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onItemSelected() {
            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context, R.color.white
                )
            )
        }

        fun onItemRelease() {
            itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.white
                    )
                )
        }

        fun bind(data: MarkerOptions) {
            if (layoutPosition != RecyclerView.NO_POSITION) {

                itemView.apply {
                    binding.apply {
                        cardView.setCardBackgroundColor(resources.getColor(R.color.white))
                        headerTextviewRecyclerItem.text = data.title
                        descriptionTextviewRecyclerItem.text = data.snippet
                        transcriptionTextviewRecyclerItem.text =
                            "Latitude: " + data.position.latitude.toString() + "\nLongitude: " + data.position.longitude.toString()
                        setOnClickListener { openInNewWindow(data) }
                        playArticulation.setOnClickListener {
                            it?.apply {
                                isEnabled = false
                                postDelayed({ isEnabled = true }, delayButton)
                            }
//                            data.meanings?.get(0)?.soundUrl?.let { soundUrl ->
//                                playArticulationClickListener(soundUrl)
                            //   }
                        }
                    }
                }
            }
        }
    }

    private fun openInNewWindow(listItemData: MarkerOptions) {
        onListItemClickListener(listItemData)
    }


    override fun onItemDismiss(position: Int) {
        callbackRemove(position, data.get(position))
    }

}
