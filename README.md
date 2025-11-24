# RecipeApp – Kotlin Multiplatform
Aplicación móvil desarrollada con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform**, diseñada para generar recetas personalizadas basadas en los ingredientes que proporcione el usuario. Funciona tanto en **Android** como en **iOS** con una sola base de código.

## Descripción
RecipeApp permite a los usuarios ingresar uno o varios ingredientes y obtener recetas sugeridas que pueden prepararse con esos insumos. La app se conecta a una API que procesa los ingredientes y devuelve una lista de recetas con nombre, pasos e información relevante.

## Tecnologías utilizadas
### Multiplataforma
- Kotlin Multiplatform (KMP)
- Compose Multiplatform
- Ktorfit (cliente API)
- Kotlin Serialization
- ViewModel + StateFlow (Multiplataforma)

### Android
- Compose UI
- Activity Compose

### iOS
- Compose Multiplatform para iOS (UIKit embedding)

## Arquitectura
- Data Layer (Ktorfit, modelos)
- Domain Layer (casos de uso)
- UI Layer (Compose, StateFlow)

## Funcionalidades principales
- Ingreso de ingredientes
- Obtención de recetas desde la API
- Pantalla de resultados
- Modo claro/oscuro
- Tema unificado

## Estructura del proyecto
```
composeApp/
 ├── commonMain/
 │    ├── data/
 │    ├── domain/
 │    ├── ui/
 ├── androidMain/
 └── iosMain/
```

## API
```
POST /api/recipes
{
  "ingredients": ["tomate", "pan", "queso"]
}
```

Respuesta:
```json
{
  "recipes": [
    {
      "title": "Sandwich de queso y tomate",
      "ingredients": ["pan", "tomate", "queso"],
      "steps": ["Corta...", "Mezcla...", "Sirve..."]
    }
  ]
}
```

## Ejecución
### Android
```
Run → Android App
```

### iOS
```
./gradlew iosApp:podInstall
Open iosApp.xcworkspace
```

## Próximas mejoras
- Búsqueda por nombre
- Favoritos
- Filtros por categoría
- Integración con cámara

## Contribuir
1. Fork
2. Rama nueva
3. Pull Request

## Autor
GitHub: https://github.com/angx7
