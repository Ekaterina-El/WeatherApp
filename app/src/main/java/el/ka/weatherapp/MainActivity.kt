package el.ka.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import el.ka.weatherapp.data.db.CityStore
import el.ka.weatherapp.factory.SpinnerAdapterFactory

class MainActivity : AppCompatActivity() {
  val spinnerAdapterFactory by lazy { SpinnerAdapterFactory(this) }
  val cityStore by lazy { CityStore(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}