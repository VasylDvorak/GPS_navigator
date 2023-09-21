package com.gps_navigator.view.markers

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.RecyclerviewItemBinding
import com.google.android.gms.maps.model.MarkerOptions

class ListMarkersAdapter(
    private var onListItemClickListener: (MarkerOptions) -> Unit,
    private var correctClickListener: (Int, MarkerOptions) -> Unit,
    private var callbackRemove: (Int, MarkerOptions) -> Unit
) : RecyclerView.Adapter<ListMarkersAdapter.RecyclerItemViewHolder>(), ItemTouchHelperAdapter {

    private var data: MutableList<MarkerOptions> = mutableListOf()
    fun setData(data: MutableList<MarkerOptions>) {
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
                        titleMarkersRecyclerItem.setText(data.title)
                        annotationRecyclerItem.setText(data.snippet)
                        coordinatesRecyclerItem.text =
                            "Latitude: " + data.position.latitude.toString() + "\nLongitude: " + data.position.longitude.toString()
                        setOnClickListener { openInNewWindow(data) }
                        approve.visibility = View.GONE
                        titleMarkersRecyclerItem.inputType = InputType.TYPE_NULL
                        annotationRecyclerItem.inputType = InputType.TYPE_NULL
                        titleMarkersRecyclerItem.setOnLongClickListener {
                            editTitleAnnotation(
                                titleMarkersRecyclerItem,
                                approve,
                                layoutPosition,
                                true
                            )
                            true
                        }
                        annotationRecyclerItem.setOnLongClickListener {
                            editTitleAnnotation(
                                annotationRecyclerItem,
                                approve,
                                layoutPosition,
                                false
                            )
                            true
                        }
                    }
                }
            }
        }
    }

    private fun editTitleAnnotation(
        editTextView: AppCompatEditText,
        apply: ImageView,
        layoutPosition: Int,
        titleCorrect: Boolean
    ) {

        editTextView.inputType = InputType.TYPE_CLASS_TEXT
        apply.visibility = View.VISIBLE
        var newText = editTextView.text.toString()

        editTextView.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    newText = charSequence.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            apply.setOnClickListener {
                if (titleCorrect) {
                    data[layoutPosition] =
                        MarkerOptions()
                            .position(data[layoutPosition].position)
                            .title(newText)
                            .snippet(data[layoutPosition].snippet)
                            .draggable(true)
                } else {
                    data[layoutPosition] =
                        MarkerOptions()
                            .position(data[layoutPosition].position)
                            .title(data[layoutPosition].title)
                            .snippet(newText)
                            .draggable(true)
                }
                editTextView.hideKeyboard()
                apply.visibility = View.GONE
                editTextView.inputType = InputType.TYPE_NULL
                changeMarkerOptionsItem(layoutPosition, data[layoutPosition])
            }
        }
    }

    // Расширяем функционал вью для скрытия клавиатуры
    fun View.hideKeyboard(): Boolean {
        try {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } catch (ignored: RuntimeException) {
        }
        return false
    }

    private fun openInNewWindow(listItemData: MarkerOptions) {
        onListItemClickListener(listItemData)
    }

    private fun changeMarkerOptionsItem(layoutPosition: Int, listItemData: MarkerOptions) {
        correctClickListener(layoutPosition, listItemData)
    }

    override fun onItemDelete(position: Int) {
        callbackRemove(position, data.get(position))
    }

}
