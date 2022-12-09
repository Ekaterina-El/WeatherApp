package el.ka.weatherapp.strategy

import el.ka.weatherapp.data.model.TemperatureType
import kotlin.math.pow
import kotlin.math.roundToInt

class Convertor {
  private val celsius by lazy { Celsius() }
  private val fahrenheit by lazy { Fahrenheit() }
  private val kelvin by lazy { Kelvin() }

  fun convert(num: Double, from: TemperatureType, to: TemperatureType): Double {
    val strategy = when(from) {
      TemperatureType.CELSIUS -> celsius
      TemperatureType.FAHRENHEIT -> fahrenheit
      TemperatureType.KELVIN -> kelvin
    }
    return strategy.convertTo(num, to).roundTo()
  }

  private fun TemperatureStrategy.convertTo(value: Double, to: TemperatureType): Double {
    return when(to) {
      TemperatureType.CELSIUS -> this.convertToCelsius(value)
      TemperatureType.FAHRENHEIT -> this.convertToFahrenheit(value)
      TemperatureType.KELVIN -> this.convertToKelvin(value)
    }
  }


  fun Double.roundTo(number: Int = 2): Double {
    val a = 10.0.pow(number)
    return (this * a).roundToInt() / a
  }

  fun getUnit(toTemperatureType: TemperatureType): String {
    return when(toTemperatureType) {
      TemperatureType.CELSIUS -> "°C"
      TemperatureType.FAHRENHEIT -> "°F"
      TemperatureType.KELVIN -> "°K"
    }
  }
}