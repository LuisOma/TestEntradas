# TestEntradas
Proyecto de demostración de Entradas, basado en una arquitectura CLEAN con MVVM.

## Funciones de la aplicación

- Los usuarios pueden crear, buscar y ver entradas
- Los usuarios pueden hacer clic en cualquier entrada para ver los detalles de la misma.

## Arquitectura de la aplicación
Basado en la arquitectura Clean y el patrón MVVM.

## La aplicación incluye los siguientes componentes principales:
- Un DataSource que trabaja con la BD, proporcionando una interfaz de datos unificada.
- Un ViewModel que proporciona datos específicos para la interfaz de usuario.
- La interfaz de usuario, que muestra una representación visual de los datos en ViewModel.

## Paquetes de aplicaciones
- constants.
- core.
- data.
- di.
- domain.
- ui.
- utils.

## Especificaciones de la aplicación
- SDK mínimo 26
- Java (en la rama maestra) y Kotlin (en la rama kotlin_support)
- Arquitectura MVVM
- Componentes de la arquitectura de Android (LiveData, ViewModel, Material Design).
- **ViewModel** para pasar datos del modelo a las vistas
- **LiveData**
- **Hilt** para inyección de dependencias.
- **Flow**
- **Room** para persistencia de datos.
