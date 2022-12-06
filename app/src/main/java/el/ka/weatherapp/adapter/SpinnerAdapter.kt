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
import el.ka.weatherapp.data.model.YearSeasonType

class YearSeasonAdapter(context: Context, private val items: List<YearSeasonType>) :
  ArrayAdapter<YearSeasonType>(context, 0, items), SpinnerAdapter {
  private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view = convertView ?: createView(parent, R.layout.spinner_item_with_icon)
    val item = items[position]
    inflateView(view, item)
    return view
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view = convertView ?: createView(parent, R.layout.spinner_item_with_icon)
    val item = items[position]
    inflateView(view, item)
    return view
  }

  private fun inflateView(view: View, item: YearSeasonType) {
    view.findViewById<TextView>(R.id.textViewValue).text =
      view.context.getString(item.strIdx)
    view.findViewById<ImageView>(R.id.imageViewIcon)
      .setImageDrawable(
        ResourcesCompat.getDrawable(
          view.resources,
          item.iconIdx,
          context.theme
        )
      )
  }

  private fun createView(parent: ViewGroup, layout: Int): View {
    return layoutInflater.inflate(layout, parent, false)
  }
}

