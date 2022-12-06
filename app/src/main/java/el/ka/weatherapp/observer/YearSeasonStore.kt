package el.ka.weatherapp.observer

import el.ka.weatherapp.data.model.YearSeasonType

class YearSeasonStore: Observed {
  private var yearSeasonType: YearSeasonType? = null
  private val observers = mutableListOf<Observer>()

  fun getYearSeasonType() = yearSeasonType

  fun updateYearSeasonType(type: YearSeasonType) {
    yearSeasonType = type
    println(type)
    notifyObservers()
  }

  override fun addListener(observer: Observer) {
    observers.add(observer)
    observer.notify(this)
  }

  override fun removeListener(observer: Observer) {
    observers.remove(observer)
  }

  override fun notifyObservers() {
    observers.forEach { it.notify(this) }
  }
}