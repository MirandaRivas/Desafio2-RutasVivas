# RutasVivas — Agencia de Viajes

**Alumno:** Gladis Del Carmen Rivas Miranda #RM191684
**Materia:** Desarrollo de Software para Móviles
**Ciclo:** 02-2026
**Segundo Desafío Práctico**

## Descripción
Aplicación Android en Kotlin para una agencia de viajes que permite gestionar
un catálogo de destinos turísticos. Cuenta con sistema de autenticación
(Firebase Authentication) y un CRUD completo (Crear, Leer, Actualizar,
Eliminar) conectado a Firebase Realtime Database, con validaciones
obligatorias y gestión de imágenes mediante Glide.

## Video de defensa
[PEGA AQUÍ LA URL de tu video una vez lo subas, ej: https://youtu.be/xxxxxxx]

## Contenido de este repositorio
- **Código fuente:** carpeta `app/` (proyecto completo de Android Studio)
- **APK funcional:** carpeta `apk/app-debug.apk`
- **Documentación:** este archivo README.md

## Funcionalidades implementadas
- Login y Registro de usuarios con Firebase Authentication
- Catálogo de destinos (RecyclerView + CardView) con foto, nombre, precio y descripción
- Registro de nuevo destino: nombre, país (Spinner), precio, descripción, selector de imagen desde galería
- Edición de cualquier destino, incluyendo su imagen
- Eliminación de destinos con diálogo de confirmación previo
- Validaciones: campos obligatorios, precio mayor a 0, imagen obligatoria, descripción mínima de 20 caracteres, errores mostrados en pantalla
- Ícono y nombre personalizado de la aplicación
- Paleta de colores basada en materialpalette.com
- Todos los textos externalizados en `strings.xml`

## Tecnologías usadas
- Kotlin
- Firebase Authentication
- Firebase Realtime Database
- Glide (carga de imágenes)
- RecyclerView + CardView
- Almacenamiento local de imágenes (memoria interna del dispositivo)