package el.ka.weatherapp.strategy

class Fahrenheit: TemperatureStrategy {
  override fun convertToCelsius(number: Double) = (number - 32) * 5/9
  override fun convertToFahrenheit(number: Double) = number
  override fun convertToKelvin(number: Double) = convertToCelsius(number) + 273.15
}
