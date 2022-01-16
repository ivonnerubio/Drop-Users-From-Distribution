# Drop Users From a Distribution List


CONTENTS OF THIS FILE
---------------------

 * Introduction
 * Requirements
 * Installation


INTRODUCTION
------------

The distribution control will automatically remove users or roles from the distribution list on the discussion section of a document. Once it is implemented, it will remove the user/roles even if they were manually added to a document.

REQUIREMENTS
------------

This control requires the following items:

 * On_Load_Discussion.java
 * Create Discussion Onload Trigger


INSTALLATION
------------
 
 1. Create a system task that executes a java script and paste the text from the On_Load_Discussion.java file. Name it "On_Load_Discussion"

 2. Create a trigger with the following values: 
	- Name: On Load Discussion
	- Conditions: *Folder where the discussion control will be applied*
	- Event: Create Discussion
	- Task Mode: Onload
	- Task: On_Load_Discussion
	
Congratulations! When creating a new dicussion on your document it should remove any users and roles from the distribution automatically. 
