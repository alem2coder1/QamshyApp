package kz.qamshy.app.koinmodule.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles WHERE id = :articleId")
    suspend fun getArticleById(articleId: Int): ArticleEntity?

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun clearAllArticles()
}

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<TagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    @Query("DELETE FROM tags")
    suspend fun clearAllTags()

    @Query("SELECT * FROM tags INNER JOIN article_tag_cross_ref ON tags.id = article_tag_cross_ref.tagId WHERE article_tag_cross_ref.articleId = :articleId")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getTagsForArticle(articleId: Int): List<TagEntity>
}

@Dao
interface ArticleTagCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefs(crossRefs: List<ArticleTagCrossRef>)

    @Query("DELETE FROM article_tag_cross_ref")
    suspend fun clearAllCrossRefs()
}

@Dao
interface IndexDao {
    @Query("SELECT * FROM index_data LIMIT 1")
    suspend fun getIndexData(): IndexEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndexData(indexEntity: IndexEntity)

    @Query("DELETE FROM index_data")
    suspend fun clearIndexData()
}