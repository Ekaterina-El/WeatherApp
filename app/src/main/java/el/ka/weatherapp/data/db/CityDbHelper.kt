package el.ka.weatherapp.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import el.ka.weatherapp.data.model.City

class CityDbHelper(context: Context) :
  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  companion object {
    const val DATABASE_NAME = "WeatherApp.db"
    const val DATABASE_VERSION = 1
  }

  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(CityDbContract.SQL_CREATE_ENTRIES)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
    db.execSQL(CityDbContract.SQL_DELETE_ENTRIES)
    onCreate(db)
  }

  override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    onUpgrade(db, oldVersion, newVersion)
  }
}