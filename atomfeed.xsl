<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:atom="http://www.w3.org/2005/Atom"
		xmlns:app="http://www.w3.org/2007/app"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:a="http://www.w3.org/2005/Atom">
  <xsl:template match="/">
    <html>
      <head>
	<link rel="stylesheet" href="/atomic.css" type="text/css" media="screen"/>
      </head>
      <body>
	<xsl:if test="count(a:feed/a:entry)>0">
	  <table>
	    <tr>
 	      <xsl:for-each select="a:feed/a:entry[1]/a:content/properties/*">
		<th><xsl:value-of select="local-name()"/></th>
	      </xsl:for-each>
	    </tr>
 	    <xsl:for-each select="a:feed/a:entry">
	      <tr>
 		<xsl:for-each select="a:content/properties/*">
		  <td><xsl:value-of select="."/></td>
		</xsl:for-each>
	      </tr>
	    </xsl:for-each>
	  </table>
	</xsl:if>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet> 
