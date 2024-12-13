package com.example.p8vitesse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "candidat")
data class CandidatDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "surname")
    var surname: String,

    @ColumnInfo(name = "phone")
    var phone: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "birthdate")
    var birthdate: Date,

    @ColumnInfo(name = "desiredSalary")
    var desiredSalary: Double,

    @ColumnInfo(name = "note")
    var note: String,

    @ColumnInfo(name = "isFav")
    var isFav: Boolean,

    @ColumnInfo(name = "profilePicture")
    var profilePicture: String? // Base64-encoded image string
)
