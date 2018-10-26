# QuickStart Guide

To start using this project, you first need to clone this repo.

After cloning the repo, navigate to the directory containing the project.

`cd indexa-graph`

Once in the root directory, the project can be built using the gradle wrapper
 
`gradlew clean build`

This command will build a jar and place it in the build folder of each subproject's directory 

If you would like to install this jar into your local maven repository, you can do so by running

`gradlew clean build install`

Doing so, will place the created jar file into the .m2 directory and allow 'mavenLocal()' to be used as a dependency repo

The graph project can now be added as a dependency for your project and can be referenced to use all classes and interfaces available. 

