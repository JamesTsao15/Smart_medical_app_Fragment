package com.example.smart_medical_app

class MyBinder: IMyAidlInterface.Stub() {
    override fun basicTypes(
        anInt: Int,
        aLong: Long,
        aBoolean: Boolean,
        aFloat: Float,
        aDouble: Double,
        aString: String?
    ) {
        TODO("Not yet implemented")
    }

}