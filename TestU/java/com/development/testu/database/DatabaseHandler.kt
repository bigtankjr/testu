package com.development.testu.database

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.development.testu.models.TestingLocationModel
import com.development.testu.models.UserModel


//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "TestUDatabase" // Database name
        private const val TABLE_TESTING_LOCATION = "TestingLocationsTable" // Table Name
        private const val TABLE_USER_IDENTITY = "UserIdentityTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        private const val USER_ID = "user_id"
        private const val USER_LOGIN = "user_login"
        private const val USER_PASSWORD = "user_password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_TESTU_USER_IDENTITY_TABLE = ("CREATE TABLE " + TABLE_USER_IDENTITY + "("
                + USER_ID + " INTEGER PRIMARY KEY,"
                + USER_LOGIN + " TEXT,"
                + USER_PASSWORD + " TEXT)")
        db?.execSQL(CREATE_TESTU_USER_IDENTITY_TABLE)

        val CREATE_TESTU_LOCATIONS_TABLE = ("CREATE TABLE " + TABLE_TESTING_LOCATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_TESTU_LOCATIONS_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TESTING_LOCATION")

        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER_IDENTITY")
        onCreate(db)
    }

    /**
     * Function to insert a Testing Location details to SQLite Database.
     */
    fun addTestingLocation(testingLocation: TestingLocationModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, testingLocation.title) // TestingLocationModelClass TITLE
        contentValues.put(KEY_IMAGE, testingLocation.image) // TestingLocationModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            testingLocation.description
        ) // TestingLocationModelClass DESCRIPTION
        contentValues.put(KEY_DATE, testingLocation.date) // TestingLocationModelClass DATE
        contentValues.put(KEY_LOCATION, testingLocation.location) // TestingLocationModelClass LOCATION
        contentValues.put(KEY_LATITUDE, testingLocation.latitude) // TestingLocationModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, testingLocation.longitude) // TestingLocationModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_TESTING_LOCATION, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    /**
     * Function to read all the list of Testing Location data which are inserted.
     */
    fun getTestingLocationsList(): ArrayList<TestingLocationModel> {

        // A list is initialize using the data model class in which we will add the values from cursor.
        val testingLocationList: ArrayList<TestingLocationModel> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE_TESTING_LOCATION" // Database select query

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = TestingLocationModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE))
                    )
                    testingLocationList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return testingLocationList
    }

    /**
     * Function to update record
     */
    fun updateTestingLocation(testingLocation: TestingLocationModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, testingLocation.title) // TestingLocationModelClass TITLE
        contentValues.put(KEY_IMAGE, testingLocation.image) // TestingLocationModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            testingLocation.description
        ) // TestingLocationModelClass DESCRIPTION
        contentValues.put(KEY_DATE, testingLocation.date) // TestingLocationModelClass DATE
        contentValues.put(KEY_LOCATION, testingLocation.location) // TestingLocationModelClass LOCATION
        contentValues.put(KEY_LATITUDE, testingLocation.latitude) // TestingLocationModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, testingLocation.longitude) // TestingLocationModelClass LONGITUDE

        // Updating Row
        val success =
            db.update(TABLE_TESTING_LOCATION, contentValues, KEY_ID + "=" + testingLocation.id, null)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    /**
     * Function to delete Testing Location details.
     */
    fun deleteTestingLocation(testingLocation: TestingLocationModel): Int {

        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE_TESTING_LOCATION, KEY_ID + "=" + testingLocation.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    fun insertData(username: String, password: String):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_LOGIN, username)
        contentValues.put(USER_PASSWORD, password)

        val result = db.insert(TABLE_USER_IDENTITY, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result

    }

    fun checkUserName(username: String):Boolean{
        val selectQuery = "select * from $TABLE_USER_IDENTITY  where $USER_LOGIN=?"


        val db = this.readableDatabase
        val params = arrayOf(username)

        try {
            val cursor:Cursor = db.rawQuery(selectQuery, params)
            if (cursor.count > 0) {
                return true
            }
            cursor.close()
            } catch (e: SQLiteException) {
                db.execSQL(selectQuery)
                return false
            }
        return true
    }

    fun checkUserNamePassword(username: String,password: String):Boolean{
        val selectQuery = "select * from  $TABLE_USER_IDENTITY where $USER_LOGIN=? and $USER_PASSWORD=?"

        val db = this.readableDatabase
        val params = arrayOf(username, password)
        try {
            val cursor:Cursor = db.rawQuery(selectQuery,params)
            return cursor.count > 0
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return false
        }
        return false
    }

    /*fun getUserId(username: String, password: String):String{
        val selectQuery = "select * from  $TABLE_USER_IDENTITY where $USER_LOGIN=? and $USER_PASSWORD=?"

        val db = this.readableDatabase
        val params = arrayOf(username, password)
        var userId =  -1


        try {
            val cursor:Cursor = db.rawQuery(selectQuery,params)
            if (cursor.moveToFirst()) {
                do {
                    val user = UserModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_LOGIN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD))
                    )
                    userId = user.user_id
                } while (cursor.moveToNext())
            }

            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return "-1"
        }

        return userId.toString()
    }*/
}
