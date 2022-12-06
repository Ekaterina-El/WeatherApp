package el.ka.weatherapp.data.db

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import el.ka.weatherapp.data.model.City

class CityStore(private val context: Context) {
  private val dbHelper by lazy { CityDbHelper(context) }

  fun addCity(city: City) {
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
      put(CityDbContract.CityEntry.COLUMN_NAME, city.name)
      put(CityDbContract.CityEntry.COLUMN_TYPE, city.type.toString())
      put(CityDbContract.CityEntry.COLUMN_TEMPERATURE_TYPE, city.tempType.toString())
      put(
        CityDbContract.CityEntry.COLUMN_MONTHS_TEMPERATURE,
        city.temps?.joinToString(CityDbContract.CityEntry.TEMPERATURE_SPLITER)
      )
    }

    val newRowId = db.insert(CityDbContract.CityEntry.TABLE_NAME, null, values)
    db.close()
  }

  fun getCities(): List<City> {
    val db = dbHelper.readableDatabase

    val projection = arrayOf(BaseColumns._ID, CityDbContract.CityEntry.COLUMN_NAME)
    val cursor = db.query(
      CityDbContract.CityEntry.TABLE_NAME,
      projection,
      null, null, null, null, null
    )

    val items = mutableListOf<City>()
    with(cursor) {
      while (moveToNext()) {
        val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
        val name = getString(getColumnIndexOrThrow(CityDbContract.CityEntry.COLUMN_NAME))
        items.add(City(id, name))
      }
    }

    cursor.close()
    return items
  }

  fun closeDb() = dbHelper.close()
}