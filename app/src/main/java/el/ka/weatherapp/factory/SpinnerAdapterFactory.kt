package el.ka.weatherapp.factory

import android.content.Context
import android.widget.SpinnerAdapter
import el.ka.weatherapp.adapter.SpinnerWithIconAdapter
import el.ka.weatherapp.data.model.Fields
import el.ka.weatherapp.data.model.SpinnerItem
import el.ka.weatherapp.data.model.SpinnerType

class SpinnerAdapterFactory(private val context: Context) {
  fun getSpinnerItems(spinnerType: SpinnerType): List<SpinnerItem> {
    return when (spinnerType) {
      SpinnerType.YEAR_SEASONS -> Fields.yearSeasons
      SpinnerType.CITY_TYPE -> Fields.cityTypes
      SpinnerType.TEMPERATURE_TYPE -> Fields.temperatureType
      else -> throw RuntimeException("$spinnerType - is unknown SpinnerType")
    }
  }

  fun getSpinnerAdapter(spinnerType: SpinnerType): SpinnerAdapter {
    val items = getSpinnerItems(spinnerType)
    return when (spinnerType) {
      SpinnerType.YEAR_SEASONS,
      SpinnerType.CITY_TYPE,
      SpinnerType.TEMPERATURE_TYPE -> SpinnerWithIconAdapter(context, items)
      else -> throw RuntimeException("$spinnerType - is unknown SpinnerType")
    }
  }
}