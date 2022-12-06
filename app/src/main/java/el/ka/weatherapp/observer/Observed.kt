package el.ka.weatherapp.observer

interface Observed {
  fun addListener(observer: Observer)
  fun removeListener(observer: Observer)
  fun notifyObservers()
}