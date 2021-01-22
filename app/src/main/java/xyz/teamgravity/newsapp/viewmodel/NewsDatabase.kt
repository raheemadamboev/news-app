package xyz.teamgravity.newsapp.viewmodel

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.teamgravity.newsapp.helper.constants.MyDatabase
import xyz.teamgravity.newsapp.helper.converters.Converter
import xyz.teamgravity.newsapp.model.ArticleModel

@TypeConverters(Converter::class)
@Database(entities = [ArticleModel::class], version = MyDatabase.DATABASE_VERSION)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}