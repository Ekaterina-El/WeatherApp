package el.ka.weatherapp.data.model


enum class YearSeasonType(val months: Array<Int>) {
  SPRING(arrayOf(2, 3, 4)),
  SUMMER(arrayOf(5, 6, 7)),
  AUTUMN(arrayOf(8, 9, 10)),
  WINTER(arrayOf(11, 0, 1))
}

