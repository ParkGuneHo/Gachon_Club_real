package com.example.gachon_club.Club.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Board(var _id: Long?, var club:String, var title:String, var content:String, var name:String, var data: Date?) : Parcelable