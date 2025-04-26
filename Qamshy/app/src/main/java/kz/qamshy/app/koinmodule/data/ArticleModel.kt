package kz.qamshy.app.koinmodule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val thumbnailUrl: String,
    val categoryTitle: String,
    val addTime: String,
    val videoPath: String,
    val latynUrl: String,
    val likeCount: Int,
    val author: String,
    val thumbnailCopyright: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val id: Int,
    val title: String
)

@Entity(tableName = "article_tag_cross_ref", primaryKeys = ["articleId", "tagId"])
data class ArticleTagCrossRef(
    val articleId: Int,
    val tagId: Int
)

@Entity(tableName = "index_data")
data class IndexEntity(
    @PrimaryKey val id: Int = 1,
    val pinnedArticleIds: String,
    val focusArticleIds: String,
    val blockData: String,
    val lastUpdated: Long = System.currentTimeMillis()
)