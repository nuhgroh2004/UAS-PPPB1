package com.example.uaspppb1.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Mood(
    @SerializedName("_id") val id: String? = null,
    val mood: String,
    val timestamp: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(mood)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mood> {
        override fun createFromParcel(parcel: Parcel): Mood {
            return Mood(parcel)
        }

        override fun newArray(size: Int): Array<Mood?> {
            return arrayOfNulls(size)
        }
    }
}