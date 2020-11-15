package com.interview.thenewyorktimes.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.interview.thenewyorktimes.model.Bookmarks
import com.interview.thenewyorktimes.model.Results

/**
 * Created by akshay on 14,November,2020
 * akshay2211@github.io
 */

@Database(entities = [Results::class, Bookmarks::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultsDao(): ResultsDao
    abstract fun bookmarksDao(): BookmarksDao
}


@Dao
interface ResultsDao {

    @Query("SELECT * FROM results_table WHERE type = :type ORDER BY id ASC")
    fun storiesByType(type: String): LiveData<List<Results>>

    @Query("SELECT COUNT(id) FROM results_table WHERE type = :type")
    fun getCount(type: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<Results>)

    @Query("DELETE FROM results_table WHERE type = :type")
    fun deleteBySectionType(type: String)

    @Query("DELETE FROM results_table")
    suspend fun deleteTable()

}

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM bookmarks_table ORDER BY id ASC")
    fun getBookmarks(): LiveData<List<Bookmarks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: Bookmarks)

    @Query("DELETE FROM bookmarks_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM bookmarks_table")
    suspend fun deleteTable()
}