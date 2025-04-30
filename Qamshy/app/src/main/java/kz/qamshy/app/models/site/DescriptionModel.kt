package kz.qamshy.app.models.site

data class DescriptionModel (
    val tagSiteList : List<TagItemModel> = emptyList(),
    val article:ArticleModel = ArticleModel(),
    val reletedArticleList : List<ArticleModel> = emptyList()
)