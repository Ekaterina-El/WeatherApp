package el.ka.weatherapp.data.model

import el.ka.weatherapp.R

object Fields {
  val yearSeasons by lazy {
    listOf(
      SpinnerItem(YearSeasonType.SPRING, R.string.spring, R.drawable.ic_spring),
      SpinnerItem(YearSeasonType.SUMMER, R.string.summer, R.drawable.ic_summer),
      SpinnerItem(YearSeasonType.AUTUMN, R.string.autumn, R.drawable.ic_autumn),
      SpinnerItem(YearSeasonType.WINTER, R.string.winter, R.drawable.ic_winter),
    )
  }

  val cityTypes by lazy {
    listOf(
      SpinnerItem(CityType.BIG, R.string.big_city, R.drawable.ic_big_city),
      SpinnerItem(CityType.MEDIUM, R.string.medium_city, R.drawable.ic_medium_city),
      SpinnerItem(CityType.SMALL, R.string.small_city, R.drawable.ic_small_town),
    )
  }

  val temperatureType by lazy {
    listOf(
      SpinnerItem(TemperatureType.CELSIUS, R.string.celsius, R.drawable.ic_celsius),
      SpinnerItem(TemperatureType.FAHRENHEIT, R.string.fahrenheit, R.drawable.ic_fahrenheit),
      SpinnerItem(TemperatureType.KELVIN, R.string.kelvin, R.drawable.ic_kelvin),
    )
  }
}