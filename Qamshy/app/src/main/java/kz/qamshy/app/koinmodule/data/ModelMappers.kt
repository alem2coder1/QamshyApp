package kz.qamshy.app.koinmodule.data

import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.models.site.TagModel

object ModelMappers {
    fun mapArticleToEntity(article: ArticleModel): ArticleEntity {
        return ArticleEntity(
            id = article.id,
            title = article.title,
            shortDescription = article.shortDescription,
            fullDescription = article.fullDescription,
            thumbnailUrl = article.thumbnailUrl,
            categoryTitle = article.categoryTitle,
            addTime = article.addTime,
            videoPath = article.videoPath,
            latynUrl = article.latynUrl,
            likeCount = article.likeCount,
            author = article.author,
            thumbnailCopyright = article.thumbnailCopyright
        )
    }

    // 将ArticleEntity和TagList转换回ArticleModel
    fun mapEntityToArticle(entity: ArticleEntity, tagList: List<TagModel> = emptyList()): ArticleModel {
        return ArticleModel(
            id = entity.id,
            title = entity.title,
            shortDescription = entity.shortDescription,
            fullDescription = entity.fullDescription,
            thumbnailUrl = entity.thumbnailUrl,
            categoryTitle = entity.categoryTitle,
            addTime = entity.addTime,
            videoPath = entity.videoPath,
            latynUrl = entity.latynUrl,
            likeCount = entity.likeCount,
            author = entity.author,
            thumbnailCopyright = entity.thumbnailCopyright,
            tagList = tagList
        )
    }

    fun mapTagToEntity(tag: TagModel): TagEntity {
        return TagEntity(
            id = tag.id,
            title = tag.title
        )
    }

    fun mapEntityToTag(entity: TagEntity): TagModel {
        return TagModel(
            id = entity.id,
            title = entity.title
        )
    }
}