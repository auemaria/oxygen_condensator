package com.example.oxygencondensator

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate

class DBhelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 3) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT, email TEXT, name TEXT, age INTEGER, ill TEXT, averagesat INTEGER, averagepulse INTEGER, todaytherapy INTEGER, dateoflasttherapy TEXT, stat INTEGER)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addUser(user: User){
        val values = ContentValues()
        values.put("login", user.login)
        values.put("password", user.password)
        values.put("email", user.email)
        values.put("name", "")
        values.put("ill", "")
        values.put("age", 0)
        values.put("averagesat", 0)
        values.put("averagepulse", 0)
        values.put("todaytherapy", 0)
        values.put("dateoflasttherapy", "")
        values.put("stat",0)
        val db = this.writableDatabase
        db.insert("users", null, values)

        db.close()
    }

    fun getUser(login: String, pass: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND password = '$pass'", null)
        return result.moveToFirst()
    }

    fun getAccountInfo(login: String, pass: String): MutableList<String>{
        val db = this.readableDatabase
        var inform: MutableList<String> = mutableListOf("", "", "", "");
        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND password = '$pass'", null)
        result.moveToFirst()
        inform[0] = result.getString(3) //email
        inform[1] = result.getString(4) //name
        inform[2] = result.getInt(5).toString() //age
        inform[3] = result.getString(6) //illness
        result.close()
        db.close()
        return inform
    }

    fun getTherapyInfo(login: String, pass: String): MutableList<String>{
        val db = this.readableDatabase
        var inform: MutableList<String> = mutableListOf("", "", "", "", "");
        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND password = '$pass'", null)
        result.moveToFirst()
        inform[0] = result.getInt(7).toString() //averagesat
        inform[1] = result.getInt(8).toString() //averagepulse
        inform[2] = result.getInt(9).toString() //todaytherapy
        inform[3] = result.getString(10) //dateoflasttherapy
        inform[4] = result.getInt(11).toString() //stat
        return inform
    }

    fun addInfo(login: String, name: String, age: Int, ill: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("age", age)
        values.put("ill", ill)
        db.update("users", values, "login = ?", arrayOf(login))
        db.close()
    }

    fun addTrerInfo(login: String,averagesat: Int, averagepulse: Int, therdur: Int, therdate:String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("averagesat", averagesat)
        values.put("averagepulse", averagepulse)
        values.put("todaytherapy", therdur)
        values.put("dateoflasttherapy", therdate)
        values.put("stat", 1)
        db.update("users", values, "login = ?", arrayOf(login))
        db.close()
    }

    fun changepass(login: String, newpass: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("password", newpass)
        db.update("users", values, "login = ?", arrayOf(login))
        db.close()
    }


}