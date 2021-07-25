package live.tek.mvvm_kotlin.model

import com.google.gson.annotations.SerializedName

data class SimpleError(
    @field:SerializedName("code")
    val code:Int,

    @field:SerializedName("message")
    val message:String
)