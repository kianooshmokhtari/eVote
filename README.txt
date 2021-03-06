# Term Project
### Team 9: Joey Bruce, Jack Fisher, Kianoosh Mokhtari, Will Runge
### Written for Dr. Krzysztof Kochut's CSCI 4050 class, fall 2016

**THIS PROJECT MUST BE RUN ON `uml`. RUNNING IT ON ANOTHER SYSTEM WILL PRODUCE
ERRORS.**


## Running the servlet

The project includes an Apache Ant xml script for easy compilation and
deployment of the eVote servlet. The script defines four directives:

	- `ant clean` -- removes any files generated by other Ant commands
	- `ant compile` -- compiles all *.java files
	- `ant build` -- creates a WAR file containing the necessary classes,
				libraries, HTML files, and Freemarker templates
	- `ant deploy` -- copies the WAR file to the appropriate directory on `uml`

Each Ant directive relies upon the one previous, e.g. `ant build` will
automatically clean and recompile before building the WAR.

Deploying the project will make it accessable at
`uml.cs.uga.edu:8080/team9_evote/index.html`. A copy of the servlet is already
up and running at that location.


## Testing the object and persistence layers

The project includes two useful scripts for compiling and testing the object and
persistence layers:

	- `compile.sh` -- compiles all *.java files and links them with the
				included *.jar library files.
	- `evote-test.sh` -- runs the `EvoteTester` class, which performs tests as
				prescribed in the part 6 instructions.

To run either of these scripts, navigate to the top project folder (`evote`)
and execute the command `sh <script_name>` where `<script_name>`.

When `EvoteTester` is run, it first displays the contents of the database, to
demonstrate that the database is empty. It will then add data, update data to
simulate voting, and delete all data, displaying the contents of the database
after each step. Each test and read of the database is a separate session.
