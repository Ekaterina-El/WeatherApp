package el.ka.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import el.ka.weatherapp.data.db.CityStore
import el.ka.weatherapp.excel.ExcelGenerator
import el.ka.weatherapp.factory.SpinnerAdapterFactory
import el.ka.weatherapp.strategy.Convertor

class MainActivity : AppCompatActivity() {
  val spinnerAdapterFactory by lazy { SpinnerAdapterFactory(this) }
  val cityStore by lazy { CityStore(this) }
  val convertor by lazy { Convertor() }
  val excelGenerator by lazy { ExcelGenerator(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ActivityCompat.requestPermissions(
      this, arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
      ), PackageManager.PERMISSION_GRANTED
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    cityStore.closeDb()
  }

  fun navigateTo(navController: NavController, actionIdx: Int, args: Bundle? = null) {
    navController.navigate(actionIdx, args)
  }
}