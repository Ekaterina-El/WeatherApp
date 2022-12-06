package el.ka.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import el.ka.weatherapp.R
import el.ka.weatherapp.data.model.MonthTemperature

class MonthsAdapter(private val months: List<MonthTemperature>) :
  RecyclerView.Adapter<MonthViewHolder>() {
  private val viewHolders = mutableListOf<MonthViewHolder>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
    val view = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.month_tempr_input_item, parent, false)
    val viewHolder = MonthViewHolder(view)
    viewHolders.add(viewHolder)
    return viewHolder
  }

  override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
    val item = months[position]
    holder.bind(item)
  }

  override fun getItemCount() = months.size

  fun getMonthsTemperature() = viewHolders.map { it.getTemperature() }
}

class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  private val tempLayout by lazy { itemView.findViewById<TextInputLayout>(R.id.tempLayout) }
  private val tempValue by lazy { itemView.findViewById<TextInputEditText>(R.id.tempValue) }

  fun bind(item: MonthTemperature) {
    tempLayout.hint = item.month
    tempValue.setText(item.temperature.toString())
  }

  fun getTemperature(): Double = tempValue.text.toString().toDouble()
}
