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

Für größere Projekte ist ggf. javalin besser, jedoch für die vorliegende Anwendung bietet es keine Vorteile. 

VUEJS 
======

- Von den vielen JS-Frameworks eher eins der kleineren und weniger invasiven. Nach inkompatiblem API-Änderungen bei Versionswechsel 
  wurde das Framework aus der Anwendung entfernt. Der Client benötigt ohne VUE weniger Quelltextzeilen als mit bei gleicher
  Funktionalität. 


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
- Die Dinger sind aber extrem nützlich, wenn man selbst ein CSS schreibt und kein CSS-Guru ist. Allein zu erfahren, was 
  alles rein mit CSS zu machen ist, lohnt die Beschäftigung.
- wurde aus Anwendung entfernt.  

Außerdem hat sich in diesem Zusammenhang die Webseite https://jsfiddle.net/ als recht nützlich erwiesen. Dort kann man das Zusamenspiel von CSS, HTML und JS-Schnipselns schnell und einfach ausprobieren.

Custom Components
=================

Ermöglichen eine einfache, übersichtliche Implementierung der Oberfläche bei Minimierung der Nutzung notwendiger Fremdbibliotheken.
Für die aktuelle Anwendung ist der Verzicht auf VueJS ein erheblicher Gewinn. 

Achtung: Custom Components sind zwar Klassen, aber die Instanzen sind keine Objekte wie in Java, sondern 
lediglich Sichten auf das DOM-Element. Attribute müssen also zwingend im DOM abgelegt werden, da das JS-Objekt 
ggf. aus dem DOM-Element rekonstruiert wird.  

PDF-Erzeugung
==============

Geeignet sind verschiedene Libs, allerdings ist der programmatische Aufbau eines auch einfachen Dokuments so komplex, dass 
die Umsetzung zu teuer wird. Für "document from scratch" ist lediglich itextpdf geiegnet, das scheidet aber aus Lizenzgründen aus.
Anständige Druckausgaben sollten durch ein Textverbeitungssystem oder ein Textsatzsystem erzeugt werden.

_Fazit:_

Erzeuge keine PDFs selbst, sondern stattdessen docx. Die Vorlage kann mit LibreOffice beliebig elegant erstellt werden, die 
Daten fügt dann EasyData ein. Vorteil ist, dass der Nutzer die Dokumente vor Druck ggf. noch bearbeiten kann.   
