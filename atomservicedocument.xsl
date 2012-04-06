<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:a="http://www.w3.org/2005/Atom"
		xmlns:p="http://www.w3.org/2007/app"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <head>
	<link rel="stylesheet" href="/atomic.css" type="text/css" media="screen"/>
      </head>
      <body>
	<ul>
 	  <xsl:for-each select="p:service/p:workspace">
	      <li id="{a:title}" class="workspace">
		<xsl:value-of select="a:title"/>
	      </li>
	      <ul>
 	      <xsl:for-each select="p:collection">
		<li id="{a:title}" class="collection">
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
