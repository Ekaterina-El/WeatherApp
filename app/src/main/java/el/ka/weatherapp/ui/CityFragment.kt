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
import el.ka.weatherapp.data.model.*
import el.ka.weatherapp.observer.ObservedValue

class CityFragment : Fragment(R.layout.city_fragment) {
  private val ctx by lazy { requireContext() as MainActivity }
  private val mView by lazy { requireView() }

  private val textViewCityName by lazy { mView.findViewById<EditText>(R.id.editTextViewCityName) }

  private val spinnerCityType by lazy { mView.findViewById<Spinner>(R.id.spinnerCityType) }
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

  private val spinnerTempType by lazy { mView.findViewById<Spinner>(R.id.spinnerTempType) }
  private val temperatureType by lazy { ObservedValue() }
  private val spinnerTempListener by lazy {
    object: AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        temperatureType.updateValue((parent!!.getItemAtPosition(pos) as SpinnerItem).programValue)
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerTempType.setSelection(0)
      }

    }
  }

  private val recyclerViewDegrees by lazy { mView.findViewById<RecyclerView>(R.id.recyclerViewDegrees) }

  private val buttonSave by lazy { mView.findViewById<Button>(R.id.buttonSave) }
  private val buttonSaveListener by lazy {
    OnClickListener {
      val city = City(
        cityNameObserved.getValue() as String,
        cityType.getValue() as CityType,
        temperatureType.getValue() as TemperatureType,
        listOf()
      )
    }
   }

  private val cityNameObserved by lazy { ObservedValue() }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initSpinners()
  }

  private fun initSpinners() {
    spinnerCityType.adapter = ctx.spinnerAdapterFactory.getSpinnerAdapter(SpinnerType.CITY_TYPE)
    spinnerTempType.adapter = ctx.spinnerAdapterFactory.getSpinnerAdapter(SpinnerType.TEMPERATURE_TYPE)
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

}