import com.google.gson.annotations.SerializedName
import com.neighbor.neighborsrefrigerator.data.PostData

data class ReturnObjectForWrite (
    @SerializedName("isConnect")
    val isConnect: Boolean,
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("msg")
    val msg: String,
)