# El Templo Barbería - Sistema de Gestión de Turnos

## Descripción
Sistema web para la gestión de turnos de "El Templo Barbería". Permite a los clientes agendar turnos online y a los administradores gestionar las reservas.

## Características
- Agendamiento de turnos online
- Panel de administración para gestionar turnos
- Confirmación, cancelación y marcado de turnos como completados
- Diseño responsive con Tailwind CSS
- Interfaz moderna con tipografía Montserrat

## Tecnologías
- Java 17
- Spring Boot 2.7.8
- Thymeleaf para plantillas
- PostgreSQL (base de datos)
- Tailwind CSS (frontend)
- Maven (gestión de dependencias)

## Configuración del Proyecto
El proyecto está configurado para ejecutarse en el puerto 8080 por defecto. Las principales configuraciones se encuentran en:
- `src/main/resources/application.properties`: Configuración de Spring Boot, base de datos y servidor.

## Despliegue Automatizado
El proyecto utiliza GitHub Actions para automatizar el proceso de despliegue a un servidor EC2:

1. **Workflow de CI/CD**: `.github/workflows/deploy.yml`
   - Se activa con cada push a las ramas `main` o `master`
   - Compila la aplicación con Maven
   - Copia el archivo JAR generado al servidor EC2
   - Reinicia automáticamente la aplicación en el servidor

2. **Requisitos para el despliegue**:
   - Servidor EC2 configurado con Java 17
   - Secretos configurados en GitHub:
     - `EC2_HOST`: Dirección IP o hostname del servidor EC2
     - `EC2_KEY`: Clave SSH privada para acceder al servidor

3. **Estructura en el servidor**:
   - El JAR se despliega en: `/home/ec2-user/el_templo/target/`
   - Los logs se almacenan en: `/home/ec2-user/el_templo/logs/output.log`

## Desarrollo Local
Para ejecutar el proyecto localmente:

```bash
# Clonar el repositorio
git clone [URL del repositorio]

# Navegar al directorio del proyecto
cd el_templo

# Compilar el proyecto
mvn clean package

# Ejecutar la aplicación
java -jar target/[nombre-del-jar].jar
```

## Acceso
- **Página principal (Agenda)**: `/`
- **Panel de Administración**: `/admin`
