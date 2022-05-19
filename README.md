# Technical test

Crear una applicacion movil Android con minimo las siguientes condiciones:

La app debería contar con tres pantallas:
1. Campo de búsqueda.
2. Visualización de resultados de la búsqueda.
3. Detalle de un producto.
Puedes entregar un listado y detalle de productos que sea puro texto, o un buscador con
imágenes, iconos y texto, y un detalle completo del producto, como el que se muestra en la
web.


## Architecture

Se uso MVVM como patron de diseño impementado tambien Clean Arquitecture para dividir las responsabilidades de la app en diferentes capas.
Esto facilita su testeabilidad y escalabilidad a lo largo del tiempo.

## Features

- Pantalla con campo de busqueda
- Pantalla donde muestra los resultados de la busqueda, incluye paginación
- Pantalla con una descripción mas detallada de la busqueda

## Libraries

Para este proyecto usé las siguientes tecnologias:

- [Retrofit] : <https://square.github.io/retrofit>
- [Dagger Hilt] : <https://dagger.dev/hilt/gradle-setup.html>
- [ViewModel] : <https://developer.android.com/topic/libraries/architecture/viewmodell>
- [Glide] : <https://github.com/bumptech/glide>
- [Mockk] : <https://github.com/mockk/mockk>
- [Picasso] : <https://github.com/square/picasso>