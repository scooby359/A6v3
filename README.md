# A6v3
A note taking app for Android with simple status marking and searching

## About
I'm building this app as part of the #100DaysOfCode project. It's designed to meet a specific user need I have, and it's giving me opportunity to practice my Android development skills.

## Features
The app allows the user to make a simple note with title, status (Good, Bad, Warning, None) and the actual note.

These are then listed by title on the main screen in alphabetical order. A search field is provided allowing the user to enter text which then filters the list. 

New notes can be created from the main screen by tapping the + symbol. If any text has been entered into the search field, this will be carried over and entered into the new note's title field.

## Tools
The app is built natively in Java with Android Studio. The Room database library is used to manage storage and queries, and autobackup is used to preserve user data in the event of the app being deleted.

## Aims
As I develop the app, I aim to include import / export features, allowing an existing data set to be imported in, and for data in the app to be exported out as JSON, XML or CSV, for use elsewhere and guarantee that user data is preserved.

I also want set up automated testing on the app. At the moment, all testing is done manually, but as part of my #100DaysOfCode work, I want to learn about automated testing and use jJUit and Espresso to implement some automated testing ont the app.

Further work may also include using an external data provider, eg Google Drive, to allow the user to log in and export data remotely.

Filtering and sorting could also be implemented on the main screen, though this isn't a priority.
