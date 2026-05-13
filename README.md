Aplicacion gestion de libros
Aplicacion gestion de libros es una aplicación de Android moderna diseñada para los amantes de la lectura. 
Permite buscar libros a través de la API de Google Books, gestionar una biblioteca personal local y realizar un seguimiento detallado de la lectura (páginas leídas, valoraciones y notas personales).

Características Principales
Búsqueda Global (Remota)
Consulta en Tiempo Real: Busca libros por título o autor conectándote directamente con la API de Google Books.

Gestión de Estados: Manejo robusto de estados de carga, errores de conexión (429, 503, etc.) y falta de resultados.

Detalle Extendido: Visualiza sinopsis completas y portadas en alta resolución antes de añadir un libro a tu colección.

Biblioteca Personal (Local)
Persistencia con Room: Guarda tus libros favoritos para consultarlos sin conexión a internet.

Buscador Inteligente: Filtra tu colección local por título o autor al instante.

Edición de Progreso: Registra en qué página te encuentras, añade comentarios personales y califica tus lecturas con estrellas.

Control de Fechas: Guarda automáticamente la fecha de inicio y fin de tus lecturas.

Herramientas y UX
Arquitectura Limpia: Separación estricta de responsabilidades usando el patrón MVVM y Clean Architecture.

Diseño Material 3: Interfaz moderna, limpia y adaptativa con Jetpack Compose.

Navegación Fluida: Sistema de rutas tipado que permite transiciones seguras entre pantallas.

Arquitectura y Tecnologías

Lenguaje: Kotlin

UI: Jetpack Compose (Declarativa y reactiva).

Inyección de Dependencias: Hilt (Dagger).

Base de Datos: Room (SQLite con soporte para Coroutines y Flow).

Red: Retrofit 2 & Gson.

Carga de Imágenes: Coil (Carga asíncrona de portadas).

Arquitectura: MVVM (Model-View-ViewModel) + Use Cases.

Asincronía: Kotlin Coroutines & Flow.

com.andriy_borukh.aplicaciongestiondelibros
├── data                # Repositorios y fuentes de datos (API/DB)
├── domain              # Modelos de negocio y Casos de Uso (UseCases)
├── di                  # Módulos de Inyección de Dependencias (Hilt)
└── ui                  # Componentes de UI (Compose)
    ├── busqueda_remoto # Pantalla de búsqueda en Google Books
    ├── lista_favoritos # Pantalla de biblioteca local
    ├── detalle_local   # Pantalla de edición/progreso de lectura
    ├── detalle_remoto  # Pantalla de vista previa de la API
    ├── components      # Componentes reutilizables (Cards, Items)
    ├── navigation      # Configuración de NavHost y rutas
    └── error           # Gestión centralizada de errores (UiError)
