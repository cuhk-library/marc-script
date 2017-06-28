<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
		<head>
			<title>MarcEdit Tutorials</title>
		</head>
		
		<body>
			<xsl:if test="tutorial/item/dc.relation='Online Tutorials'">
				<center><img src="logo.png" alt="MarcEdit: Your free MARC editing software" /></center>
				<br />
				<center><a href="#remote">Remote Tutorials</a> | <a href="#local">Local Tutorials</a></center>
				<br />
			</xsl:if>
			<xsl:if test="tutorial/item/dc.relation='Online Tutorials'">
				<h2><a name="remote">
					<xsl:value-of select="tutorial/item/dc.relation" />
					</a>
				</h2>
			</xsl:if>
			
			<xsl:if test="tutorial/item/dc.relation!='Online Tutorials'">
				<h2><a name="local">
					<xsl:value-of select="tutorial/item/dc.relation" />
					</a>
				</h2>
			</xsl:if>
			
			<table style="font-size:12;font-family:Arial;">
				<xsl:for-each select="tutorial/item"> 
				<xsl:sort select="dc.title" />
					<tr><td>				
					<table>
						<tr>
							<td valign="top">
							<b>Title:   </b>  <i><xsl:value-of select="dc.title" /></i><br />
								<xsl:if test="dc.description=''" >
									<b>Description: </b><br />
								</xsl:if>
								<xsl:if test="dc.description!=''" >
									<b>Description:  </b><xsl:value-of select="dc.description" /><br />		
								</xsl:if>
							
							
								<xsl:if test="dc.identifier=''" >
									<b>URL:  </b> No URL Available<br />
								</xsl:if>
								<xsl:if test="dc.identifier!=''" >
									<b>URL:  </b>  
									<a><xsl:attribute name="href">
											<xsl:value-of select="dc.identifier" />
										</xsl:attribute><xsl:value-of select="dc.identifier" />
									</a><br />
								</xsl:if>
							<b>Last Modified:  </b><xsl:value-of select="dc.date" /> <br />
							</td>
						</tr>
					</table>
					</td></tr>
				</xsl:for-each>
			</table>
		</body>
		</html>


	</xsl:template>
</xsl:stylesheet>

  