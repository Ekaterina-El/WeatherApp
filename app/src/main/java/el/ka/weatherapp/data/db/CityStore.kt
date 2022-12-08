package el.ka.weatherapp.data.db

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import el.ka.weatherapp.data.model.City
import el.ka.weatherapp.data.model.CityType
import el.ka.weatherapp.data.model.TemperatureType

class CityStore(private val context: Context) {
  private val dbHelper by lazy { CityDbHelper(context) }

  fun saveCity(city: City, isEdit: Boolean = false) {
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

    val tbName = CityDbContract.CityEntry.TABLE_NAME
    if (isEdit) db.update(
      tbName,
      values,
      "${BaseColumns._ID} = ?",
      arrayOf(city.id.toString())
    )
    else db.insert(tbName, null, values)
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

  fun getCityById(cityId: Long): City? {
    if (cityId < 0) return null

    val db = dbHelper.readableDatabase
    val projection = arrayOf(
      BaseColumns._ID,
      CityDbContract.CityEntry.COLUMN_NAME,
      CityDbContract.CityEntry.COLUMN_TYPE,
      CityDbContract.CityEntry.COLUMN_MONTHS_TEMPERATURE,
      CityDbContract.CityEntry.COLUMN_TEMPERATURE_TYPE,
    )

    val sel = "${BaseColumns._ID} = ?"
    val selArg = arrayOf(cityId.toString())

    val cursor =
      db.query(CityDbContract.CityEntry.TABLE_NAME, projection, sel, selArg, null, null, null)

    // Если не нашли подходящих зваписей
    if (cursor.count == 0) return null

    cursor.moveToFirst()

    val city = City()
    with(cursor) {
      city.id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
      city.name = getString(getColumnIndexOrThrow(CityDbContract.CityEntry.COLUMN_NAME))
      city.type =
        CityType.valueOf(getString(getColumnIndexOrThrow(CityDbContract.CityEntry.COLUMN_TYPE)))
      city.tempType =
        TemperatureType.valueOf(getString(getColumnIndexOrThrow(CityDbContract.CityEntry.COLUMN_TEMPERATURE_TYPE)))
      city.temps =
        getString(getColumnIndexOrThrow(CityDbContract.CityEntry.COLUMN_MONTHS_TEMPERATURE))
          .split(CityDbContract.CityEntry.TEMPERATURE_SPLITER).map { it.toDouble() }
    }
    cursor.close()
    return city
  }
}