    import org.gradle.kotlin.dsl.develocity

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
        }
    }

    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
        }
    }

    plugins {
        id("com.gradle.develocity") version("4.0.1")
    }

    develocity {
        buildScan {
            termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
            termsOfUseAgree.set("yes")
        }
    }

    rootProject.name = "Reparalo"
    include(":app")