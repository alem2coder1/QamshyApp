package kz.qamshy.app.koinmodule.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kz.qamshy.app.koinmodule.data.AppDatabase
import kz.qamshy.app.koinmodule.data.ArticleDao
import kz.qamshy.app.koinmodule.data.ArticleEntity
import kz.qamshy.app.koinmodule.data.ArticleTagCrossRef
import kz.qamshy.app.koinmodule.data.ArticleTagCrossRefDao
import kz.qamshy.app.koinmodule.data.IndexDao
import kz.qamshy.app.koinmodule.data.IndexEntity
import kz.qamshy.app.koinmodule.data.TagDao
import kz.qamshy.app.koinmodule.data.TagEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.*

@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var articleDao: ArticleDao
    private lateinit var tagDao: TagDao
    private lateinit var crossRefDao: ArticleTagCrossRefDao
    private lateinit var indexDao: IndexDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        articleDao = database.articleDao()
        tagDao = database.tagDao()
        crossRefDao = database.articleTagCrossRefDao()
        indexDao = database.indexDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetArticle() = runBlocking {
        val article = ArticleEntity(
            id = 1,
            title = "测试文章",
            shortDescription = "短描述",
            fullDescription = "完整描述",
            thumbnailUrl = "http://example.com/image.jpg",
            categoryTitle = "测试分类",
            addTime = "2025-05-02",
            videoPath = "",
            latynUrl = "",
            likeCount = 10,
            author = "测试作者",
            thumbnailCopyright = ""
        )

        articleDao.insertArticles(listOf(article))

        val retrievedArticle = articleDao.getArticleById(1)
        assertNotNull(retrievedArticle)
        assertEquals(article.title, retrievedArticle?.title)
        assertEquals(article.shortDescription, retrievedArticle?.shortDescription)
    }

    @Test
    fun insertAndGetAllArticles() = runBlocking {
        val articles = listOf(
            ArticleEntity(1, "文章1", "描述1", "全描述1", "", "分类1", "2025-05-01", "", "", 5, "作者1", ""),
            ArticleEntity(2, "文章2", "描述2", "全描述2", "", "分类2", "2025-05-02", "", "", 10, "作者2", "")
        )

        articleDao.insertArticles(articles)

        val retrievedArticles = articleDao.getAllArticles()
        assertEquals(2, retrievedArticles.size)
    }

    @Test
    fun clearAllArticles() = runBlocking {
        val articles = listOf(
            ArticleEntity(1, "文章1", "描述1", "全描述1", "", "分类1", "2025-05-01", "", "", 5, "作者1", ""),
            ArticleEntity(2, "文章2", "描述2", "全描述2", "", "分类2", "2025-05-02", "", "", 10, "作者2", "")
        )

        articleDao.insertArticles(articles)

        articleDao.clearAllArticles()

        val retrievedArticles = articleDao.getAllArticles()
        assertTrue(retrievedArticles.isEmpty())
    }

    @Test
    fun testArticleWithTags() = runBlocking {
        val article = ArticleEntity(1, "带标签的文章", "描述", "全描述", "", "分类", "2025-05-02", "", "", 10, "作者", "")
        val tags = listOf(
            TagEntity(1, "标签1"),
            TagEntity(2, "标签2")
        )
        val crossRefs = listOf(
            ArticleTagCrossRef(1, 1),
            ArticleTagCrossRef(1, 2)
        )

        articleDao.insertArticles(listOf(article))
        tagDao.insertTags(tags)
        crossRefDao.insertCrossRefs(crossRefs)

        val articleTags = tagDao.getTagsForArticle(1)
        assertEquals(2, articleTags.size)
        assertTrue(articleTags.any { it.title == "标签1" })
        assertTrue(articleTags.any { it.title == "标签2" })
    }

    @Test
    fun testIndexData() = runBlocking {
        val indexEntity = IndexEntity(
            pinnedArticleIds = "1,2,3",
            focusArticleIds = "4,5",
            blockData = "{\"blocks\":[{\"title\":\"测试区块\",\"articleIds\":[6,7,8]}]}"
        )

        indexDao.insertIndexData(indexEntity)

        val retrievedIndex = indexDao.getIndexData()
        assertNotNull(retrievedIndex)
        assertEquals(indexEntity.pinnedArticleIds, retrievedIndex?.pinnedArticleIds)
        assertEquals(indexEntity.focusArticleIds, retrievedIndex?.focusArticleIds)
    }
}