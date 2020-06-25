package com.example.gachon_club.Account.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var userId: String, var userPw: String, var name: String, var major: String, var club: String, var position: String) : Parcelable