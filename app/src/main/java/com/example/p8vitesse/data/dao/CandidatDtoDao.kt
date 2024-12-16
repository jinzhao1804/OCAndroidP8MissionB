package com.example.p8vitesse.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p8vitesse.data.entity.CandidatDto
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface CandidatDtoDao {

    @Insert
    suspend fun insertCandidat(candidat: CandidatDto): Long


    @Query("SELECT * FROM candidat")
    fun getAllCandidats(): List<CandidatDto>

    @Query("SELECT * FROM candidat WHERE id = :candidatId")
    fun getCandidatById(candidatId: Int): Candidat


    @Query("DELETE FROM candidat WHERE id = :id")
    suspend fun deleteCandidatById(id: Long)

    @Query("DELETE FROM candidat")
    suspend fun deleteAllCandidates()

    @Query("SELECT * FROM candidat WHERE isFav = 1")
    fun getFavorisCandidats(): Flow<List<Candidat>>  // Use Flow for reactivity

    @Query("UPDATE candidat SET isFav = :isFavorite WHERE id = :candidatId")
    fun setFavoriteCandidat(candidatId: Int, isFavorite: Boolean)

    @Query("UPDATE candidat SET name = :name, surname = :surname, phone = :phone, email = :email, birthdate = :birthdate, desiredSalary = :desiredSalary, note = :note, isFav = :isFav, profilePicture = :profilePicture WHERE id = :id")
    suspend fun updateCandidat(
        id: Long?,
        name: String,
        surname: String,
        phone: String,
        email: String,
        birthdate: Date,
        desiredSalary: Double,
        note: String,
        isFav: Boolean,
        profilePicture: String
    )


}