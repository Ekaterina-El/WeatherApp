package el.ka.weatherapp.data.model

data class City(
  val id: Long? = null,
  val name: String = "",
  val type: CityType? = null,
  val tempType: TemperatureType? = null,
  val temps: List<Double>? = null
) {
  fun convertToSpinnerItem(): SpinnerItem = SpinnerItem(id!!, name, null)
}