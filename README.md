windows-path-unlocker
=====================

unlock windows path from command line

Install
-------

* install java 
* install maven
* mvn clean compile install
* download handle command to fetch process ids: http://technet.microsoft.com/de-de/sysinternals/bb896655

Run
---
* java -Dunlocker.handlepath="<path to handle.exe>" -jar "<path>"
* e.g. java -Dunlocker.handlepath="C:\Progam Files\sysinternals\handle.exe" -jar "C:\Users\someuser\somedirectory"

