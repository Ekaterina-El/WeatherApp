package el.ka.weatherapp.data.model

data class City(
  var id: Long? = null,
  var name: String = "",
  var type: CityType? = null,
  var tempType: TemperatureType? = null,
  var temps: List<Double>? = null
) {
  fun convertToSpinnerItem(): SpinnerItem = SpinnerItem(id!!, name, null)
}