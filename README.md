📻 IU Digital Radio

Aplicación móvil nativa desarrollada para Android utilizando Kotlin y Jetpack Compose, como parte de una evidencia práctica de desarrollo móvil.

La aplicación simula una plataforma de radio digital, permitiendo seleccionar emisoras, reproducir archivos de audio, controlar la reproducción, capturar una fotografía de perfil y utilizar retroalimentación háptica mediante la vibración del dispositivo.

📱 Descripción del proyecto

IU Digital Radio implementa una interfaz moderna desarrollada completamente con Jetpack Compose.

La aplicación cuenta con:

- Perfil de usuario con fotografía.

- Captura de fotografía mediante la cámara del dispositivo.

-  Catálogo de cinco emisoras.

-  Reproducción y pausa de audio.

-  Control de silencio.

-   Cambio a la emisora anterior.

-   Cambio a la siguiente emisora.

-  Navegación circular entre las emisoras.

-  Retroalimentación háptica mediante vibración.

-  Reproducción de archivos MP3 mediante Media3 ExoPlayer.

-  Adaptación a cambios de orientación del dispositivo.

-  Conservación del estado mediante rememberSaveable.

🛠️ Tecnologías utilizadas
Tecnología	Uso
Kotlin	Lenguaje principal
Android Studio	Entorno de desarrollo
Jetpack Compose	Construcción de la interfaz
Material 3	Componentes y diseño visual
Media3 ExoPlayer	Reproducción de audio
TakePicturePreview	Captura de fotografía
Vibrator / VibratorManager	Retroalimentación háptica
Gradle	Sistema de compilación
Git / GitHub	Control y publicación del código

`
🏗️ Estructura del proyecto
```text
IU Digital Radio
│
├── app
│   │
│   ├── src
│   │   └── main
│   │       │
│   │       ├── java
│   │       │   └── com.example.iudigitalradio
│   │       │       ├── MainActivity.kt
│   │       │       └── devicesFeatures.kt
│   │       │       └── RadioModels.kt
│   │       │       └── RadioScreen.kt
│   │       │
│   │       ├── res
│   │       │   ├── raw
│   │       │   │   ├── emisora_radio1.mp3
│   │       │   │   ├── emisora_radio2.mp3
│   │       │   │   └── emisora_radio3.mp3
│   │       │   ├── drawable
│   │       │   ├── mipmap
│   │       │   └── values
│   │       │
│   │       └── AndroidManifest.xml
│   │
│   └── build.gradle.kts
│
├── gradle
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```
🎧 Emisoras`

La aplicación cuenta con un catálogo de cinco emisoras simuladas.

Las primeras tres utilizan archivos de audio locales, previamente definidas
NOTA: Para efectos personalizados de la actividad se deja libre este recurso, se puede agregar audio propio a carpeta RES/RAW o url segun necesidad, con los siguientes nombres:

RadioIU 1 — emisora_radio1.mp3 
RadioIU 2 — emisora_radio2.mp3
RadioIU 3 — emisora_radio3.mp3

Las emisoras restantes forman parte de la simulación de la interfaz y pueden incorporar posteriormente sus respectivos archivos de audio.

Cada emisora presenta:

Nombre.

Frecuencia.

Descripción.

Icono representativo.

La navegación permite avanzar desde la última emisora hasta la primera y regresar desde la primera hasta la última.

🎵 Reproductor

El reproductor central utiliza Media3 ExoPlayer para gestionar los archivos de audio locales.

Cuenta con los siguientes controles:

Play: inicia la reproducción.

Pause: pausa el audio.

Mute: silencia o activa el sonido.

Anterior: cambia a la emisora anterior.

Siguiente: cambia a la siguiente emisora.

Cuando se selecciona una emisora desde el catálogo, la información del reproductor se actualiza para mostrar la emisora activa.

👤 Perfil y cámara

La sección superior permite gestionar la fotografía del perfil.

El usuario puede abrir la cámara mediante:

El botón con el icono de cámara.

El área del perfil.

La fotografía capturada reemplaza el icono predeterminado y se guarda en el almacenamiento interno de la aplicación.

El perfil muestra:

Perfil: Estudiante IUD

📳 Retroalimentación háptica

Los controles principales del reproductor generan una vibración corta al ser utilizados.

La aplicación utiliza:

Vibrator


o:

VibratorManager


dependiendo de la versión de Android.

La vibración está diseñada como una respuesta háptica breve para confirmar la interacción del usuario.

🔐 Permisos

La aplicación utiliza permisos de Android relacionados con:

Cámara.

Vibración.

El permiso de cámara se solicita durante la ejecución de la aplicación cuando se necesita acceder a la cámara.

Los permisos son gestionados mediante las APIs correspondientes de Android.

🔄 Gestión del estado

La aplicación utiliza el sistema de estado de Jetpack Compose.

Entre los estados principales se encuentran:

selectedStationId
isPlaying
isMuted
profileImageUri


Se utilizan mecanismos como:

remember
rememberSaveable
mutableStateOf


Esto permite que la interfaz reaccione a las acciones del usuario y conserve información importante ante determinados cambios de configuración, como la rotación de pantalla.

📱 Orientación de pantalla

La interfaz está diseñada para adaptarse a cambios de orientación del dispositivo.

El estado importante de la aplicación se conserva mediante los mecanismos de estado de Compose, evitando perder la selección realizada por el usuario durante la recreación de la interfaz.

🖼️ Previsualización con Compose

Durante el desarrollo se puede utilizar @Preview de Jetpack Compose para visualizar componentes de la interfaz directamente desde Android Studio sin ejecutar toda la aplicación en un dispositivo.

Ejemplo:

@Preview(showBackground = true)
@Composable
fun PreviewRadioApp() {
// Vista previa de la interfaz
}


Esta funcionalidad facilita la revisión visual de los componentes durante el desarrollo.

📦 Generación del APK

El proyecto puede compilarse desde Android Studio mediante:

Build
→ Generate App Bundles or APKs
→ Generate APKs

📋 Requisitos

Para ejecutar el proyecto se requiere:

Android Studio.

SDK de Android compatible con el proyecto.

Kotlin.

Gradle.

Un dispositivo Android físico o un emulador.

Permiso de cámara para utilizar la captura de fotografías.

🎯 Objetivo académico

El proyecto tiene como objetivo aplicar conceptos fundamentales del desarrollo móvil nativo en Android mediante:

Desarrollo declarativo con Jetpack Compose.

Manejo de estados.

Integración con hardware del dispositivo.

Gestión de permisos en tiempo de ejecución.

Reproducción multimedia.

Diseño de interfaces interactivas.

Generación y prueba de un APK.

👨‍💻 Modalidad

Modalidad: Trabajo Individual — Opción B.

Proyecto: IU Digital Radio

Plataforma: Android

Lenguaje: Kotlin

Interfaz: Jetpack Compose