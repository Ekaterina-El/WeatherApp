package el.ka.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import el.ka.weatherapp.MainActivity
import el.ka.weatherapp.R
import el.ka.weatherapp.data.model.City
import el.ka.weatherapp.data.model.SpinnerItem
import el.ka.weatherapp.data.model.SpinnerType
import el.ka.weatherapp.data.model.YearSeasonType
import el.ka.weatherapp.observer.Observed
import el.ka.weatherapp.observer.ObservedValue
import el.ka.weatherapp.observer.Observer
import el.ka.weatherapp.ui.CityFragment.Companion.CITY_ID

// TODO: Удаление города
// TODO: Решить проблему с возвратом на экран city (не загружаются данные)
// TODO: Выводить средней температуры выбраного сезона в выбранном городе
// TODO: Экспорт в Excel 

class FirstFragment : Fragment(R.layout.first_fragment) {
  private val ctx by lazy { requireContext() as MainActivity }
  private val mView by lazy { requireView() }
  private val navController by lazy { findNavController() }

  private val imageViewDeleteCity: ImageView by lazy { mView.findViewById(R.id.imageViewDeleteCity) }
  private val deleteCityListener by lazy {
    OnClickListener {
      val cityId = (currentCity.getValue() as City).id
      ctx.cityStore.deleteCity(cityId)
      loadCities()
    }
  }

  private val imageViewEditCity: ImageView by lazy { mView.findViewById(R.id.imageViewEditCity) }
  private val editCityListener by lazy {
    OnClickListener {
      val city = currentCity.getValue() as? City
      val cityId = city?.id.toString()
      navigateToCityFragment(cityId)
    }
  }

  private val spinnerCity: Spinner by lazy { mView.findViewById(R.id.spinnerCity) }
  private val currentCity by lazy { ObservedValue() }
  private val spinnerCityListener by lazy {
    object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val cityId =
          (parent!!.getItemAtPosition(pos) as SpinnerItem).programValue as Long  // Get city Id
        loadCityDate(cityId)
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerCity.setSelection(0)
      }
    }
  }

  private val allCities by lazy { ObservedValue() }
  private val allCitiesObserver by lazy {
    object : Observer {
      override fun notify(observed: Observed) {
        val a =
          ((observed as ObservedValue).getValue() as List<*>).mapNotNull { if (it is City) it else null }

        imageViewEditCity.visibility = if (a.isEmpty())  View.GONE else View.VISIBLE
        imageViewDeleteCity.visibility = if (a.isEmpty())  View.GONE else View.VISIBLE

        updateCitySpinner()
        if (a.isNotEmpty()) spinnerCity.setSelection(0)
      }
    }
  }

  private val buttonAdd by lazy { mView.findViewById<ConstraintLayout>(R.id.buttonAdd) }
  private val buttonAddListener by lazy { OnClickListener { navigateToCityFragment() } }

  private val spinnerYearSeason: Spinner by lazy { mView.findViewById(R.id.spinnerYearSeason) }
  private val yearSeasonStore by lazy { ObservedValue() }
  private val spinnerYearSeasonListener by lazy {
    object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        yearSeasonStore.updateValue((parent!!.getItemAtPosition(pos) as SpinnerItem).programValue)
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerYearSeason.setSelection(0)
      }
    }
  }


  private val textViewCityType: TextView by lazy { mView.findViewById(R.id.textViewCityType) }
  private val textViewArgTemperature: TextView by lazy { mView.findViewById(R.id.textViewArgTemperature) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initSpinners()
    loadCities()
  }

  private fun loadCities() {
    val cities = ctx.cityStore.getCities()
    allCities.updateValue(cities)
  }

  private fun initSpinners() {
    spinnerYearSeason.adapter =
      ctx.spinnerAdapterFactory.getSpinnerAdapter(SpinnerType.YEAR_SEASONS)
  }

  private fun updateCitySpinner() {
    spinnerCity.adapter = ctx.spinnerAdapterFactory.getSpinnerAdapter(
      SpinnerType.CITIES,
      allCities.getValue() as List<*>
    )
  }

  private fun loadCityDate(cityId: Long) {
    val city = ctx.cityStore.getCityById(cityId) ?: return
    currentCity.updateValue(city)
  }

  private val updatedCityDate by lazy {
    object : Observer {
      override fun notify(observed: Observed) {
        val city = currentCity.getValue() as? City
        val yearSeason = yearSeasonStore.getValue() as? YearSeasonType

        if (city == null) return
        textViewCityType.setText(city.type!!.stringIdx)

        if (yearSeason == null) return
        textViewArgTemperature.text = calculateAverageTemperature(city.temps, yearSeason)
      }

    }
  }

  private fun calculateAverageTemperature(
    temps: List<Double>?,
    yearSeason: YearSeasonType
  ): String {
    return "+34℃"
  }

  private fun navigateToCityFragment(cityId: String? = null) {
    val bundle = Bundle()
    bundle.putString(CITY_ID, cityId)
    ctx.navigateTo(navController, R.id.action_firstFragment_to_cityFragment, bundle)
  }

  override fun onResume() {
    super.onResume()
    spinnerYearSeason.onItemSelectedListener = spinnerYearSeasonListener
    spinnerCity.onItemSelectedListener = spinnerCityListener

    buttonAdd.setOnClickListener(buttonAddListener)
    imageViewEditCity.setOnClickListener(editCityListener)
    imageViewDeleteCity.setOnClickListener(deleteCityListener)

    allCities.addListener(allCitiesObserver)
    currentCity.addListener(updatedCityDate)
    yearSeasonStore.addListener(updatedCityDate)
  }

  override fun onStop() {
    super.onStop()
    spinnerYearSeason.onItemSelectedListener = null
    spinnerCity.onItemSelectedListener = null
    allCities.removeListener(allCitiesObserver)
    currentCity.removeListener(updatedCityDate)
    yearSeasonStore.removeListener(updatedCityDate)

    buttonAdd.setOnClickListener(null)
    imageViewEditCity.setOnClickListener(null)
  }
}