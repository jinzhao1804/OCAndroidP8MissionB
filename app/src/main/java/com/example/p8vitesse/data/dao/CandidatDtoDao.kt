package com.example.p8vitesse.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p8vitesse.data.entity.CandidatDto
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.flow.Flow

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
    fun getFavoriteCandidats(): List<CandidatDto>


}