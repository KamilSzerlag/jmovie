# cobrick-recruitment-task-java

Recruitmen task for Junior Java Developer position.

##Zadanie rekrutacyjne co.brick
Aplikacja stworzona w oparciu o framework <b>Spring boot</b>. Jako bazę danych użyto <b>MongoDB</b>.

Aplikacja może zostać uruchomiona poprzez użycie komendy w katalogu zawięrającym aplikację.
<code>mvn spring-boot:run</code>

Przed wykonaniem polecenia należy upewnić się, że serwer bazodanowy mongoDB jest uruchomiony (polecenie <code>systemclt mongod status</code>) oraz czy w pliku konfiguracyjnym aplikacji <code>application.properties</code> w parametrze <code>application.mongodb.host</code> mamy prawidłowo zdefiniowane połączenie do bazy danych.

Domyślne parametr połączenia zdefiniowany jest jako:
<code>application.mongodb.host=<b>mongodb://localhost</b></code>
Domyślny port mongoDB to: <code>27017</code>

Przed uruchomieniem aplikacji warto także zdefiniować miejsce zapisu przesłanych plików:
<code>application.upload.folder=<b>/home/Desktop/</b></code>

Domyślnie server tomcat jest dostępny pod adresem <code>localhost: 8080</code>
