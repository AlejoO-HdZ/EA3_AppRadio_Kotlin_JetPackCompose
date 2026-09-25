package com.example.iudigitalradio

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeOff

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.File
// ==================================================
// RADIO SCREEN
// ==================================================
@Composable
fun RadioScreen() {
    val context =
        LocalContext.current
    // ==================================================
    // ESTADOS PRINCIPALES
    // ==================================================
    var selectedStationId by rememberSaveable {
        mutableStateOf(1)
    }
    var isPlaying by rememberSaveable {
        mutableStateOf(false)
    }
    var isMuted by rememberSaveable {
        mutableStateOf(false)
    }
    // ==================================================
    // FOTOGRAFÍA DE PERFIL
    // ==================================================
    var profileImageUri by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    // ==================================================
    // MENSAJE DE VIBRACIÓN
    // ==================================================
    var vibrationMessage by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    // ==================================================
    // OCULTAR MENSAJE
    // ==================================================

    LaunchedEffect(vibrationMessage) {

        if (vibrationMessage != null) {

            kotlinx.coroutines.delay(700L)

            vibrationMessage = null
        }
    }


    // ==================================================
    // LANZADOR DE CÁMARA
    // ==================================================

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->

            if (bitmap != null) {

                val savedUri =
                    saveProfilePhoto(
                        context = context,
                        bitmap = bitmap
                    )

                if (savedUri != null) {

                    profileImageUri =
                        savedUri.toString()

                    Toast.makeText(
                        context,
                        "📷 Fotografía actualizada",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        context,
                        "No se pudo guardar la fotografía",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    // ==================================================
    // PERMISO DE CÁMARA
    // ==================================================

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {

                cameraLauncher.launch(null)

            } else {

                Toast.makeText(
                    context,
                    "Permiso de cámara no concedido",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    // ==================================================
    // ABRIR CÁMARA
    // ==================================================

    fun openCamera() {

        val permissionStatus =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )

        if (
            permissionStatus ==
            PackageManager.PERMISSION_GRANTED
        ) {

            cameraLauncher.launch(null)

        } else {

            cameraPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }


    // ==================================================
    // EMISORA SELECCIONADA
    // ==================================================

    val selectedStation =
        radioStations.first {
            it.id == selectedStationId
        }


    // ==================================================
    // ÍNDICE DE LA EMISORA
    // ==================================================

    val selectedIndex =
        radioStations.indexOfFirst {
            it.id == selectedStationId
        }


    // ==================================================
    // RECURSO DE AUDIO
    // ==================================================

    val audioResId =
        audioResourceForStation(
            selectedStationId
        )


    // ==================================================
    // CAMBIO DE EMISORA
    // ==================================================

    LaunchedEffect(
        selectedStationId
    ) {

        if (audioResId != null) {

            AudioPlayerManager.loadStation(
                context = context,
                audioResId = audioResId
            )

            AudioPlayerManager.setMuted(
                context = context,
                muted = isMuted
            )

            if (isPlaying) {

                AudioPlayerManager.play(
                    context
                )
            }

        } else {

            AudioPlayerManager.stop()
        }
    }


    // ==================================================
    // PLAY / PAUSE
    // ==================================================

    LaunchedEffect(
        selectedStationId,
        isPlaying
    ) {

        if (audioResId != null) {

            if (isPlaying) {

                AudioPlayerManager.play(
                    context
                )

            } else {

                AudioPlayerManager.pause(
                    context
                )
            }
        }
    }


    // ==================================================
    // MUTE
    // ==================================================

    LaunchedEffect(
        isMuted
    ) {

        AudioPlayerManager.setMuted(
            context = context,
            muted = isMuted
        )
    }


    // ==================================================
    // EMISORA ANTERIOR
    // ==================================================

    fun previousStation() {

        selectedStationId =
            if (selectedIndex > 0) {

                radioStations[
                    selectedIndex - 1
                ].id

            } else {

                radioStations.last().id
            }

        isPlaying = true
    }


    // ==================================================
    // EMISORA SIGUIENTE
    // ==================================================

    fun nextStation() {

        selectedStationId =
            if (
                selectedIndex <
                radioStations.lastIndex
            ) {

                radioStations[
                    selectedIndex + 1
                ].id

            } else {

                radioStations.first().id
            }

        isPlaying = true
    }


    // ==================================================
    // SELECCIÓN MANUAL
    // ==================================================

    fun selectStation(
        stationId: Int
    ) {

        selectedStationId =
            stationId

        isPlaying = true
    }


    // ==================================================
    // ORIENTACIÓN
    // ==================================================

    val configuration =
        LocalConfiguration.current

    val isLandscape =
        configuration.orientation ==
                Configuration.ORIENTATION_LANDSCAPE


    // ==================================================
    // HORIZONTAL
    // ==================================================

    if (isLandscape) {

        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),

            horizontalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                ProfileSection(
                    profileImageUri =
                        profileImageUri,

                    vibrationMessage =
                        vibrationMessage,

                    onCameraClick =
                        ::openCamera
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                PlayerSection(
                    selectedStation =
                        selectedStation,

                    isPlaying =
                        isPlaying,

                    isMuted =
                        isMuted,

                    onPlayPause = {

                        vibrateDevice(context)

                        vibrationMessage =
                            if (isPlaying) {
                                "📳 Vibración: Pausa"
                            } else {
                                "📳 Vibración: Play"
                            }

                        isPlaying =
                            !isPlaying
                    },

                    onMute = {

                        vibrateDevice(context)

                        vibrationMessage =
                            if (isMuted) {
                                "📳 Vibración: Sonido"
                            } else {
                                "📳 Vibración: Mute"
                            }

                        isMuted =
                            !isMuted
                    },

                    onPrevious = {

                        vibrateDevice(context)

                        vibrationMessage =
                            "📳 Vibración: Anterior"

                        previousStation()
                    },

                    onNext = {

                        vibrateDevice(context)

                        vibrationMessage =
                            "📳 Vibración: Siguiente"

                        nextStation()
                    }
                )
            }


            StationList(
                modifier =
                    Modifier.weight(1f),

                selectedStationId =
                    selectedStationId,

                onStationSelected =
                    ::selectStation
            )
        }

    } else {

        // ==================================================
        // VERTICAL
        // ==================================================

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
        ) {

            ProfileSection(
                profileImageUri =
                    profileImageUri,

                vibrationMessage =
                    vibrationMessage,

                onCameraClick =
                    ::openCamera
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            PlayerSection(
                selectedStation =
                    selectedStation,

                isPlaying =
                    isPlaying,

                isMuted =
                    isMuted,

                onPlayPause = {

                    vibrateDevice(context)

                    vibrationMessage =
                        if (isPlaying) {
                            "📳 Vibración: Pausa"
                        } else {
                            "📳 Vibración: Play"
                        }

                    isPlaying =
                        !isPlaying
                },

                onMute = {

                    vibrateDevice(context)

                    vibrationMessage =
                        if (isMuted) {
                            "📳 Vibración: Sonido"
                        } else {
                            "📳 Vibración: Mute"
                        }

                    isMuted =
                        !isMuted
                },

                onPrevious = {

                    vibrateDevice(context)

                    vibrationMessage =
                        "📳 Vibración: Anterior"

                    previousStation()
                },

                onNext = {

                    vibrateDevice(context)

                    vibrationMessage =
                        "📳 Vibración: Siguiente"

                    nextStation()
                }
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            StationList(
                modifier =
                    Modifier.weight(1f),

                selectedStationId =
                    selectedStationId,

                onStationSelected =
                    ::selectStation
            )
        }
    }
}


// ==================================================
// AUDIO DE CADA EMISORA
// ==================================================

fun audioResourceForStation(
    stationId: Int
): Int? {

    return when (stationId) {

        1 ->
            R.raw.emisora_radio1

        2 ->
            R.raw.emisora_radio2

        3 ->
            R.raw.emisora_radio3

        // Emisoras simuladas
        4,
        5 ->
            null

        else ->
            null
    }
}


// ==================================================
// PERFIL: Sección superior (Perfil y camara)
// ==================================================
@Composable
fun ProfileSection(
    profileImageUri: String?,
    vibrationMessage: String?,
    onCameraClick: () -> Unit
) {
    val profileBitmap =
        remember(profileImageUri) {
            if (profileImageUri != null) {
                try {
                    val uri =
                        Uri.parse(profileImageUri)
                    val path =
                        uri.path
                    if (path != null) {
                        BitmapFactory.decodeFile(
                            File(path).absolutePath
                        )
                    } else {
                        null
                    }
                } catch (
                    exception: Exception
                ) {
                    null
                }
            } else {
                null
            }
        }
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                contentAlignment =
                    Alignment.BottomEnd
            ) {

                Surface(
                    modifier =
                        Modifier
                            .height(82.dp)
                            .width(82.dp)
                            .clip(CircleShape)
                            .clickable {
                                onCameraClick()
                            },

                    color =
                        MaterialTheme
                            .colorScheme
                            .surfaceVariant
                ) {

                    if (profileBitmap != null) {

                        Image(
                            bitmap =
                                profileBitmap
                                    .asImageBitmap(),

                            contentDescription =
                                "Fotografía de perfil",

                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .clip(
                                        CircleShape
                                    )
                        )

                    } else {

                        Box(
                            modifier =
                                Modifier.fillMaxSize(),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.Person,

                                contentDescription =
                                    "Perfil de usuario",

                                modifier =
                                    Modifier
                                        .height(50.dp)
                                        .width(50.dp),

                                tint =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
                            )
                        }
                    }
                }


                Surface(
                    modifier =
                        Modifier
                            .height(30.dp)
                            .width(30.dp)
                            .clip(CircleShape)
                            .clickable {
                                onCameraClick()
                            },

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.CameraAlt,

                            contentDescription =
                                "Tomar fotografía",

                            modifier =
                                Modifier
                                    .height(17.dp)
                                    .width(17.dp),

                            tint =
                                Color.White
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.width(14.dp)
            )


            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "Perfil: Estudiante IUD",

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        "Usuario de IU Digital Radio",

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall
                )
            }


            if (
                vibrationMessage != null
            ) {

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text =
                        vibrationMessage,

                    style =
                        MaterialTheme
                            .typography
                            .labelSmall,

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}

// ==================================================
// REPRODUCTOR CENTRAL: Sección central
// ==================================================
@Composable
fun PlayerSection(
    selectedStation: RadioStation,
    isPlaying: Boolean,
    isMuted: Boolean,
    onPlayPause: () -> Unit,
    onMute: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(16.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            // ==================================================
            // TÍTULO
            // ==================================================
            Text(
                text =
                    "IU Digital Radio",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )


            // ==================================================
            // NOMBRE + FRECUENCIA + DESCRIPCIÓN
            //
            // TODO EN UNA SOLA LÍNEA
            // ==================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        selectedStation.name,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold,

                    maxLines = 1
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text =
                        "—",

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text =
                        stationFrequency(
                            selectedStation.id
                        ),

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    maxLines = 1
                )

                Spacer(
                    modifier =
                        Modifier.width(5.dp)
                )

                Text(
                    text =
                        "(${selectedStation.description})",

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,

                    maxLines = 1
                )
            }


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            // ==================================================
            // ESTADO
            // ==================================================

            Text(
                text =
                    when {

                        isMuted ->
                            "🔇 Audio silenciado"

                        isPlaying &&
                                audioResourceForStation(
                                    selectedStation.id
                                ) == null ->
                            "▶ Estado de reproducción simulado."

                        isPlaying ->
                            "▶ Reproduciendo Media3 ExoPlayer Real"

                        else ->
                            "⏸ En pausa"
                    },

                fontWeight =
                    FontWeight.Medium
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // ==================================================
            // CONTROLES
            // ==================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceEvenly,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Button(
                    onClick =
                        onPrevious,

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF3F51B5),

                            contentColor =
                                Color.White
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.SkipPrevious,

                        contentDescription =
                            "Emisora anterior"
                    )
                }


                Button(
                    onClick =
                        onPlayPause,

                    colors =
                        if (isPlaying) {

                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF757575),

                                contentColor =
                                    Color.White
                            )

                        } else {

                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF43A047),

                                contentColor =
                                    Color.White
                            )
                        }
                ) {

                    Icon(
                        imageVector =
                            if (isPlaying) {
                                Icons.Filled.Pause
                            } else {
                                Icons.Filled.PlayArrow
                            },

                        contentDescription =
                            if (isPlaying) {
                                "Pausar"
                            } else {
                                "Reproducir"
                            }
                    )
                }


                Button(
                    onClick =
                        onNext,

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF3F51B5),

                            contentColor =
                                Color.White
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.SkipNext,

                        contentDescription =
                            "Siguiente emisora"
                    )
                }


                Button(
                    onClick =
                        onMute,

                    colors =
                        if (isMuted) {

                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFD32F2F),

                                contentColor =
                                    Color.White
                            )

                        } else {

                            ButtonDefaults.buttonColors(
                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .primary,

                                contentColor =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary
                            )
                        }
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.VolumeOff,

                        contentDescription =
                            if (isMuted) {
                                "Activar sonido"
                            } else {
                                "Silenciar"
                            }
                    )
                }
            }
        }
    }
}


