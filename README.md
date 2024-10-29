# Proyecto de Pruebas Automatizadas con Selenium, JUnit y ExtentReports

Este proyecto contiene pruebas automatizadas de inicio y cierre de sesión para el sitio de **LatamAutos**, implementadas en Java utilizando **Selenium WebDriver**, **JUnit** y **ExtentReports**. Las pruebas verifican casos de inicio de sesión exitoso, inicio de sesión fallido y cierre de sesión, y generan un reporte en formato HTML.

## Requisitos Previos

- Java
- Maven
- Navegador Google Chrome (las pruebas utilizan el controlador de Chrome)

### 1. Instalar Java
Asegúrate de tener **Java JDK** (versión 8 o superior) instalado en tu sistema. Puedes verificar la instalación ejecutando:
java -version.

### 2. Variables de entorno
Java_Home
JDK/bin

### 3. Actualizar el chromedriver si es necesario.
resources/chromedriver

## Clonar el Repositorio

Clona el repositorio:

git clone https://github.com/MiguelSCuatecontzi/latamAutos


# Importar Proyecto a Eclipse

## Importar como proyecto Maven existente

1. Abre Eclipse
2. Ve a `File > Import`
3. Selecciona `Maven > Existing Maven Projects`
4. Haz clic en `Next`
5. En `Root Directory`, busca la carpeta donde clonaste el repositorio
6. Eclipse detectará automáticamente el archivo `pom.xml`
7. Selecciona el proyecto y haz clic en `Finish`
8. Espera a que Eclipse descargue todas las dependencias Maven


## Solución de problemas comunes

1. Si el proyecto muestra errores después de importarlo:
   - Haz clic derecho en el proyecto
   - Selecciona `Maven > Update Project`
   - Marca la casilla `Force Update of Snapshots/Releases`
   - Haz clic en `OK`

2. Si hay errores de JRE:
   - Haz clic derecho en el proyecto
   - Ve a `Properties > Java Build Path`
   - En la pestaña `Libraries`, verifica que el JRE System Library sea compatible
   - Si es necesario, añade la biblioteca JRE correcta

3. Si hay errores de dependencias Maven:
   - Abre el archivo `pom.xml`
   - Verifica que todas las dependencias estén correctamente definidas
   - Ejecuta `Maven > Clean` seguido de `Maven > Install`

