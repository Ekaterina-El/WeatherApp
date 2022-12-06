package el.ka.weatherapp.strategy

interface TemperatureStrategy {
  fun convertToCelsius(number: Double): Double
  fun convertToFahrenheit(number: Double): Double
  fun convertToKelvin(number: Double): Double
}