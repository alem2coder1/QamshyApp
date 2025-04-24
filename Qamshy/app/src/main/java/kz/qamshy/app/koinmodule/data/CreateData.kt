package kz.qamshy.app.koinmodule.data

@Database(
    entities = [
        ArticleEntity::class,
        TagEntity::class,
        ArticleTagCrossRef::class,
        IndexEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun tagDao(): TagDao
    abstract fun articleTagCrossRefDao(): ArticleTagCrossRefDao
    abstract fun indexDao(): IndexDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "qamshy_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}