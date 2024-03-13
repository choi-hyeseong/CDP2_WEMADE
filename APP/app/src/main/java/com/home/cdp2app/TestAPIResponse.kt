package com.home.cdp2app

data class TestAPIResponse (
    val result: Boolean,
    val ip: String,
    val countryCode: String,
    val countryName: List<Country>
)

data class Country (
    val de: String,
    val en: String,
    val es: String,
    val fa: String,
    val fr: String,
    val ja: String,
    val ko: String,
    val ptBR: String,
    val ru: String,
    val zhCN: String
)

