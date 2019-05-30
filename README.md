# ShootingGame

A simple 2D top-down multiplayer shooting game written in JavaFX.

## Installation

### Running the Jar

Requires a working java install with JavaFX.

Provided in the directory is a .jar file. Double-click on the jar to run the game. Alternatively, open in terminal with 

```sh
java -jar ShootingGame.jar
```

### Compiling from scratch

In case the jar file doesn't work, you can try to compile the game manually.

Open a terminal in the top-level directory of the project (Same directory as this README).

---
#### Windows
```sh
dir /s /B *.java > sources.txt
javac @sources.txt
```
#### Mac/Linux
```sh
find -name "*.java" > sources.txt
javac @sources.txt
```
---

Afterward the sources compile, run the GameLauncher with 

```sh
java GameLauncher
```

## Playing the Game

Inside the GameLauncher, you can either start a server using the panel on the right, or you can connect to an existing one using the panel on the left.

### Creating a server

Type in a port (any number > 1024; I usually use 4444), then click "Start Server". 
The internal IP address of the server is shown in the text below the button. Save this number to connect to the server.
Note: Make sure to port-forward if you plan on playing over the internet. The game only uses UDP for its connections.

### Connecting to a server

On the left panel, you can connect to a server by typing in the IP address, port, and a username. 

If you are playing alone on the same PC, simply use "localhost" as the IP address.

## Loading maps

The game can load maps by parsing through a CSV. While the server is running, simply click "Load Map File" and select a map file. TestMap.csv is provided in the same directory. 

### Map file format

The game parses through a csv file, reading the "w" character as a wall. For example, the following:
```
w,w,w,w,w
 , ,w, ,
 , ,w, ,
w,w,w,w,w
```
creates a map that looks like the letter I. Feel free to load your own .csv files to create your own maps.

## Powerups

At the moment the game has 2 powerups. "HP" increases your HP. "Shot++" temporarily increases your firerate.
