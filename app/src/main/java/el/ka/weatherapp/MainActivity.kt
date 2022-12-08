package el.ka.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import el.ka.weatherapp.data.db.CityStore
import el.ka.weatherapp.factory.SpinnerAdapterFactory
import el.ka.weatherapp.strategy.Convertor

class MainActivity : AppCompatActivity() {
  val spinnerAdapterFactory by lazy { SpinnerAdapterFactory(this) }
  val cityStore by lazy { CityStore(this) }
  val convertor by lazy { Convertor() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onDestroy() {
    super.onDestroy()
    cityStore.closeDb()
  }

  fun navigateTo(navController: NavController, actionIdx: Int, args: Bundle? = null) {
    navController.navigate(actionIdx, args)
  }
}