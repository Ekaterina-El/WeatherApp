package el.ka.weatherapp.data.db

import android.provider.BaseColumns

object CityDbContract {
  object CityEntry: BaseColumns {
    const val TEMPERATURE_SPLITER = ","
    const val TABLE_NAME = "city"
    const val COLUMN_NAME = "name"
    const val COLUMN_TYPE = "type"
    const val COLUMN_TEMPERATURE_TYPE = "temperature_type"
    const val COLUMN_MONTHS_TEMPERATURE = "months_temperature"
  }

  const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${CityEntry.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "${CityEntry.COLUMN_NAME} TEXT," +
        "${CityEntry.COLUMN_TYPE} TEXT," +
        "${CityEntry.COLUMN_TEMPERATURE_TYPE} TEXT," +
        "${CityEntry.COLUMN_MONTHS_TEMPERATURE} TEXT)"

  const val SQL_DELETE_ENTRIES = "DROP TABLE IS EXISTS ${CityEntry.TABLE_NAME}"
}