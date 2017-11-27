Code to automatically find QR codes in photos.
Useful for automatic assignment of photos to inventory when using QR codes to identify the subjects.
Assumes a single QR code per image.

   sbt qrIdent/assembly

to create a new jar.

   java -jar target/scala-2.11/qrIdent-assembly-0.1-SNAPSHOT.jar file.jpg

writes out the the content of the QR code.