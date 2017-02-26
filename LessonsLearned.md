REST Service mit Spark
======================

Was man wirklich braucht, um einen REST Service zu schreiben ist: "Wenn die Adresse X aufgerufen wird, soll der Server ein Ergebnis Y liefern, das die Methode Z aus Parametern und Pfad berechnet".

Genau das bietet Spark. Die Syntax ist selbsterklärend:

``get("/example/:name", (req, resp) -> "Hello "+ req.params(":name"));``

d.h. zu einem Pfad und einer Zugriffsart (get, put, ...) gibt man direkt eine Methode an, die das Ergebnis liefert, z.B. als Lambda.
Einfacher geht es wohl kaum noch. 

Das Paket wird eingebunden als Dependency "com.sparkjava:spark-core:2.5.4" in Gradle oder etwas umständlicher in Maven
und ist mit 145 KB die Jar bzw. etwas über 2MB einschließlich Jetty-Server (geht auch mit anderen Servern) recht klein.
Es ist sorgfältig dokumentiert unter [http://sparkjava.com/documentation.html] und kommt auch mit Sonderlocken wie Dateiupload 
klar.
