package kz.qamshy.app.common

import android.content.Context
import android.content.Intent
import android.net.Uri


fun openFacebook(context: Context, facebookUrl: String) {
    val intent = try {
        context.packageManager.getPackageInfo("com.facebook.katana", 0)
        Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$facebookUrl"))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
    }
    context.startActivity(intent)
}

fun openInstagram(context: Context, instagramUsername: String, instagramUrl: String) {
    val intent = try {
        context.packageManager.getPackageInfo("com.instagram.android", 0)
        Intent(Intent.ACTION_VIEW, Uri.parse("instagram://user?username=$instagramUsername"))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
    }
    context.startActivity(intent)
}

fun openTelegram(context: Context, telegramUsername: String, telegramUrl: String) {
    val intent = try {
        context.packageManager.getPackageInfo("org.telegram.messenger", 0)
        Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$telegramUsername"))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl))
    }
    context.startActivity(intent)
}

fun openWhatsApp(context: Context, phoneNumber: String, whatsappUrl: String) {
    val intent = try {
        context.packageManager.getPackageInfo("com.whatsapp", 0)
        Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber"))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
    }
    context.startActivity(intent)
}

fun openVK(context: Context, vkProfileId: String, vkUrl: String) {
    val intent = try {
        context.packageManager.getPackageInfo("com.vkontakte.android", 0)
        Intent(Intent.ACTION_VIEW, Uri.parse("vk://vk.com/id$vkProfileId"))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(vkUrl))
    }
    context.startActivity(intent)
}

fun openTwitter(context: Context, twitterUsername: String, twitterUrl: String) {
    val intent = try {
        context.packageManager.getPackageInfo("com.twitter.android", 0)
        Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=$twitterUsername"))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl))
    }
    context.startActivity(intent)
}
