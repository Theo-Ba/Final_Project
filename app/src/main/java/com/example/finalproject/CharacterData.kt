package com.example.finalproject

data class CharacterData(
    val data: Data
) {
    data class Data(
        val images: Images,
        //val anime: Anime,
        val favorites: Int,
        val name: String
    ) {
        data class Images(
            val jpg: Jpg
        ) {
            data class Jpg(
                val image_url: String
            )
        }
//        data class Anime(
//            val anime: Anime
//        ) {
//            data class Anime(
//                val title: String
//            )
//        }
    }
}
