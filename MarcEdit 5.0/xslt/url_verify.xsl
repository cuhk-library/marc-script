<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<html>
    <head>
      <title>URL Checker Results</title>
      <style>
        body { font-size:10pt; font-face:arial;}
        a, a:link, a:active {text-decoration: none;}
        a:hover {text-decoration: underline;}
      </style>
      <script>
        function checkClose(){
        var q = "Do you really want to close the window?";
        if(confirm(q)){return true}
        else {window.open(location.href);}
        }
      </script>
    </head>
    <body onbeforeunload="checkClose();">

	<table width="95%" border="1" cellpadding="1" cellspacing="0">
	<tr><td colspan="4"><b>Errors:</b></td></tr>
	<tr><td valign="top"><b>Display Field</b></td><td valign="top" align="center"><b>URL</b></td><td valign="top" align="center"><b>Status Code</b></td><td valign="top" align="right"><b>Status Message</b></td></tr>
	<xsl:call-template name="url_validate_errors" />
	<tr><td colspan="4"><br /> </td></tr>
	<tr><td colspan="4"><b>Validated:</b></td></tr>
	<tr><td valign="top"><b>Display Field</b></td><td valign="top" align="center"><b>URL</b></td><td valign="top" align="center"><b>Status Code</b></td><td valign="top" align="right"><b>Status Message</b></td></tr>
	<xsl:call-template name = "url_validate_nonerrors" />
	</table>
	</body>
	</html>
</xsl:template>

<xsl:template name="url_validate_errors">
    <xsl:for-each select="url_validate">
		<xsl:for-each select="record">
			<xsl:sort select="statuscode" />
			<xsl:if test="statuscode!='200'">
			<tr>
			<td valign="top"><xsl:value-of select="normalize-space(title/.)" /></td>
			<td valign="top">
			<a>
			<xsl:attribute name="href"><xsl:value-of select="normalize-space(url/.)" /></xsl:attribute><xsl:value-of select="normalize-space(url/.)" />
			</a>
			</td>
			<td valign="top"><xsl:value-of select="normalize-space(translate(statuscode/., ';', ''))" /></td>
			<td valign="top"><xsl:value-of select="normalize-space(translate(statusmessage/., ';', ''))" />; <xsl:value-of select="normalize-space(errormessage/.)" /></td>
			</tr>
			</xsl:if>
		</xsl:for-each>
	</xsl:for-each>
</xsl:template>

<xsl:template name="url_validate_nonerrors">
	<xsl:for-each select="url_validate">
		<xsl:for-each select="record">
			<xsl:sort select="statuscode" />
			<xsl:if test="statuscode='200'">
			<tr>
			<td valign="top"><xsl:value-of select="normalize-space(title/.)" /></td>
			<td valign="top">
			<a>
			<xsl:attribute name="href"><xsl:value-of select="normalize-space(url/.)" /></xsl:attribute><xsl:value-of select="normalize-space(url/.)" />
			</a>
			</td>
			<td valign="top"><xsl:value-of select="normalize-space(statuscode/.)" /></td>
			<td valign="top"><xsl:value-of select="normalize-space(statusmessage/.)" />; <xsl:value-of select="normalize-space(errormessage/.)" /></td>
			</tr>
			</xsl:if>
		</xsl:for-each>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>

  