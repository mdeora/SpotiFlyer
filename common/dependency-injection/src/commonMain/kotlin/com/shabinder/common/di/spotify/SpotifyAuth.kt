/*
 *  * Copyright (c)  2021  Shabinder Singh
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  *  You should have received a copy of the GNU General Public License
 *  *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.shabinder.common.di.spotify

import com.shabinder.common.di.globalJson
import com.shabinder.common.models.methods
import com.shabinder.common.models.spotify.TokenData
import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters
import kotlin.native.concurrent.SharedImmutable

suspend fun authenticateSpotify(): TokenData? {
    return try {
        if (methods.value.isInternetAvailable) spotifyAuthClient.post("https://accounts.spotify.com/api/token") {
            body = FormDataContent(Parameters.build { append("grant_type", "client_credentials") })
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@SharedImmutable
private val spotifyAuthClient by lazy {
    HttpClient {
        val clientId = "694d8bf4f6ec420fa66ea7fb4c68f89d"
        val clientSecret = "02ca2d4021a7452dae2328b47a6e8fe8"

        install(Auth) {
            basic {
                sendWithoutRequest = true
                username = clientId
                password = clientSecret
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(globalJson)
        }
    }
}
