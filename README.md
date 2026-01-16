# Aqui no hay quien viva api

Aplicación Android que consume la API de "Aquí no hay quien viva" para mostrar información sobre los personajes de la serie.

## Arquitectura

El proyecto está construido siguiendo los principios de Clean Architecture y el patrón de diseño MVVM, separando claramente las responsabilidades en capas de presentación, dominio y datos.

## Tecnologías Principales

El desarrollo se ha realizado utilizando Kotlin y las siguientes librerías y herramientas:

*   **Koin**: Gestión de inyección de dependencias.
*   **Retrofit**: Cliente HTTP para las peticiones a la API.
*   **Coil**: Librería para carga y visualización de imágenes.
*   **Jetpack Navigation**: Gestión de la navegación entre pantallas.
*   **ViewBinding**: Vinculación de vistas con el código.
*   **Coroutines**: Gestión de operaciones asíncronas.

## Estructura del Proyecto

El código fuente se organiza por características, donde cada una contiene su propia estructura interna de capas (data, domain, presentation).

## Requisitos de Compilación

*   Android SDK 36
*   JDK 11
*   Gradle con Kotlin DSL
