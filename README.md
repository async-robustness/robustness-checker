# Robustness Checker

A temporary repo for event determinism and event serializability checking implemented for POPL'17.


## Contents

- Instrumentor library
    - Contains the code that instruments an application with the "Instrumentation" library
    
    
- Instrumentation library which contains:
	- Event-determinism checker code
    - Event-serializability checker code
    - Android library models
        - Stubs for Android classes referenced by the applications
   
       
- Android applications with driver classes to run the applications
    - Aarddict app
    - Apphangar app
    - Bookworm app
    - GrandRiverTransit app
    - Irccloud app
    - Vlillechecker app
    

## Requirements

- Java SDK 8

- [Java Path Finder](http://javapathfinder.sourceforge.net/)
  
  -  Set JPF_HOME path variable to your jpf home directory.
     
     ${JPF_HOME}/jpf-core/bin/jpf must be accessible.
     
  - (Optional) You can modify the JPF exploration parameters in Test.jpf file inside the app directory. But make sure that you have added the AssertionProperty listener.
	
  	
  		listener+=.listener.AssertionProperty;  

- Apache Ant     
   

## Usage


An application can be compiled, instrumented and then the event serializability is checked by using the following command. It checks the event-serializability of the events invoked in the driver class specified in Test.jpf file.

Compile and instrument an app:

	cd checker 
	
	ant -Dapp.name=irccloud app
	
	
To check for the determinism or serializability of the events under analysis, provide your .jpf file that specifies the target driver class along with some properties. 
		
	ant -Dapp.name=irccloud -Djpf.file=SerSendClickUser verify-app
	
(Optional) You can print the accesses processed by the determinism or the serializability checker by running the app in the LOGRWDET or LOGRWSER mode respectively. 

	ant -Dapp.name=irccloud -Dmode=LOGRWSER -Dapp.class=SerSendClickUser run-app

	


	