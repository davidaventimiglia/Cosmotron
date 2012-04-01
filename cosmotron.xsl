<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:atom="http://www.w3.org/2005/Atom"
		xmlns:app="http://www.w3.org/2007/app"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <body>
	<h2>Atomic</h2>
	<ul>
 	  <xsl:for-each select="service/workspace">
	      <li><xsl:value-of select="atom:title"/></li>
	      <ul>
 	      <xsl:for-each select="collection">
		<li>
		  <a href="{@href}">
		    <xsl:value-of select="atom:title"/>
		  </a>
	      </li>
	      </xsl:for-each>
	      </ul>
	  </xsl:for-each>
	</ul>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet> 
