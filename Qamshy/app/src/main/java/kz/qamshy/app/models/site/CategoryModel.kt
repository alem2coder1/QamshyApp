package kz.qamshy.app.models.site

data class CategoryModel(
    val categories: List<CategoryGroup> = emptyList()
)

data class CategoryGroup(
    val title: String = "",
    val categoryList: List<CategoryList> = emptyList()
)

data class CategoryList(
    val id: Int = 0,
    val title: String = "",
)
data class ArticleListModel(
    val keyword:String = "",
    val articleList:List<ArticleModel> = emptyList()
)
