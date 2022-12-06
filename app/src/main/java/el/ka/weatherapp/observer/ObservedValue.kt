package el.ka.weatherapp.observer

class ObservedValue: Observed {
  private var value: Any? = null
  private val observers = mutableListOf<Observer>()

  fun getValue() = value

  fun updateValue(value: Any) {
    this.value = value
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