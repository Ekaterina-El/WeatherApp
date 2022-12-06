package el.ka.weatherapp.observer

interface Observer {
  fun notify(observed: Observed)
}