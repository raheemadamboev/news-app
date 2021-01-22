package xyz.teamgravity.newsapp.helper.converters

import androidx.room.TypeConverter
import xyz.teamgravity.newsapp.model.SourceModel

class Converter {

    @TypeConverter
    fun fromSource(source: SourceModel) = source.name

    @TypeConverter
    fun toSource(name: String) = SourceModel(name, name)
}