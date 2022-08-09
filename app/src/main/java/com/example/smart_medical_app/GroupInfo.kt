package com.example.smart_medical_app

data class GroupInfo(val Id:Int,val groupName: String,
                     val member:ArrayList<String>,
                     val message:ArrayList<MessageList>)
