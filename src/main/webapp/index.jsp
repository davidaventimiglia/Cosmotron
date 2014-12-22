<%@ page contentType="text.html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.apache.olingo.odata2.api.edm.*"%>
<%@ taglib prefix="my" uri="http://www.neptunestation.com/cosmotron-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>Cosmotron</title>
  </head>
  <body>
    <h1>Cosmotron</h1>
    <p>${my:helloWorld("World")}</p>
  </body>
</html>
