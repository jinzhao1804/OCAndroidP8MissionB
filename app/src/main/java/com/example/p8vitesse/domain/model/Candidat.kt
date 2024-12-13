package com.example.p8vitesse.domain.model

import com.example.p8vitesse.data.entity.CandidatDto

class Candidat(
    val id: Long? = null,
    var name: String,
    var surname: String,
    var phone: String,
    var email: String,
    var birthDate: java.util.Date,
    var desiredSalary: Double,
    var note: String,
    var isFav: Boolean
) {

    fun toDto(): CandidatDto {
        return CandidatDto(
            id = this.id,  // Ensure the ID is passed here
            name = this.name,
            surname = this.surname,
            phone = this.phone,
            email = this.email,
            birthdate = this.birthDate,
            desiredSalary = this.desiredSalary,
            note = this.note,
            isFav = this.isFav
        )
    }

    companion object {
        fun fromDto(dto: CandidatDto): Candidat {
            return Candidat(
                id = dto.id,  // Ensure the ID is passed here
                name = dto.name,
                surname = dto.surname,
                phone = dto.phone,
                email = dto.email,
                birthDate = dto.birthdate,
                desiredSalary = dto.desiredSalary,
                note = dto.note,
                isFav = dto.isFav
            )
        }
    }
}
