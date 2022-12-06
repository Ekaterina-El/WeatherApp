package el.ka.weatherapp.factory

import android.content.Context
import android.widget.SpinnerAdapter
import el.ka.weatherapp.adapter.YearSeasonAdapter
import el.ka.weatherapp.data.model.SpinnerType
import el.ka.weatherapp.data.model.YearSeasonType

class SpinnerAdapterFactory(private val context: Context) {
  fun getSpinnerAdapter(spinnerType: SpinnerType): SpinnerAdapter {
    return when (spinnerType) {
      SpinnerType.YEAR_SEASONS -> YearSeasonAdapter(context, YearSeasonType.values().toList())
      else -> throw RuntimeException("$spinnerType - is unknown SpinnerType")
    }
  }
}