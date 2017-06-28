<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"  
xmlns:marc="http://www.loc.gov/MARC21/slim"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="marc xsi">
	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
	
	
	<xsl:template match="/">
    <cdfrec>
      <xsl:variable name="oclcleader" select="marc:collection/marc:record/marc:leader/." />
      <a>
        <xsl:value-of select="substring($oclcleader, 6, 5)"/>
        <xsl:value-of select="substring($oclcleader, 18, 3)" />
      </a>
      <xsl:for-each select="marc:collection/marc:record/marc:controlfield">
        <xsl:element name="c{@tag}">
          <xsl:value-of select="text()"/>
        </xsl:element>
      </xsl:for-each>

      <xsl:for-each select="marc:collection/marc:record/marc:datafield">
        <xsl:element name="v{@tag}">
          <xsl:attribute name="im">0</xsl:attribute>
          <xsl:attribute name="i1">
            <xsl:value-of select="@ind1"/>
          </xsl:attribute>
          <xsl:attribute name="i2">
            <xsl:value-of select="@ind2"/>
          </xsl:attribute>
          <xsl:value-of select="text()"/>
          <xsl:for-each select="marc:subfield">
            <xsl:element name="s{@code}">
              <d>
                <xsl:value-of select="text()" />
              </d>
            </xsl:element>
          </xsl:for-each>
        </xsl:element>
      </xsl:for-each>
    </cdfrec>
	</xsl:template>
</xsl:stylesheet>