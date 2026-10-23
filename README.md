# Lab10 · CRUD de API con Retrofit (GLAB-S10 BPAREJA)

App de Android (Kotlin + Jetpack Compose) que hace el CRUD de series contra un servidor Django REST Framework: `GET serie`, `GET serie/{id}`, `POST serie`, `PUT serie/{id}` y `DELETE serie/{id}`.

Programación en Móviles · Tecsup · Benjamin Pareja · 2026-01.

## Cómo correrlo

1. **Servidor** (carpeta `backend-django/`, el proyecto `lab08` del laboratorio de DRF):
   ```
   python manage.py migrate
   python seed_series.py
   python manage.py runserver 0.0.0.0:8000
   ```
   - Tiene que escuchar en `0.0.0.0`, no en la IP del Wi-Fi: el emulador llega a tu PC por su `127.0.0.1` (alias `10.0.2.2`).
   - `ALLOWED_HOSTS` debe incluir `"10.0.2.2"`, o Django responde 400 `DisallowedHost`.
2. **App**: abrir en Android Studio y correr en un emulador. En Android 17 acepta el permiso de «dispositivos cercanos» al abrirla (es `ACCESS_LOCAL_NETWORK`).

## Lo que la guía no dice (y está corregido en los commits)

| Problema | Commit |
|---|---|
| Faltan `navigation-compose` y `material-icons-extended` | «Dependencias que la guia no lista» |
| `ScreenInicio()` se usa pero no se define | «ScreenInicio…» |
| Android 17: sin `ACCESS_LOCAL_NETWORK` la conexión a 10.0.2.2 da `SocketTimeoutException` | «Android 17: pedir ACCESS_LOCAL_NETWORK…» |
| Una excepción de red dentro de `LaunchedEffect` cierra la app | «Listado: capturar el error de red…» |
| Grabar: `navigate()` al componer cancela la petición (a veces no llega el PUT); un rating vacío lanza `NumberFormatException` | «Editar: grabar desde onClick…» |
| Eliminar: `navigate()` dentro del contenido del botón Cancelar corre al componer y el diálogo nunca se queda | «Eliminar: navegar desde onClick…» |
| `currentDestination` no es estado de Compose: la barra inferior no marca la pestaña | «BarraInferior: selected reactivo…» |

Video del funcionamiento: `demo/GLAB-S10-Lab10-demo.mp4`.
