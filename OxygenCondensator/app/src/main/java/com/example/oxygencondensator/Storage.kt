package com.example.oxygencondensator

class Storage {
    val internalStorage: FileHelper? = FileHelper.instance
    companion object{
        var instance: Storage? = null
            get(){
                if (field == null){
                    field = Storage()
                }
                return field
            }
        private set
    }
}