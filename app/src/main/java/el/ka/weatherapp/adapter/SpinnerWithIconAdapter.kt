package el.ka.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import el.ka.weatherapp.R
import el.ka.weatherapp.data.model.SpinnerItem

class SpinnerWithIconAdapter(context: Context, private val items: List<SpinnerItem>) :
  ArrayAdapter<SpinnerItem>(context, 0, items), SpinnerAdapter {
  private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view = convertView ?: createView(parent, R.layout.spinner_item_with_icon)
    val item = items[position]
    inflateView(view, item)
    return view
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
    getView(position, convertView, parent)

  private fun inflateView(view: View, item: SpinnerItem) {
    val textViewValue = view.findViewById<TextView>(R.id.textViewValue)
    try {
      item.string.toInt()
      val text = view.context.getString(item.string.toInt())
      textViewValue.text = text
    } catch (_: NumberFormatException) {
      textViewValue.text = item.string
    }

    val imageView = view.findViewById<ImageView>(R.id.imageViewIcon)
    if (item.iconIdx != null) {
      val icon = ResourcesCompat.getDrawable(view.resources, item.iconIdx, context.theme)
      imageView.setImageDrawable(icon)
      imageView.visibility = View.VISIBLE
    } else imageView.visibility = View.GONE
  }

  private fun createView(parent: ViewGroup, layout: Int): View {
    return layoutInflater.inflate(layout, parent, false)
  }
}

