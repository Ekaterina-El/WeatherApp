package el.ka.weatherapp.strategy

class Kelvin: TemperatureStrategy {
  override fun convertToCelsius(number: Double) = number - 273.15
  override fun convertToFahrenheit(number: Double) = convertToCelsius(number) * 9 / 5 + 32
  override fun convertToKelvin(number: Double) = number
}