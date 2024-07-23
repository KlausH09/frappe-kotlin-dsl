package io.github.klahap.fraplin.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.coroutines.executeAsync


fun HttpUrl.newBuilder(block: HttpUrl.Builder.() -> Unit) = newBuilder().apply(block).build()

fun Request.newBuilder(block: Request.Builder.(request: Request) -> Unit) =
    newBuilder().apply { block(this@newBuilder) }.build()

fun requestBuilder(block: Request.Builder.() -> Unit) = Request.Builder().apply(block).build()


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Request.send(client: OkHttpClient, responseHandler: Response.() -> T): T =
    client.newCall(this).executeAsync().use { responseHandler(it) }

fun Response.throwIfNotSuccessful() {
    if (isSuccessful) return
    val msg = message.takeIf { it.isNotBlank() } ?: body.string()
    throw Exception("$code, $msg")
}

fun Response.getBodyIfSuccessfulOrThrow(): ResponseBody {
    throwIfNotSuccessful()
    return body
}

inline fun <reified T> Response.getJsonIfSuccessfulOrThrow(json: Json = Json): T =
    json.decodeFromString(getBodyIfSuccessfulOrThrow().string())

fun JsonElement.toRequestBody() =
    Json.encodeToString(this).toRequestBody("application/json; charset=utf-8".toMediaType())
