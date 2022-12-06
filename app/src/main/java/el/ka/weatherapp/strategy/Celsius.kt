package el.ka.weatherapp.strategy

class Celsius: TemperatureStrategy {
  override fun convertToCelsius(number: Double) = number
  override fun convertToFahrenheit(number: Double) = (number * 9 / 5) + 32
  override fun convertToKelvin(number: Double) = number + 273.15
}
