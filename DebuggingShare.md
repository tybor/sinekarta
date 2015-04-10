# How to debug share plugin #

Before reading this page, ensure that you have a valid eclipse environment : https://code.google.com/p/sinekarta/wiki/projectBuild

## have a tomcat running into your eclipse ##

  * go to Window->preferences
  * server->runtime environment
  * click add
  * select Apache Tomcat 7.0 and click next
  * set /opt/alfresco-4.2.f.skds/tomcat as installation directory
  * click finish
  * go to Window->show view->other
  * select "servers" and press ok
  * rigth click on servers view
  * new->server
  * select "apache tomcat 7"
  * click finish
  * double click on server created
  * in Ports section set (these port MUST be different from Alfresco port)
    * tomcat admin port to 7005
    * http to 7080
    * ajp to 7009
    * ssl to 7443
  * select "module"s tab
  * press "add external web module"
  * set "docuemnt base" to < workspacedir >/work/share (your work directory)
  * set path to /share
  * save tomcat server configuration

start tomcat and go to : http://localhost:7080/share