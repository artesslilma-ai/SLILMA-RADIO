SLILMA RADIO - Proyecto Android

Este proyecto crea una aplicación Android sencilla para escuchar el streaming:
https://sonicpanel.zonaradio.net/8068/stream

REQUISITOS
- Android Studio instalado.
- Conexión a Internet para descargar Gradle y las dependencias.
- JDK incluido con Android Studio.

CÓMO ABRIR
1. Extrae el ZIP en tu computadora.
2. Abre Android Studio.
3. Selecciona Open y elige la carpeta SLILMA_RADIO_Android.
4. Espera a que termine Gradle Sync.
5. Conecta un teléfono Android con depuración USB o usa un emulador.
6. Presiona Run (triángulo verde) para instalar y probar.

CÓMO GENERAR APK DE PRUEBA
1. En Android Studio abre Build > Build Bundle(s) / APK(s) > Build APK(s).
2. Cuando termine, pulsa Locate.
3. El APK de prueba estará normalmente en app/build/outputs/apk/debug/app-debug.apk.

IMPORTANTE
- Este ZIP contiene el proyecto fuente, no un APK ya compilado ni firmado para Play Store.
- Para publicar en Google Play se debe generar un Android App Bundle (AAB) firmado con una clave de lanzamiento y completar la ficha de Play Console.
- El streaming necesita estar activo y accesible por HTTPS.
