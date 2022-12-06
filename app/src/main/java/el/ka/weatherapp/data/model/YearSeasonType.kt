package el.ka.weatherapp.data.model

import el.ka.weatherapp.R

enum class YearSeasonType(val strIdx: Int, val iconIdx: Int) {
  SPRING(R.string.spring, R.drawable.ic_spring),
  SUMMER(R.string.summer, R.drawable.ic_summer),
  AUTUMN(R.string.autumn, R.drawable.ic_autumn),
  WINTER(R.string.winter, R.drawable.ic_winter)


}