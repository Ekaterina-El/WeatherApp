package el.ka.weatherapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import el.ka.weatherapp.R
import el.ka.weatherapp.data.model.SpinnerType
import el.ka.weatherapp.data.model.YearSeasonType
import el.ka.weatherapp.factory.SpinnerAdapterFactory
import el.ka.weatherapp.observer.Observed
import el.ka.weatherapp.observer.Observer
import el.ka.weatherapp.observer.YearSeasonStore

class FirstFragment : Fragment(R.layout.first_fragment) {
  private val ctx: Context by lazy { requireContext() }
  private val mView: View by lazy { requireView() }
  private val navController by lazy { findNavController() }

  private val spinnerAdapterFactory by lazy { SpinnerAdapterFactory(ctx) }

  private val spinnerCity: Spinner by lazy { mView.findViewById(R.id.spinnerCity) }

  private val buttonAdd by lazy { mView.findViewById<ConstraintLayout>(R.id.buttonAdd) }
  private val buttonAddListener by lazy { OnClickListener { navigateTo(R.id.action_firstFragment_to_cityFragment) } }

  private val spinnerYearSeason: Spinner by lazy { mView.findViewById(R.id.spinnerYearSeason) }
  private val yearSeasonStore by lazy { YearSeasonStore() }
  private val spinnerYearSeasonListener by lazy {
    object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        yearSeasonStore.updateYearSeasonType(parent!!.getItemAtPosition(pos) as YearSeasonType)
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerYearSeason.setSelection(0)
      }
    }
  }

  private val testObserver by lazy {
    object : Observer {
      override fun notify(observed: Observed) {
        val seasonType = (observed as YearSeasonStore).getYearSeasonType()
        val value = if (seasonType != null) getString(seasonType.strIdx) else ""
        textViewArgTemperature.text = value
      }
    }
  }

  private val textViewCityType: TextView by lazy { mView.findViewById(R.id.textViewCityType) }
  private val textViewArgTemperature: TextView by lazy { mView.findViewById(R.id.textViewArgTemperature) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initSpinners()
  }

  private fun initSpinners() {
    spinnerYearSeason.adapter = spinnerAdapterFactory.getSpinnerAdapter(SpinnerType.YEAR_SEASONS)
  }

  override fun onResume() {
    super.onResume()
    spinnerYearSeason.onItemSelectedListener = spinnerYearSeasonListener
    yearSeasonStore.addListener(testObserver)
    buttonAdd.setOnClickListener(buttonAddListener)
  }

  override fun onStop() {
    super.onStop()
    spinnerYearSeason.onItemSelectedListener = null
    yearSeasonStore.removeListener(testObserver)
    buttonAdd.setOnClickListener(null)
  }

  private fun navigateTo(actionIdx: Int) {
    navController.navigate(actionIdx)
  }
}