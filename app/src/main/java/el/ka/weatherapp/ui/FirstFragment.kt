package el.ka.weatherapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import el.ka.weatherapp.R
import el.ka.weatherapp.adapter.YearSeasonAdapter
import el.ka.weatherapp.data.model.SpinnerType
import el.ka.weatherapp.data.model.YearSeasonType
import el.ka.weatherapp.factory.SpinnerAdapterFactory

class FirstFragment : Fragment(R.layout.first_fragment) {
  private val ctx: Context by lazy { requireContext() }
  private val mView: View by lazy { requireView() }

  private val spinnerAdapterFactory by lazy { SpinnerAdapterFactory(ctx) }

  private val spinnerCity: Spinner by lazy { mView.findViewById(R.id.spinnerCity) }
  private val spinnerYearSeason: Spinner by lazy { mView.findViewById(R.id.spinnerYearSeason) }

  private val textViewCityType: TextView by lazy { mView.findViewById(R.id.textViewCityType) }
  private val textViewArgTemperature: TextView by lazy { mView.findViewById(R.id.textViewArgTemperature) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initSpinners()
  }

  private fun initSpinners() {
    spinnerYearSeason.adapter = spinnerAdapterFactory.getSpinnerAdapter(SpinnerType.YEAR_SEASONS)
  }
}