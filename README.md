# Cobalto

La app de escritura del móvil. Hermana de [Atrio](https://github.com/gvegagato-prog/Atrio-Suite-2.0), [Marfil](https://github.com/Gonvegato/lectura-app)
y [Carmesí](https://github.com/gvegagato-prog/Android-Launcher): mismo sistema de temas (las mismas paletas, letras y «estilos»), otro paquete
(`com.gonzalo.cobalto`), así que **conviven en el mismo móvil** y se ven como programas de la misma suite.

Cobalto es **Escribir de Atrio, entero y con casa propia**: tus obras, y dentro de cada una el manuscrito (partes, capítulos y escenas), las
fichas, el árbol, las notas, la línea temporal, las cifras y la guía de la novela; sprints, sinónimos, papelera, borradores e historial de
versiones, comparar y mezclar, exportar (texto, Markdown, Word, PDF) e importar, y lo de la pareja (compartirle capítulos y que te los anote).
Sin migración: usa los mismos documentos que Atrio y Atrio del PC (`/users/<uid>/escritos`), con el formato de `docs/escribir.md` de Atrio.
Lo que escribas en el PC aparece aquí al momento, y al revés.

## 0.2.4

- En el manuscrito, «+ Nuevo capítulo» y «+ Nueva parte» son enlaces de la revista (como «+ Añadir» en Hiedra y Marfil), no botones.

## 0.2.3: la portada como la de sus hermanas

- Arriba, «Buscar» y «+ Nueva» como en Hiedra y Marfil (antes solo «+ Nueva obra»); «Buscar» abre las secciones con el buscador abierto.
- Las barras de escritura son las de Marfil y Azabache: la semana, una barra por día con su inicial (en el acento, los días que cumpliste
  el objetivo), y la línea de arriba en una sola línea.
- Los textos de la guía y del árbol que decían «Atrio» dicen Cobalto; lo de la pareja, «en su Cobalto (o en Atrio)».

## 0.2.2

- «Leer en Marfil» abre Marfil **leyendo esa obra** (Marfil 0.12.3 o más nueva), no solo la lista de Escritos.

## 0.2.1: Azabache y Marfil se enteran al momento

- Al publicar un capítulo terminado o las palabras del día, Cobalto **sella también `stamps`** (antes solo lo sellaba la apariencia). Azabache y
  Marfil escuchan ese sello: el Diario y los Escritos se actualizan en cuanto sincronizas, no la próxima vez que se abran.

## 0.2.0: «Leer en Marfil» y Ajustes en pestañas

- **Leer en Marfil**: en el manuscrito de una obra (y en «Exportar»), si Marfil está en el móvil y la obra tiene capítulos terminados, un enlace
  abre Marfil en sus «Escritos», donde esa obra se lee con el lector (Marfil 0.12.0 o más nueva). No se exporta nada: los capítulos llegan por tu cuenta.
- **Ajustes** en pestañas, como Atrio: Apariencia · Escritura (papelera, y qué llega a Marfil y a Azabache) · Cuenta (Atrio y pareja) ·
  Avanzado (kDrive y actualizaciones, copias).

## 0.1.0: la primera

- **Portada de revista**, con las mismas medidas que las de Atrio, Marfil y Carmesí: la cabecera, el número del día y la fecha, las barras de
  **escritura** (las palabras de hoy, tu objetivo diario y las dos últimas semanas) y el menú.
- **Ahora**: la obra en la que estás (la de la última escena que tocaste), enmarcada, con sus palabras de hoy, lo que lleva de su meta, la escena
  por la que vas, «Seguir escribiendo →» (directo al editor) y «Sprint →». Debajo, **Sobre la mesa** (las demás obras en marcha) y **En el cajón**
  (las que están en pausa). Si tu pareja te comparte capítulos, salen aquí para leerlos y anotarlos.
- **Menú**: Escribiendo · En pausa · Terminadas · Archivo, como páginas por las que te mueves, con buscador, «Importar» (Word, Markdown o texto como
  obra nueva) y «+ Nueva». El estado de una obra se cambia en sus ajustes (dentro de la obra). Cobalto lo guarda en la obra como `est`
  (`pausa`/`fin`); `arch` (archivada) ya existía en Atrio.
- **El estudio** de cada obra es el de Atrio tal cual (los archivos `Escribir*.kt` se copian sin cambios; `Compat.kt` les da las piezas de Atrio
  con las de la revista): Manuscrito, Fichas, Árbol, Notas, Línea, Cifras y Guía; la página de cada parte y de cada capítulo; el editor con su
  formato, buscar y cambiar, fichas y notas al lado, borradores e historial.
- **Para Azabache** (ver `docs/CONTRATO-DATOS.md`): al marcar un capítulo (o su parte) como terminado se publica una copia de solo lectura de
  su texto en `/users/<uid>/cobalto/lectura/<obra>/<capítulo>`, y las palabras escritas cada día en este dispositivo en
  `/users/<uid>/cobalto/palabras/<dispositivo>`. Se publica tras cada sincronización.
- **Cuenta de Atrio** (la misma) y **pareja** (su usuario de Atrio, para compartir y leer lo suyo); **apariencia** como la de Atrio, Marfil y
  Carmesí (temas, fondos, acento, letras, colores de Atrio en directo o copiados de tu cuenta), y viaja entre tus Cobaltos.
- **Se actualiza sola** desde la carpeta «versiones» de tu kDrive (`Cobalto-<versión>.apk`), como las demás.
- **Widgets**: *Palabras de hoy* (las de hoy, tu racha y dos semanas en barras con tu objetivo), *Sigue escribiendo* (la obra en la que estás,
  con «Seguir» y «Sprint»; al ponerlo eliges si abre la que toque o una fija; tocando la esquina pasa a otra), *Una frase tuya* (un párrafo de tu
  manuscrito, uno cada día, que abre esa escena), *COBALTO* y *COBALTO en papel*.
- **Icono**: el átomo sobre azul cobalto, adaptable (`herramientas/icono-cobalto.py`).

## Capturas

Generadas en el ordenador con obras de ejemplo (`CapturasTest`, `-Proborazzi.test.record=true`): [portada](docs/capturas/1_portada.png) ·
[portada abajo](docs/capturas/1b_portada_abajo.png) · [en oscuro](docs/capturas/2_portada_oscuro.png) · [Escribiendo](docs/capturas/3_escribiendo.png) ·
[En pausa](docs/capturas/3b_en_pausa.png) · [Archivo](docs/capturas/3c_archivo.png) · [manuscrito](docs/capturas/4_obra_manuscrito.png) ·
[fichas](docs/capturas/4b_obra_fichas.png) · [notas](docs/capturas/4c_obra_notas.png) · [editor](docs/capturas/4d_editor.png) ·
[ajustes](docs/capturas/5_ajustes.png) · [ajustes: escritura](docs/capturas/5b_ajustes_escritura.png) · [ajustes: cuenta](docs/capturas/5c_ajustes_cuenta.png) · [ajustes: avanzado](docs/capturas/5d_ajustes_avanzado.png) · [vacía](docs/capturas/6_portada_vacia.png). Los widgets, en `docs/widgets/`.

## Cómo se trabaja

- Pruebas: `gradle :app:testDebugUnitTest` (0 fallos). `ContratoTest` deja en `app/build/outputs/contrato/cobalto.json` lo que Cobalto publica
  tal cual; se copia a `azabache/herramientas/casos/hermanas/` y la prueba `ContratoHermanasTest` de Azabache comprueba que lo lee entero. Con `-Proborazzi.test.record=true` se graban las capturas (`app/build/outputs/roborazzi/`)
  y los widgets (`app/build/outputs/widgets/`, de donde salen las vistas previas `w_prev_*.png`).
- Los casos compartidos con el PC (`herramientas/casos/*.json`: mezcla, estructura, tipografía, sinónimos) son copia de los de Atrio y los pasan
  `EscribirMezclaTest`, `EscribirEstructuraTest` y `EscribirCasosTest`.
- Versión nueva: subir `versionCode`/`versionName` (`app/build.gradle.kts`) y `VERSION` (`PantEstilo.kt`), `gradle :app:assembleRelease`, y
  `python3 herramientas/subir-version.py Cobalto-<versión>.apk` la deja en tu kDrive para que los móviles se actualicen solos.
- Para traer cambios de Escribir de Atrio: copiar `Escritos.kt` y `Escribir*.kt` de `Atrio-Suite-2.0/movil/...` cambiando el paquete, y volver
  a aplicar los pocos cambios de aquí (`Store`/`Account` → `Cuenta`, `CloudSave` → `NubeTarea`, el estado de la obra en `ObraDialogo`).
