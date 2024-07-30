package com.example.test1

data class Message(
    var message: String?,
    var sendId: String?
){
    constructor():this("","")
}
