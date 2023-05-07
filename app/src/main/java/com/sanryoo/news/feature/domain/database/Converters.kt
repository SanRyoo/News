package com.sanryoo.news.feature.domain.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.sanryoo.news.feature.domain.modal.Source

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

}