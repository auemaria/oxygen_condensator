package com.example.oxygencondensator

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import kotlin.text.StringBuilder


class FileHelper {
    private lateinit var fileWriter:FileWriter

    fun createFile(context: Context, fileName: String): Boolean{
        fileName?.let{
            File(context.filesDir, it)
        }
        return true
    }
    fun getfile(context: Context, fileName: String): File?{
        return fileName?.let {File(context.filesDir,it)}
    }

    fun write(file: File?, data: String){
        try{
            fileWriter= FileWriter(file)
            fileWriter.append(data)
            fileWriter.flush()
            fileWriter.close()
        }catch(e: IOException){
            e.printStackTrace()
        }
    }
    fun read(file: File?): StringBuilder{
        var line: String?
        val stringBuilder = StringBuilder()
        try{
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            while(bufferedReader.readLine().also{line=it}!=null){
                stringBuilder.append(line + "\n")
            }
        }catch(e:IOException){
            e.printStackTrace()
        }
        return stringBuilder
    }

    companion object{
        var instance: FileHelper? = null
            get(){
                if(field==null)
                {
                    field = FileHelper()
                }
                return field
                }
        private set
    }
}