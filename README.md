# 📚 Aplicación Gestión de Libros

Aplicación Android moderna para los amantes de la lectura. Permite **buscar libros mediante la API de Google Books**, gestionar una biblioteca personal almacenada localmente y realizar un seguimiento detallado del progreso de lectura.

La aplicación combina datos remotos y almacenamiento local para ofrecer una experiencia completa, incluso cuando no hay conexión a Internet.

---

## ✨ Características principales

### 🔎 Búsqueda de libros

La aplicación permite buscar libros utilizando la **API de Google Books**.

* Búsqueda por título o autor.
* Consulta de información en tiempo real.
* Visualización de portadas y sinopsis.
* Gestión de estados de carga.
* Gestión de errores de conexión y respuestas HTTP.
* Control de búsquedas sin resultados.
* Vista detallada antes de añadir un libro a la biblioteca.

### 📖 Biblioteca personal

Los libros guardados se almacenan localmente utilizando **Room**, permitiendo acceder a la biblioteca sin necesidad de conexión a Internet.

* Guardado de libros favoritos.
* Consulta de la biblioteca local.
* Búsqueda por título o autor.
* Acceso a la información de cada libro.
* Edición del progreso de lectura.

### 📊 Seguimiento de lectura

Cada libro de la biblioteca puede utilizarse para realizar un seguimiento personalizado de la lectura.

* 📄 Registro de páginas leídas.
* ⭐ Valoración mediante estrellas.
* 📝 Añadir notas y comentarios personales.
* 📅 Registro de la fecha de inicio.
* 📅 Registro de la fecha de finalización.
* ✏️ Edición del progreso de lectura.

---

## 🏗️ Arquitectura

El proyecto utiliza una arquitectura basada en **MVVM + Clean Architecture**, separando las responsabilidades de cada parte de la aplicación.

```text
com.andriy_borukh.aplicaciongestiondelibros
│
├── data
│   ├── API
│   ├── database
│   └── repository
│
├── domain
│   ├── models
│   ├── repository
│   └── usecases
│
├── di
│   └── módulos de inyección de dependencias
│
└── ui
    ├── busqueda_remoto
    ├── lista_favoritos
    ├── detalle_local
    ├── detalle_remoto
    ├── components
    ├── navigation
    └── error
```

### 📂 Capas principales

**Data**

Se encarga de la obtención y almacenamiento de los datos, incluyendo la comunicación con la API de Google Books y la base de datos local.

**Domain**

Contiene la lógica de negocio de la aplicación, los modelos y los casos de uso.

**UI**

Contiene las pantallas y componentes desarrollados con Jetpack Compose, además de la navegación y la gestión de los estados de la interfaz.

**DI**

Contiene la configuración de la inyección de dependencias mediante Hilt.

---

## 🛠️ Tecnologías utilizadas

| Tecnología           | Uso                         |
| -------------------- | --------------------------- |
| **Kotlin**           | Lenguaje principal          |
| **Jetpack Compose**  | Interfaz de usuario         |
| **Material 3**       | Diseño de la aplicación     |
| **Hilt / Dagger**    | Inyección de dependencias   |
| **Room**             | Base de datos local         |
| **SQLite**           | Almacenamiento local        |
| **Retrofit 2**       | Comunicación con la API     |
| **Gson**             | Conversión de datos JSON    |
| **Coil**             | Carga asíncrona de imágenes |
| **Coroutines**       | Programación asíncrona      |
| **Flow**             | Flujo reactivo de datos     |
| **Google Books API** | Búsqueda de libros          |

---

## 🌐 Google Books API

La aplicación utiliza la **Google Books API** para realizar búsquedas y obtener información sobre los libros.

Los datos obtenidos desde la API permiten mostrar información como:

* Título.
* Autor.
* Portada.
* Sinopsis.
* Información bibliográfica disponible.

Los resultados obtenidos pueden visualizarse antes de decidir si se añade el libro a la biblioteca personal.

---

## 💾 Almacenamiento local

Los libros guardados en la biblioteca se almacenan mediante **Room**, utilizando SQLite como sistema de almacenamiento local.

Esto permite que la información de la biblioteca esté disponible incluso cuando el dispositivo no tiene conexión a Internet.

Además de la información del libro, se almacena la información relacionada con el seguimiento de la lectura, como:

* Página actual.
* Valoración.
* Notas personales.
* Fecha de inicio.
* Fecha de finalización.

---

## 🧭 Navegación

La aplicación utiliza un sistema de navegación basado en rutas tipadas para controlar las diferentes pantallas.

Las principales secciones son:

```text
Búsqueda remota
      │
      ├── Detalle remoto
      │       │
      │       └── Añadir a biblioteca
      │
      └── Biblioteca
              │
              └── Detalle local
                      │
                      └── Editar progreso
```

### Pantallas principales

#### 🔍 Búsqueda remota

Permite buscar libros utilizando Google Books API y consultar los resultados obtenidos.

#### 📖 Biblioteca

Muestra los libros guardados localmente y permite filtrarlos mediante el título o autor.

#### 📕 Detalle remoto

Muestra la información disponible de un libro obtenido desde la API antes de guardarlo en la biblioteca.

#### 📝 Detalle local

Permite consultar y modificar la información relacionada con el progreso de lectura de un libro.

---

## ⚠️ Gestión de errores

La aplicación incorpora una gestión centralizada de errores mediante `UiError`.

Se contemplan diferentes situaciones, entre ellas:

* Errores de conexión.
* Errores de la API.
* Respuestas HTTP como `429` o `503`.
* Búsquedas sin resultados.
* Errores durante la carga de información.

De esta forma, los errores se gestionan de manera controlada y se pueden mostrar al usuario mediante la interfaz.

---

## 🎨 Interfaz de usuario

La interfaz está desarrollada utilizando **Jetpack Compose** y **Material 3**.

El uso de Compose permite crear una interfaz:

* Declarativa.
* Reactiva.
* Adaptativa.
* Basada en componentes reutilizables.

También se utilizan componentes propios para evitar repetir elementos de la interfaz.

---

## 🚀 Instalación

### Requisitos

* Android Studio.
* JDK compatible con el proyecto.
* Android SDK.
* Conexión a Internet para realizar búsquedas mediante Google Books API.

### Clonar el repositorio

```bash
git clone https://github.com/Andriy-Borukh/aplicaciongestiondelibros.git
```

Después, abre el proyecto con **Android Studio**, sincroniza las dependencias de Gradle y ejecuta la aplicación en un dispositivo físico o emulador.

---

## 📱 Flujo de uso

El funcionamiento principal de la aplicación sigue el siguiente flujo:

```text
1. Buscar un libro
       ↓
2. Consultar los resultados
       ↓
3. Ver los detalles del libro
       ↓
4. Añadirlo a la biblioteca
       ↓
5. Consultarlo desde la biblioteca
       ↓
6. Registrar el progreso de lectura
       ↓
7. Añadir valoración y notas
       ↓
8. Registrar las fechas de lectura
```

---

## 👨‍💻 Autor

**Andriy Borukh**

Estudiante de Desarrollo de Aplicaciones Web y Multiplataforma.

[GitHub](https://github.com/Andriy-Borukh)

---

## 📄 Licencia

Este proyecto ha sido desarrollado con fines educativos.
