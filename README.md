# Backend of PetPocket
(updated on Aug 29 2022)
Important documentation for the backend of PetPocket: 
* [API Documentation](https://petsapidoc.blob.core.windows.net/$web/Pets-tracking-app-1-1-2.html)
* [Database Diagram](https://drive.google.com/file/d/1SKr-YTJUhAyQewe16b6HvqFJvcW7C1Lz/view?usp=sharing)

Below is an instruction of *how to set up the development environment* on your own devices: \
 \
``DOCKER COMING SOON...``

### Prerequisites
#### Java
Check your JDK version by running ```java version``` in a terminal window.\
If you do not have JDK installed, download from https://www.oracle.com/java/technologies/downloads/#java17.
#### IntelliJ IDEA
You can download *IntelliJ IDEA Ultimate* from https://www.jetbrains.com/idea/download \
(You may want to apply for a Free Educational License at https://www.jetbrains.com/community/education/#students by validating your ANU student email address).
#### Git
Check your Git version by running ```git version``` in a terminal window.\
If you do not have Git installed, download from https://git-scm.com/downloads.
### Installation
#### Clone the Git repository
Clone the repository by running ```git clone https://github.com/BikiniKang/pets-backend.git``` in a terminal window and open it with IntelliJ IDEA. 
#### Connect to Database
If you would like to view or modify the database of the project, open the Database tool window (View | Tool Windows | Database) in IntelliJ IDEA and find the *Data Source Properties* icon, add a data source with following information:
```
Host:     pets-server.mysql.database.azure.com
Port:     3306
User:     petsteam
Password: **** (please ask the Backend Manager to access)
Database: appdb
URL:      jdbc:mysql://pets-server.mysql.database.azure.com:3306/appdb?sslmode=required
```
