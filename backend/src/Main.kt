import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addCommandLineSource
import com.sksamuel.hoplite.addEnvironmentSource
import com.sksamuel.hoplite.addFileSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation.Plugin as ClientContentNegotiationPlugin
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiationPlugin

data class DemoConfig(
    val port: Int,
    val issuerEndpointUrl: String,
    val verifierEndpointUrl: String,
    val verifierStatusEndpointUrl: String,
    val clientId: String,
    val clientSecret: String,
    val tenantId: String,
    val scope: String
) {
    fun toEntraRequestObject(): JsonObject =
        buildJsonObject {
            put("clientId", clientId)
            put("clientSecret", clientSecret)
            put("tenantId", tenantId)
            put("scope", scope)
        }
}

fun main(args: Array<String>) {
    println("Entra Demo backend")
    val config = ConfigLoaderBuilder.default()
        .addEnvironmentSource()
        .addCommandLineSource(args)
        .addFileSource("demo.conf", optional = true)
        .build()
        .loadConfigOrThrow<DemoConfig>()


    val entraAuthorization = config.toEntraRequestObject()

    val http = HttpClient {
        install(ClientContentNegotiationPlugin) {
            json()
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(Logging)
    }

    embeddedServer(CIO, port = config.port) {
        install(ServerContentNegotiationPlugin) {
            json()
        }
        install(CallLogging)
        install(CORS) {
            allowHeaders { true }
            allowMethod(HttpMethod.Options)
            allowNonSimpleContentTypes = true

            /*allowHost("localhost:3000")
            allowHost("127.0.0.1:3000")
            allowHost("0.0.0.0:3000")
            allowHost("host.docker.internal:3000")*/
            allowOrigins { true }
        }

        routing {
            get("/") {
                call.respondText("Entra demo backend")
            }

            post("entra/issue") {
                val req = call.receive<JsonObject>()
                val entraReq = JsonObject(req.toMutableMap().apply {
                    put("authorization", entraAuthorization)
                })

                context.respond(
                    http.post(config.issuerEndpointUrl) {
                        setBody(entraReq)
                    }.bodyAsText()
                )
            }

            post("entra/verify") {
                val req = call.receive<JsonObject>()
                val entraReq = JsonObject(
                    req.toMutableMap().apply {
                        val prevEntraVerification = get("entraVerification")?.jsonObject?.toMutableMap()
                            ?: error("No entra verification supplied")
                        put("entraVerification", JsonObject(prevEntraVerification.apply {
                            put("authorization", entraAuthorization)
                        }))
                    }
                )

                context.respond(
                    http.post(config.verifierEndpointUrl) {
                        setBody(entraReq)
                    }.body<JsonObject>()
                )
            }

            get("entra/status/{nonce}") {
                val nonce = context.parameters["nonce"] ?: error("No nonce provided!")

                val resp = http.get(config.verifierStatusEndpointUrl.replace("%nonce%", nonce)) {
                    expectSuccess = false
                }

                when {
                    resp.status.isSuccess() -> context.respond(resp.status, resp.body<JsonObject>())
                    else -> context.respond(resp.status)
                }
            }
        }
    }.start(wait = true)
}
