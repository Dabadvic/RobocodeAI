# RobocodeAI
This AI was designed as a result of the course Artificial Intelligence took at University of Jaén in collaboration with another student, Alejandro Jiménez Pereira. It was designed to compete with a group of AIs designed by other groups of students.

## Use
The repository contains a ".jar" file with the robot already prepared. You will need the program Robocode, which you can get in the [official website](http://robocode.sourceforge.net/).

## Installation
If you want to compile it yourself, the source code is available at the Source folder. Using Robocode you can compile it following the instructions in the [official wiki](http://robowiki.net/wiki/Robocode/My_First_Robot#Compile_your_robot).

## How does it work
It uses gravity points, using the idea of [Anti-Gravity movement](http://robowiki.net/wiki/Anti-Gravity_Movement) proposed at the wiki. It assigns gravity points with different strengths, first one point for each corner evading bumping into walls, then for each enemy detected it assings a gravity point. For evading staying at the center, another point it assigns one last point the center with an random strength value.

With this movement the AI will evade the gravity points, moving swiftly between enemies.

Further information can be found in the Documentation PDF file, in Spanish language.