# Mapper


## What is this?

The purpose of the mapper is to serve as an example of how to implement our graph project to index artifact metadata and dependency data.

## Official Project Documentation 
Please see our [Hitchhiker's Guide to The Indexa.](../docs/index.md) 

This is where one can find the project design, contribution guide and other detailed information.

## To Execute :clapper:

To start using this project, you first need to clone this repo.

After cloning the repo, navigate to the directory containing the project.

`cd indexa-mapper`

`./gradlew clean build run -PinputDir=c:/hub` (or \users\YOURUSERNAME\hub if on a non-windows OS)  where 'hub' is the directory containing your java archives. 

If you would like to see how the project runs on sample data, an example hub directory can be found in the src/test/resources/hub portion of this project. 



The project will run and begin indexing the directory given and will output the indices to a SmallIndexes folder that will be on C:/ for Windows users and the project root directory for non-Windows users(the command  `ls -a` would be required to see these indexes)

## How it Works (Simplistic View) :bulb:

The graph project that this project references is the foundation for gathering data about your data.
		
This mapper will use the methods provided in the graph project as a base to get all the information required. When given a file directory, the mapper will traverse that directory looking for java archives.  A java archive is merely a zip file that is specific to java(jar files, ear files, war files). Upon finding one of these archives, the archive's information will be added to a database(Lucene index in this case) to reference later; a list of the contents will be made.
		
Next, each item in this list is examined to determine what dependencies a specific item needs in order to function properly. Each item and the corresponding dependency will be added to the database connecting the two to the archive in which they were found. After the specified directory has been fully traversed, the database will be populated with all archive information, items inside that archives' information, and the dependencies needed by that item in order to function.
		
		
		
Using the database created by the mapping process explained above, dependency resolution will be completed. Resolution in this context, means to use all the data provided to find a specific archive where the dependency resides.

:heavy_check_mark: For every archive in the created database, a list of dependencies are also now available in the database, after the initial population steps are completed as detailed above.
		
The resolution process will go through all items, looking for items that match what is needed for each archive. Once found, the dependency's archive is added to a database and noted as being needed by the dependent archive. Upon completion of examining all dependencies required for all archives, the database will contain all archives and the information about the archive, upon which archive depends.


## Graphical Representation :triangular_ruler:

For a graphical representation of this explanation see our [Hitchhikers Guide to The Indexa.](../docs/index.md) 