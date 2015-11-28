Software : OboInNeo4j
Main Author : Thierry Barthel thierry.barthel@inria.fr
Second Partial-Author : Nicolas Paris
Version : 0.1
Release date : 04/08/2015


			****************************
			*    Basic informational requirements   *
			****************************
			
	Use motherfucking java 1.8 and launch it with required arguments and internet
	connection (see -help)

			****************************
			*   What is QuestioNeo4j ?   *
			****************************

	This software is used to generate Json horse race informations


			****************************
			*    How does it work ?    *
			****************************
	Jsoup and some regexp


			****************************
			*       Requirements       *
			****************************

	- java 1.8
	- maven
	- internet connection


			****************************
			*   	   Install	   *
			
			****************************
			
	Use pom.xml to install (create) the jar (see Maven documentation). 
	     On Eclipse : install maven plugin, then right click on pom.xml chose "Maven install"
	
	2 jar will be installed in the target dir : 
		- the one without dependencies
		- the one with dependencies (the functional one, with external libraries)
			Check the help or read the "Usage" paragraph to know how to properly launch it
	
	
				****************************
			*   	   Usage	   *
			
			****************************
	Command line : (sudo) java -jar Dadai-*.*.*-jar-with-dependencies [args]
	*.*.*= depends on version (see pom.xml), let's call it "QuestioNeo4j.jar" for the rest
	of the paragraph
	(sudo) since the files read (graph.db) often times have specific rights/owners
	
	Arguments :
	-h (-help)             Usage
	-dtend   <arg>		   Date beginning extraction (included)
	-dtstart  <arg>   	   Date end extraction (included
	-o <arg>               Path directory where you want to generate the output JSON files.
 	

			****************************
			*   	  warnings 	   *
			****************************

