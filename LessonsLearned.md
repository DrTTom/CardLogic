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

VUEJS und update von gebundenen Elementen
=========================================

Wenn z.B. eine Checkbox an eine Variable von Vue gebunden ist und sich die Variable ändert, erfolgt manchmal kein Update. 
VUE triggert den Update nicht, wenn es sich bei der Variable um ein Attribut in einem Attribut handelt. Anhilfe: irgendein _direktes_ Attribut der VUE Komponente ändern 

CSS Frameworks: z.B. Blaze CSS
==============================

CSS Frameworks versprechen, eine Web-Anwendung mit minimalem Aufwand mehr oder weniger gut aussehen zu lassen, einfach durch Einbinden eines fertigen Stylesheets. Dass dabei natürlich keinerlei speziellen Anforderungen oder gar Corporate Design berücksichtigt werden können, versteht sich von selbst. Stattdessen werden Grundaufgaben angegangen:

- Mehrspaltige Ansichten, die auf &lt;table&gt; verzichten
- Styling von allerlei Formlular-Elementen (Buttons, Tooltips, ...), oft mitgelieferte Icons
- Vorgefertigte Ordnungselemente wie Tabs, Drop-Down-Menues, Akkordions, modale Dialoge usw. oft mit durchaus eindrucksvollen Funktionen und Animationen.

Es gibt eine große Anzahl mehr oder weniger ausgereifter CSS-Frameworks mit hoher Fluktuation. Blaze hat gegenüber einigen anderen den Vorteil, nur eigene Stil-Klassen zu definieren und keine Standards mal eben umzudefinieren. Die Benutzung ist einfach: auf der Beschreibungsseite http://blazecss.com/ eine Formatierung aussuchen und die Klassen an die Elemente auf der eigenen Seite schrieben. 
Leider passt vieles eben doch nicht so ganz und manches (wie die modalen Dialoge, neben denen man eben doch die Element im Hintergrund klicken kann) bedarf auch kleinerer Reparaturen. 

_Fazit:_

- Ein CSS-Framework kann niemals mit einem professionell für eine Anwendung designten CSS konkurrieren.
- Die Dinger sind aber extrem nützlich, wenn man selbst ein CSS schreibt und kein CSS-Guru ist.  
