package com.legion1900.moviesapp.data.impl.services

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.legion1900.moviesapp.data.impl.serialization.TMDBConfiguration
import java.lang.reflect.Type
import javax.inject.Inject

class ConfigurationDeserializer @Inject constructor() : JsonDeserializer<TMDBConfiguration> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TMDBConfiguration {
        return json.asJsonObject.getAsJsonObject("images").run {
            val posterSizes = getAsJsonArray("poster_sizes").toSizeMap()
            val backdropSizes = getAsJsonArray("backdrop_sizes").toSizeMap()
            val baseUrl = get("secure_base_url").asString
            TMDBConfiguration(TMDBConfiguration.Images(baseUrl, backdropSizes, posterSizes))
        }
    }

    private fun JsonArray.toSizeMap(): Map<TMDBConfiguration.Images.ImageSize, String> {
        val map = mutableMapOf<TMDBConfiguration.Images.ImageSize, String>()
        val tmp = size() - TMDBConfiguration.Images.ImageSize.values().size
        val smallest = if (tmp >= 0) tmp else 0
        val biggest = size() - 1
        for ((cnt, i) in (smallest..biggest).withIndex()) {
            val sizeVal = get(i).asString
            map += when (cnt) {
                0 -> TMDBConfiguration.Images.ImageSize.SMALL to sizeVal
                1 -> TMDBConfiguration.Images.ImageSize.MEDIUM to sizeVal
                2 -> TMDBConfiguration.Images.ImageSize.LARGE to sizeVal
                else -> TMDBConfiguration.Images.ImageSize.XLARGE to sizeVal
            }
        }
        return map
    }
}
