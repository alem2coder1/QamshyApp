package kz.qamshy.app.koinmodule.data

// Room数据库实体
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

// 用于保存首页数据结构
@Entity(tableName = "index_data")
data class IndexEntity(
    @PrimaryKey val id: Int = 1, // 只存一条记录
    val pinnedArticleIds: String, // JSON格式存储ID列表
    val focusArticleIds: String,  // JSON格式存储ID列表
    val blockData: String,        // JSON格式存储块数据
    val lastUpdated: Long = System.currentTimeMillis()
)