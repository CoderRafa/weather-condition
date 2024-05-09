package com.rafengimprove.study.meteo

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class WeatherController(private val restTemplate: RestTemplate) {

    private val apiKey = "8a9f017984e7d7bcc9f8cc337143df49"
    @GetMapping("/weather")
    fun getWeatherByCoordinates(
        @RequestParam lat: Double, @RequestParam lon: Double
    ): String {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey"

        val response = restTemplate.getForEntity(url, String::class.java)
        val responseBody = response.body

        if (response.statusCode.is2xxSuccessful && responseBody != null) {
            val jsonResponse = ObjectMapper().readTree(responseBody)

            val description = jsonResponse["weather"][0]["description"].asText()

            if (description == "moderate rain" || description == "mist") {
                return "mild"
            } else if (description == "heavy intensity rain" || description == "snow") {
                return "hard"
            } else {
                return "soft"
            }
        } else {
           ResponseEntity.status(response.statusCode)
        }

        return "No information"
    }
}