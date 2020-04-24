## Selenium servlets
 
###### Launching selenium hub with servlet 

 * Nodes Servlet for Selenium grid 3.141.59
 * http://localhost:4444/grid/admin/nodes

```json5
//Example output
{
  "nodes": [
    {
      "sessions": [
        "84dc816d439a74a0ec48e6bfbd659ee9",
        "ceb5b64922eb8fad6bda8a875506604e",
        "67710195-8323-4498-a649-ac3cb2ee99a5"
      ],
      "port": 5566,
      "host": "192.168.56.1",
      "url": "http://192.168.56.1:5566"
    },
    {
      "sessions": [],
      "port": 43952,
      "host": "192.168.56.1",
      "url": "http://192.168.56.1:43952"
    }
  ]
}
```
_for windows:_
```
java -cp "json-20190722.jar;selenium-1.0-SNAPSHOT.jar;selenium-server-standalone-3.141.59.jar" org.openqa.grid.selenium.GridLauncherV3 -role hub -servlets servlet.nodes
```
_for linux:_
```
java -cp json-20190722.jar:selenium-1.0-SNAPSHOT.jar:selenium-server-standalone-3.141.59.jar org.openqa.grid.selenium.GridLauncherV3 -role hub -servlets servlet.nodes
```
_attach node to hub (port optional):_
```
java -jar selenium-server-standalone-3.141.59.jar -role node -hub http://localhost:4444/grid/register -port 5566
```