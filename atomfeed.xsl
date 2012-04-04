<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:atom="http://www.w3.org/2005/Atom"
		xmlns:app="http://www.w3.org/2007/app"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <head><title>Content</title></head>
      <body>
	<table border="1">
	  <tr>
 	    <xsl:for-each select="feed/entry/properties/*">
	      <td><xsl:value-of select="local-name()"/></td>
	    </xsl:for-each>
	  </tr>
 	  <xsl:for-each select="feed/entry">
	    <tr>
	      <td><xsl:value-of select="content/properties/NAME"/></td>
	      <td><xsl:value-of select="content/properties/AGE"/></td>
	    </tr>
	  </xsl:for-each>
	</table>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet> 
