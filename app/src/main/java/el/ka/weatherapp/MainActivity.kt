package el.ka.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import el.ka.weatherapp.factory.SpinnerAdapterFactory

class MainActivity : AppCompatActivity() {
  val spinnerAdapterFactory by lazy { SpinnerAdapterFactory(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}