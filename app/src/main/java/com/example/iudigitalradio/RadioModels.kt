package com.example.iudigitalradio

data class RadioStation(
    val id: Int,
    val name: String,
    val description: String,
    val frequency: String,
    val audioResId: Int?
)
// MEDIA 3 EXOPLAYER
// 3 Emisoras Reales Media 3 ExoPlayer
// 2 Emisoras Simuladas con estados visibles
val radioStations = listOf(
    // ==================================================
    // EMISORA 1 - AUDIO REAL  (Poner propio Audio en RAW)
    // ==================================================
    RadioStation(
        id = 1,
        name = "RadioIU Campus",
        description = "Vida universitaria",
        frequency = "95.00 FM",
        audioResId = R.raw.emisora_radio1
    ),
    // ==================================================
    // EMISORA 2 - AUDIO REAL (Poner propio Audio en RAW)
    // ==================================================
    RadioStation(
        id = 2,
        name = "RadioIU Cultura",
        description = "Arte y cultura",
        frequency = "98.50 FM",
        audioResId = R.raw.emisora_radio2
    ),
    // ==================================================
    // EMISORA 3 - AUDIO REAL  (Poner propio Audio en RAW)
    // ==================================================

    RadioStation(
        id = 3,
        name = "RadioIU Music",
        description = "Música variada",
        frequency = "101.20 FM",
        audioResId = R.raw.emisora_radio3
    ),

    // ==================================================
    // EMISORA 4 - SIMULADA
    // ==================================================

    RadioStation(
        id = 4,
        name = "RadioIU Noticias",
        description = "Actualidad universitaria",
        frequency = "104.30 FM",
        audioResId = null
    ),

    // ==================================================
    // EMISORA 5 - SIMULADA
    // ==================================================

    RadioStation(
        id = 5,
        name = "RadioIU Digital",
        description = "Contenido digital",
        frequency = "107.50 FM",
        audioResId = null
    )
)
