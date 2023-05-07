package com.sanryoo.news.feature.domain.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sanryoo.news.feature.domain.database.Converters

@Entity(
    tableName = "article"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(defaultValue = "")
    val author: String = "",
    @ColumnInfo(defaultValue = "")
    val content: String = "",
    @ColumnInfo(defaultValue = "")
    val description: String = "",
    @ColumnInfo(defaultValue = "")
    val publishedAt: String = "",
    @ColumnInfo(defaultValue = "")
    val source: Source = Source(),
    @ColumnInfo(defaultValue = "")
    val title: String = "",
    @ColumnInfo(defaultValue = "")
    val url: String = "",
    @ColumnInfo(defaultValue = "")
    val urlToImage: String = ""
)