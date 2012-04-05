<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:a="http://www.w3.org/2005/Atom"
		xmlns:p="http://www.w3.org/2007/app"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <body>
	<h2>Atomic</h2>
	<ul>
 	  <xsl:for-each select="p:service/p:workspace">
	      <li><xsl:value-of select="a:title"/></li>
	      <ul>
 	      <xsl:for-each select="p:collection">
		<li>
		  <a href="{@href}" target="right">
		    <xsl:value-of select="a:title"/>
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
