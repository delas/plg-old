# PLG-CLI

This software is the Command Line Interface of the Process Log Generator. It is composed of two main components, one for the generation of processes, and the other for generating logs.

## Example usage

Usage of the geneartor of processes:
```bash
$ java -jar ProcessCreator.jar
Please add the following parameters:
  - [INT] max number of and branches
  - [INT] max number of xor branches
  - [INT] loop percentage (0-100)
  - [INT] single activity percentage (0-100)
  - [INT] sequence percentage (0-100)
  - [INT] and percentage (0-100)
  - [INT] xor percentage (0-100)
  - [INT] max depth
  - [STRING] process name
  - [STRING] path where to save the file
$ java -jar ProcessCreator.jar 3 3 30 60 40 40 40 3 TestProcess /home/user
Process generation in progress...
Process created!
These files have been produced:
  - /home/user/TestProcess.plg
  - /home/user/TestProcess-hn.dot
  - /home/user/TestProcess-pn.dot
  - /home/user/TestProcess.tpn
```
Usage of the generator of logs:
```bash
$ java -jar LogCreator.jar
Please add the following parameters:
  - [STRING] file PLG with the process file
  - [INT] number of cases of the log
  - [INT] interval percentage (0-100)
  - [INT] error percentage (0-100)
  - [STRING] destination filename (.zip)
$ java -jar LogCreator.jar TestProcess.plg 100 100 0 TestLog.zip
Log generation in progress...
Log created!
This file has been produced:
  - TestLog.zip
```
