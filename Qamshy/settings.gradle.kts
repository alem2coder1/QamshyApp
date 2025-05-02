pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://artifactory.2gis.dev/sdk-maven-release") }
        maven { url = uri("https://developer.huawei.com/repo/") }
        maven { url = uri("https://jitpack.io")}
        maven {
            name = "GitHubPackages"
            url  = uri("https://maven.pkg.github.com/zhanel01/AndroidRepo")
            credentials {
                username = providers.gradleProperty("gpr.user")
                    .orNull
                    ?: providers.environmentVariable("GITHUB_USER").orNull
                            ?: ""
                password = providers.gradleProperty("gpr.key")
                    .orNull
                    ?: providers.environmentVariable("GITHUB_TOKEN").orNull
                            ?: ""
            }
        }

    }
}


rootProject.name = "Qamshy"
include(":app")
