package com.lukevanoort.chall500px.photo

import kotlin.random.Random

interface Photo {
    val id: Int
    val name: String
    val description: String
    val thumbUrl: String
    val fullsizeUrl: String
    val photographerName: String
    val width: Int
    val height: Int
}

data class MockPhoto(
    override val id: Int,
    override val name: String ="Excellent Photo",
    override val thumbUrl: String = "",
    override val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed feugiat leo nunc, quis posuere diam congue quis. Phasellus at sem at eros consectetur mattis. Vestibulum molestie, nisl eget facilisis vehicula, orci ipsum vestibulum ante, id tempus leo turpis eu justo.",
    override val fullsizeUrl: String = "",
    override val photographerName: String = "John Doe"
): Photo {
    override val width: Int
    override val height: Int
    init {
        val rnd = Random(id)
        width = when(rnd.nextInt(3)) {
            0 -> 1024
            1 -> 500
            2 -> 650
            else -> 800
        }
        height = when(rnd.nextInt(3)) {
            0 -> 1024
            1 -> 500
            2 -> 650
            else -> 800
        }
    }

}
