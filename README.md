## Selenium Servlets
 
_IntelliJ IDEA mvn project as example, included in repo_
 
###### Launching selenium hub with servlet 

 * Nodes Servlet for Selenium grid 3.141.59
 * http://localhost:4444/grid/admin/nodes

_for windows:_
```
java -cp "Selenium-1.0-SNAPSHOT.jar;selenium-server-standalone-3.141.59.jar" org.openqa.grid.selenium.GridLauncherV3 -role hub -servlets servlet.nodes
```
_for linux:_
```
java -cp "Selenium-1.0-SNAPSHOT.jar:selenium-server-standalone-3.141.59.jar" org.openqa.grid.selenium.GridLauncherV3 -role hub -servlets servlet.nodes
```
_attach node to hub (port optional):_
```
java -jar selenium-server-standalone-3.141.59.jar -role node -hub http://localhost:4444/grid/register -port=5566
```