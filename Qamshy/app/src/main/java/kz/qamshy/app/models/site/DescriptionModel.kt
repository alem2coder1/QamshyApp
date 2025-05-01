package kz.qamshy.app.models.site

data class DescriptionModel (
    val tagList:List<TagModel> = emptyList(),
    val article:ArticleModel = ArticleModel(),
    val reletedArticleList : List<ArticleModel> = emptyList(),
)