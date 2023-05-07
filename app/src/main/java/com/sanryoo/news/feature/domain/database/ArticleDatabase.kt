package com.sanryoo.news.feature.domain.database

import android.content.Context
import androidx.room.*
import com.sanryoo.news.feature.domain.modal.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDAO(): ArticleDAO

    companion object {

        @Volatile
        private var instance: ArticleDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context = context.applicationContext,
            klass = ArticleDatabase::class.java,
            name = "article_db.db"
        )
            .addTypeConverter(Converters())
            .build()

    }

}