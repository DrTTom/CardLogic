# CardLogic

To manage a collection of stamps, of books or of any kind of "collectible"
objects you will find many software solutions. Collecting stuff which comes
without catalouges, labels or dating is an entirely different world.

This project is for two purposes:

* getting more familiar with some JS techniques, provide examples for teaching
* helping me organize my collection of playing cards

Its organized as JavaScript front end connecting via REST to a Java back end.
Both parts consist of a generic base which could be used for any kind of
collection and an extension which makes it suitable for handling the special
needs of playing card collectors.

Why is collecting playing cards different?
* There is no exhaustive catalouge or reference.
* There are rather few collectors.
* Cards may or may not have maker signs, covers ...

## Usage
The project is not finished, but the application is usable to keep track of my playing cards collection.
For a quick view, call ``gradlew run``, then navigate to "Import/Export" -> "Hochladen" -> "Gesamte Sammlung" 
and upload the file ``src/stest/resources/example.zip`` from this project. This loads some data you can browse or manipulate.
 
