package el.ka.weatherapp.factory

import android.content.Context
import android.widget.SpinnerAdapter
import el.ka.weatherapp.adapter.SpinnerWithIconAdapter
import el.ka.weatherapp.data.model.*

class SpinnerAdapterFactory(private val context: Context) {
  private fun getSpinnerItems(spinnerType: SpinnerType): List<SpinnerItem> {
    return when (spinnerType) {
      SpinnerType.YEAR_SEASONS -> Fields.yearSeasons
      SpinnerType.CITY_TYPE -> Fields.cityTypes
      SpinnerType.TEMPERATURE_TYPE -> Fields.temperatureType
      else -> throw RuntimeException("$spinnerType - is unknown SpinnerType")
    }
  }

  fun getSpinnerAdapter(spinnerType: SpinnerType, values: List<*>? = null): SpinnerAdapter {
    val items =
      if (values == null) getSpinnerItems(spinnerType) else convertValueToItems(spinnerType, values)
    return when (spinnerType) {
      SpinnerType.YEAR_SEASONS,
      SpinnerType.CITY_TYPE,
      SpinnerType.TEMPERATURE_TYPE,
      SpinnerType.CITIES -> SpinnerWithIconAdapter(context, items)
      else -> throw RuntimeException("$spinnerType - is unknown SpinnerType")
    }
  }

  private fun convertValueToItems(spinnerType: SpinnerType, values: List<*>): List<SpinnerItem> {
    return when (spinnerType) {
      SpinnerType.CITIES -> values.mapNotNull { if (it is City) it.convertToSpinnerItem() else null }
      else -> throw RuntimeException("$spinnerType - is unknown SpinnerType")
    }
  }

  fun getItemPosition(spinnerType: SpinnerType, type: Any): Int {
    val items = getSpinnerItems(spinnerType)
    return items.indexOfFirst { it.programValue == type }
  }
}