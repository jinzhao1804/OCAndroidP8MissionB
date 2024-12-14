package com.example.p8vitesse.domain.model

import android.graphics.Bitmap
import com.example.p8vitesse.data.converter.Converter
import com.example.p8vitesse.data.entity.CandidatDto

data class Candidat(
    val id: Long? = null,
    var name: String,
    var surname: String,
    var phone: String,
    var email: String,
    var birthdate: java.util.Date,
    var desiredSalary: Double,
    var note: String,
    var isFav: Boolean,
    val profilePicture: Bitmap?  // Use Bitmap instead of Image
) {

    fun toDto(): CandidatDto {
        return CandidatDto(
            id = this.id,
            name = this.name,
            surname = this.surname,
            phone = this.phone,
            email = this.email,
            birthdate = this.birthdate,
            desiredSalary = this.desiredSalary,
            note = this.note,
            isFav = this.isFav,
            profilePicture = Converter().fromBitmap(this.profilePicture)  // Convert Bitmap to String
        )
    }

    companion object {
        fun fromDto(dto: CandidatDto): Candidat {
            return Candidat(
                id = dto.id,
                name = dto.name,
                surname = dto.surname,
                phone = dto.phone,
                email = dto.email,
                birthdate = dto.birthdate,
                desiredSalary = dto.desiredSalary,
                note = dto.note,
                isFav = dto.isFav,
                profilePicture = Converter().toBitmap(dto.profilePicture)  // Convert String to Bitmap
            )
        }
    }
}
