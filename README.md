## Configuración del proyecto con IDE IntelliJ

1. [ ] Importar proyecto a través de archivo **build.gradle** en IntelliJ
2. [ ] Seleccionar en IntelliJ Menu principal **Run -> Edit Configurations**
3. [ ] Presionar signo **"+"** para agregar nueva configuración **"add new configuration"**
4. [ ] Seleccionar **"Application"**
5. [ ] Agregar en name **"users"**
6. [ ] Seleccionar la **versión de Java** (creado con versión Java 11 OpenJDK)
7. [ ] Seleccionar modulo **"users.main"**
8. [ ] Seleccionar la clase principal **"UsersApplication"**
9. [ ] Descargar las dependencias desde el archivo **build.gradle**


##### Lombok in IntelliJ

1.[ ] Añadir lombok plugin.
2.[ ] Habilitar la opción  "annotation processes" en preferencias.



## Arquitectura del proyecto

El proyecto está compuesto por una arquitectura MVC, 
la cual nos va a permitir tener separadas las responsabilidades así como la reutilizacion de código.

Actualmente está usando H2, pero se puede configurar y utilizar distintas base de datos, con Spring Data para el mapeo entre objeto entidad.

Para correr esta APP, se levanta como una aplicación Java como se define al comienzo del **README.md**.

## Flyway 

El proyecto cuenta con una implementación básica de Flyway lo que permite versionar la base de datos, en la ruta src/main/resources/db.migration se encuentran los archivos SQL.

## Postman y Swagger

El proyecto cuenta con una colección ***POSTMAN*** en la ruta public/ con el cual se puede realizar una prueba de los 2 Endpoint.

El proyecto además cuenta con el respecto archivo **openapi.yaml** el cual cuenta con la documentación de los 2 Endpoint.

## Testing

La carpeta que contiene los test unitarios es: `users/src/test`

La APP tiene el 80% de cobertura.

Para revisar los reportes generados se debe ir a **build/reports/**.  Esta carpeta se genera cuando se compila
o construye el proyecto.



## Gradle

En el IDE IntelliJ se puede revisar en la pestaña Gradle


## Añade tus archivos

- [ ] [Añadir archivos usando linea de comando](https://docs.github.com/en/repositories/working-with-files/managing-files/adding-a-file-to-a-repository) o agregalos a un repositorio Git que ya existe:

```
cd carpeta_repositorio
git remote add origin https://github.com/NoSyntaxErrors/users.git
git branch -M main
git push -uf origin main
```

