package el.ka.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import el.ka.weatherapp.MainActivity
import el.ka.weatherapp.R
import el.ka.weatherapp.adapter.MonthsAdapter
import el.ka.weatherapp.data.model.*
import el.ka.weatherapp.observer.ObservedValue


class CityFragment : Fragment(R.layout.city_fragment) {
  private var state = State.ADD
  private val factory by lazy { ctx.spinnerAdapterFactory }
  private val ctx by lazy { requireContext() as MainActivity }

  private lateinit var textViewCityName: EditText

  private lateinit var spinnerCityType: Spinner
  private val cityType by lazy { ObservedValue() }
  private val spinnerItemListener by lazy {
    object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        cityType.updateValue((parent!!.getItemAtPosition(pos) as SpinnerItem).programValue)
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerCityType.setSelection(0)
      }
    }
  }

  private lateinit var spinnerTempType: Spinner
  private val temperatureType by lazy { ObservedValue() }
  private val spinnerTempListener by lazy {
    object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        temperatureType.updateValue((parent!!.getItemAtPosition(pos) as SpinnerItem).programValue)
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerTempType.setSelection(0)
      }
    }
  }


  private fun getMonths(temps: List<Double>?): List<MonthTemperature> {
    val t: DoubleArray = (temps ?: DoubleArray(12).map { 0.0 }).toDoubleArray()
    return resources.getStringArray(R.array.months).mapIndexed { idx, month ->
      MonthTemperature(month, t[idx])
    }
  }

  private lateinit var monthsAdapter: MonthsAdapter
  private lateinit var recyclerViewMonths: RecyclerView

  private var currentID: Long? = null
  private lateinit var buttonSave: Button
  private val buttonSaveListener by lazy {
    OnClickListener {
      val city = City(
        id = currentID,
        cityNameObserved.getValue() as? String ?: "",
        cityType.getValue() as? CityType ?: CityType.SMALL,
        temperatureType.getValue() as? TemperatureType ?: TemperatureType.CELSIUS,
        monthsAdapter.getMonthsTemperature()
      )

      val db = ctx.cityStore
      db.saveCity(city, state == State.EDIT)
      ctx.onBackPressed()
    }
  }

  private val cityNameObserved by lazy { ObservedValue() }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUI()
    initSpinners()
    getCityId()
  }

  private fun initUI() {
    val mView = requireView()

    spinnerTempType = mView.findViewById(R.id.spinnerTempType)
    spinnerCityType = mView.findViewById(R.id.spinnerCityType)
    textViewCityName = mView.findViewById(R.id.editTextViewCityName)
    recyclerViewMonths = mView.findViewById(R.id.recyclerViewDegrees)
    buttonSave = mView.findViewById(R.id.buttonSave)
  }

  private fun getCityId() {
    val cityId = arguments?.getString(CITY_ID)?.toLong()
    val city = ctx.cityStore.getCityById(cityId ?: -1)

    state = if (city != null) {
      currentID = cityId
      updateFields(city)
      State.EDIT
    } else {
      State.ADD
    }
    initMonthsRecyclerView(city?.temps)
  }

  private fun updateFields(city: City) {
    cityNameObserved.updateValue(city.name)
    cityType.updateValue(city.type as Any)
    temperatureType.updateValue(city.tempType as Any)

    val cityPosition = factory.getItemPosition(SpinnerType.CITY_TYPE, city.type as Any)
    val tempPosition = factory.getItemPosition(SpinnerType.TEMPERATURE_TYPE, city.tempType as Any)

    textViewCityName.setText(city.name)
    spinnerCityType.setSelection(cityPosition)
    spinnerTempType.setSelection(tempPosition)
  }

  private fun initMonthsRecyclerView(temps: List<Double>?) {
    monthsAdapter = MonthsAdapter(getMonths(temps))
    recyclerViewMonths.adapter = monthsAdapter
  }

  private fun initSpinners() {
    spinnerCityType.adapter = factory.getSpinnerAdapter(SpinnerType.CITY_TYPE)
    spinnerTempType.adapter = factory.getSpinnerAdapter(SpinnerType.TEMPERATURE_TYPE)
  }


  override fun onResume() {
    super.onResume()

    textViewCityName.doAfterTextChanged { cityNameObserved.updateValue(it.toString()) }
    spinnerCityType.onItemSelectedListener = spinnerItemListener
    spinnerTempType.onItemSelectedListener = spinnerTempListener
    buttonSave.setOnClickListener(buttonSaveListener)

  }

  override fun onStop() {
    super.onStop()
    textViewCityName.doAfterTextChanged {}
    spinnerCityType.onItemSelectedListener = null
    spinnerTempType.onItemSelectedListener = null
    buttonSave.setOnClickListener(null)
  }

  companion object {
    const val CITY_ID = "cityId"

    enum class State { ADD, EDIT }
  }
}