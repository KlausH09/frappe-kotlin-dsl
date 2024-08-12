package io.github.klahap.fraplin.models.config

import io.github.klahap.fraplin.util.PathSerializer
import kotlinx.serialization.Serializable
import java.nio.file.Path

@Serializable
data class FraplinOpenApiConfig(
    val title: String,
    val version: String,
    val pathPrefix: String,
    val schemaPrefix: String,
    val pathTags: Set<String>,
    @Serializable(PathSerializer::class) val path: Path,
) {
    class Builder(
        var title: String = "Autogenerated OpenApi spec by Fraplin",
        var version: String = "1.0.0",
        val pathTags: MutableSet<String> = mutableSetOf(),
        var path: Path? = null,
        var pathPrefix: String = "/frappe/",
        var schemaPrefix: String = "Frappe",
    ) {
        fun build() = FraplinOpenApiConfig(
            title = title,
            version = version,
            pathTags = pathTags.toSet(),
            path = path ?: throw Exception("no output path for OpenApi spec defined"),
            schemaPrefix = schemaPrefix,
            pathPrefix = pathPrefix,
        )
    }
}