// ==================================================
// ICONOS DE LAS EMISORAS
// ==================================================

fun stationIcon(
    stationId: Int
): ImageVector {

    return when (stationId) {

        1 ->
            Icons.Filled.School

        2 ->
            Icons.Filled.Palette

        3 ->
            Icons.Filled.MusicNote

        4 ->
            Icons.Filled.Newspaper

        else ->
            Icons.Filled.MusicNote
    }
}


// ==================================================
// FRECUENCIAS
// ==================================================

fun stationFrequency(
    stationId: Int
): String {

    return when (stationId) {

        1 ->
            "95.00 FM"

        2 ->
            "98.50 FM"

        3 ->
            "101.20 FM"

        4 ->
            "104.70 FM"

        5 ->
            "107.30 FM"

        else ->
            "--.-- FM"
    }
}


// ==================================================
// LISTA DE EMISORAS: Sección inferior (Catálogo de Emisoras con LazyColumn)
//
// ==================================================
@Composable
fun StationList(
    modifier: Modifier = Modifier,
    selectedStationId: Int,
    onStationSelected: (Int) -> Unit
) {
    Column(
        modifier =
            modifier
    ) {
        Text(
            text =
                "Emisoras disponibles",
            style =
                MaterialTheme
                    .typography
                    .titleMedium,
            fontWeight =
                FontWeight.Bold
        )
        Spacer(
            modifier =
                Modifier.height(8.dp)
        )
        // (Catálogo de Emisoras con LazyColumn)
        LazyColumn(
            modifier =
                Modifier.fillMaxSize()
        ) {

            items(
                items =
                    radioStations,

                key = { station ->
                    station.id
                }

            ) { station ->

                val isSelected =
                    station.id ==
                            selectedStationId


                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 5.dp
                            ),

                    onClick = {

                        onStationSelected(
                            station.id
                        )
                    },

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                if (isSelected) {

                                    MaterialTheme
                                        .colorScheme
                                        .primaryContainer

                                } else {

                                    MaterialTheme
                                        .colorScheme
                                        .surfaceVariant
                                }
                        ),

                    elevation =
                        CardDefaults.cardElevation(

                            defaultElevation =
                                if (isSelected) {
                                    6.dp
                                } else {
                                    2.dp
                                }
                        )
                ) {

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                stationIcon(
                                    station.id
                                ),

                            contentDescription =
                                station.name,

                            modifier =
                                Modifier
                                    .height(40.dp)
                                    .width(40.dp),

                            tint =
                                if (isSelected) {

                                    MaterialTheme
                                        .colorScheme
                                        .primary

                                } else {

                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
                                }
                        )


                        Spacer(
                            modifier =
                                Modifier.width(14.dp)
                        )


                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween,

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(
                                    text =
                                        station.name,

                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleSmall,

                                    fontWeight =
                                        if (isSelected) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Medium
                                        }
                                )


                                Text(
                                    text =
                                        stationFrequency(
                                            station.id
                                        ),

                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyMedium,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                )
                            }


                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )


                            Text(
                                text =
                                    station.description,

                                style =
                                    MaterialTheme
                                        .typography
                                        .bodySmall
                            )


                            if (
                                audioResourceForStation(
                                    station.id
                                ) == null
                            ) {

                                Spacer(
                                    modifier =
                                        Modifier.height(2.dp)
                                )

                                Text(
                                    text =
                                        "Emisora simulada",

                                    style =
                                        MaterialTheme
                                            .typography
                                            .labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ==================================================
// @PREVIEW DE COMPOSE
// Vista componentes sin ejecutar aplicación.
// ==================================================

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "IU Digital Radio"
)
@Composable
fun RadioScreenPreview() {

    MaterialTheme {

        RadioScreen()
    }
}

