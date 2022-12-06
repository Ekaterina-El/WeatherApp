package el.ka.weatherapp.data.model

data class SpinnerItem(val programValue: Any, val string: String, val iconIdx: Int? = null) {
  constructor(programValue: Any, string: Int, iconIdx: Int) :
      this(programValue, string.toString(), iconIdx)

}
