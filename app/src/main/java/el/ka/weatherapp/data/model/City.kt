package el.ka.weatherapp.data.model

data class City(
  val name: String,
  val type: CityType,
  val tempType: TemperatureType,
  val temps: List<Double>
)