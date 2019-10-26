# webChat
Aplicacion chat con websocket

## Prerequisitos
* Tener instalado JDK 1.8 o superior.     (https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Tener instalado maven 3.6.2 o superior. (https://maven.apache.org/)

## Inicializar el proyecto webChat

** Clonar el código fuente en una carpeta en tu maquina.
```
git clone 
```
* Abrir una terminal, posicionarse en la carpeta del proyecto y ejecutar el siguiente comando.
```
mvn clean
```
Y luego 
```
mvn package
```
* Una vez ejecutado mvn package dirigirse en la carpeta del proyecto y buscar la carpeta target, aquí se encontrará un jar con nombre 
    webChat.jar
* Ejecutar desde la terminal (Posicionandose en la carpeta que contiene el jar) el siguiente comando
```
java jar- webChat-1-0.jar
```

* Abrir un navegador e ingresar la url  
```
http://localhost:9191/index.html
```
* En el login se pueden ocupar cualquiera de los siguientes participantes.

```
Usuarios: Juan, Ana, Admin, Invitado
Contraseña: 123
```
 
 
