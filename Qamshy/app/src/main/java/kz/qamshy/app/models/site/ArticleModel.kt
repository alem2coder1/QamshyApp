package kz.qamshy.app.models.site

data class ArticleModel(
    val id: Int = 0,
    val title: String = "",
    val shortDescription: String = "",
    val thumbnailUrl: String = "",
    val categoryTitle: String = "",
    val addTime: String = "",
    val fullDescription: String = "",
    val videoPath: String = "",
    val latynUrl:String = "",
    val likeCount:Int = 0,
    val author:String = "",
    val thumbnailCopyright:String = "",
    val tagList:List<TagModel> = emptyList()
)

data class TagModel(
     val id:Int = 0,
    val title:String = ""
)

data class IndexViewModel(
    val pinnedArticleList:List<ArticleModel> = emptyList(),
    val focusArticleList:List<ArticleModel> = emptyList(),
    val blockList:List<ArticleBlockModel> = emptyList()
)

data class ArticleBlockModel(
    val categoryTitle:String,
    val articleList:List<ArticleModel>
)

data class ArticleListViewModel(
    val tag:TagModel,
    val articles:List<ArticleModel>
)
data class ArticleViewModel(
    val article:ArticleModel,
    val tagList:List<TagModel>,
    val relatedArticles:List<ArticleModel>
)