package el.ka.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import el.ka.weatherapp.MainActivity
import el.ka.weatherapp.R
import el.ka.weatherapp.data.model.*
import el.ka.weatherapp.observer.Observed
import el.ka.weatherapp.observer.ObservedValue
import el.ka.weatherapp.observer.Observer
import el.ka.weatherapp.ui.CityFragment.Companion.CITY_ID

// TODO: Экспорт в Excel

class FirstFragment : Fragment() {
  private val ctx by lazy { requireContext() as MainActivity }
  private val navController by lazy { findNavController() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.first_fragment, container, false)
  }

  private lateinit var imageViewDeleteCity: ImageView
  private val deleteCityListener by lazy {
    OnClickListener {
      val cityId = (currentCity.getValue() as City).id
      ctx.cityStore.deleteCity(cityId)
      loadCities()
    }
  }

  private lateinit var imageViewEditCity: ImageView
  private val editCityListener by lazy {
    OnClickListener {
      val city = currentCity.getValue() as? City
      val cityId = city?.id.toString()
      navigateToCityFragment(cityId)
    }
  }

  private lateinit var spinnerCity: Spinner
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
        val a = ((observed as ObservedValue).getValue() as List<*>)
          .mapNotNull { if (it is City) it else null }

        imageViewEditCity.visibility = if (a.isEmpty()) View.GONE else View.VISIBLE
        imageViewDeleteCity.visibility = if (a.isEmpty()) View.GONE else View.VISIBLE

        updateCitySpinner()
        if (a.isNotEmpty()) spinnerCity.setSelection(0)
      }
    }
  }

  private lateinit var buttonAdd: ConstraintLayout
  private val buttonAddListener by lazy { OnClickListener {
    ctx.excelGenerator.save()
//    navigateToCityFragment()
  } }

  private lateinit var spinnerTempType: Spinner
  private val toTemperatureType by lazy { ObservedValue() }
  private val spinnerTempTypeListener by lazy {
    object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        toTemperatureType.updateValue((parent!!.getItemAtPosition(pos) as SpinnerItem).programValue)
      }

      override fun onNothingSelected(p0: AdapterView<*>?) { spinnerYearSeason.setSelection(0) }
    }
  }


  private lateinit var spinnerYearSeason: Spinner
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

  private fun initUI() {
    val mView = requireView()


    textViewArgTemperature = mView.findViewById(R.id.textViewArgTemperature)
    textViewCityType = mView.findViewById(R.id.textViewCityType)
    buttonAdd = mView.findViewById(R.id.buttonAdd)
    spinnerCity = mView.findViewById(R.id.spinnerCity)
    spinnerTempType = mView.findViewById(R.id.spinnerTempType)
    imageViewEditCity = mView.findViewById(R.id.imageViewEditCity)
    imageViewDeleteCity = mView.findViewById(R.id.imageViewDeleteCity)
    spinnerYearSeason = mView.findViewById(R.id.spinnerYearSeason)
  }


  private lateinit var textViewCityType: TextView
  private lateinit var textViewArgTemperature: TextView

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUI()
    initSpinners()
    loadCities()
  }

  private fun loadCities() {
    val cities = ctx.cityStore.getCities()
    allCities.updateValue(cities)
  }

  private fun initSpinners() {
    val factory = ctx.spinnerAdapterFactory
    spinnerYearSeason.adapter = factory.getSpinnerAdapter(SpinnerType.YEAR_SEASONS)
    spinnerTempType.adapter = factory.getSpinnerAdapter(SpinnerType.TEMPERATURE_TYPE)

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
        textViewArgTemperature.text = calculateAverageTemperature(city.temps, yearSeason, city.tempType!!)
      }

    }
  }

  private fun calculateAverageTemperature(
    temps: List<Double>?,
    yearSeason: YearSeasonType,
    currentTemperatureType: TemperatureType
  ): String {
    val toTemperatureType = this.toTemperatureType.getValue() as? TemperatureType

    if (temps == null || temps.size != 12 || toTemperatureType == null) return ""

    val temps = yearSeason.months.map { temps[it] }.average()
    val value = ctx.convertor.convert(temps, currentTemperatureType, toTemperatureType)
    val unit = ctx.convertor.getUnit(toTemperatureType)
    return "$value$unit"
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
    spinnerTempType.onItemSelectedListener = spinnerTempTypeListener

    buttonAdd.setOnClickListener(buttonAddListener)
    imageViewEditCity.setOnClickListener(editCityListener)
    imageViewDeleteCity.setOnClickListener(deleteCityListener)

    allCities.addListener(allCitiesObserver)
    currentCity.addListener(updatedCityDate)
    yearSeasonStore.addListener(updatedCityDate)
    toTemperatureType.addListener(updatedCityDate)
  }

  override fun onStop() {
    super.onStop()
    spinnerYearSeason.onItemSelectedListener = null
    spinnerCity.onItemSelectedListener = null

    allCities.removeListener(allCitiesObserver)
    currentCity.removeListener(updatedCityDate)
    yearSeasonStore.removeListener(updatedCityDate)
    toTemperatureType.removeListener(updatedCityDate)

    buttonAdd.setOnClickListener(null)
    imageViewEditCity.setOnClickListener(null)
  }
}