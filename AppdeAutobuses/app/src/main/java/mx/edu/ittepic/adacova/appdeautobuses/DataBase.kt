package mx.edu.ittepic.adacova.appdeautobuses

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context?, name:String?, factory: SQLiteDatabase.CursorFactory?, version:Int):
    SQLiteOpenHelper
        (context,name,factory,version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE User(id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username VARCHAR(200), " +
                "password VARCHAR(200) NOT NULL, " +
                "name VARCHAR(200) NOT NULL, " +
                "first_lastname VARCHAR(200) NOT NULL, " +
                "second_lastname VARCHAR(200), " +
                "photo VARCHAR(200), " +
                "license VARCHAR(200), " +
                "ine VARCHAR(200));")


        db?.execSQL("CREATE TABLE Bus(id_bus INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "brand VARCHAR(200) NOT NULL, " +
                "model VARCHAR(200) NOT NULL, " +
                "capacity VARCHAR(200) NOT NULL, " +
                "plates VARCHAR(200), " +
                "bus_number VARCHAR(200));")

        db?.execSQL("CREATE TABLE Travel(id_viaje INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "origen VARCHAR(200) NOT NULL, " +
                "destiny VARCHAR(200) NOT NULL, " +
                "departure_date DATE, " +
                "arrival_date DATE, " +
                "passengers_number VARCHAR(200), " +
                "bus_status VARCHAR(200), " +
                "completed BOOLEAN, " +
                "travel_log VARCHAR(200), " +
                "FOREIGN KEY (id_bus) REFERENCES Bus(id_bus), " +
                "FOREIGN KEY (id_user) REFERENCES User(id_user));")


        db?.execSQL("CREATE TABLE Incidents(id_Incidents INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type VARCHAR(200), " +
                "location VARCHAR(200), " +
                "description VARCHAR(200), " +
                "FOREIGN KEY (id_travel) REFERENCES Travel(id_travel));")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}