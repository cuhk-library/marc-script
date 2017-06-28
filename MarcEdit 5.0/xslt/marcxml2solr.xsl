<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:marc="http://www.loc.gov/MARC21/slim"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="marc">

  <xsl:output method="xml" indent="yes" encoding="utf-8"/>

  <xsl:template match="/">
    <add>
      <!--<xsl:call-template name="marc:record"/>-->
      <xsl:apply-templates />
    </add>
  </xsl:template>

  <xsl:template match="marc:record">
    <xsl:if test="marc:datafield[@tag=245]/marc:subfield[@code='a']">
      <doc>
        <xsl:choose>
          <xsl:when test="marc:datafield[@tag=907]/marc:subfield[@code='a']">
            <field name="id">
              <xsl:value-of select="substring(marc:datafield[@tag=907]/marc:subfield[@code='a'],2)" />
            </field>
          </xsl:when>
          <xsl:otherwise>
            <field name="id">none</field>
          </xsl:otherwise>
        </xsl:choose>
        <field name="format">
          <xsl:variable name="form" select="substring(marc:leader, 7,1)" />
          <xsl:if test="$form='a' or $form='c'">Books</xsl:if>
          <xsl:if test="$form='g' or $form='k' or $form='r' or $form='o'">Visual Materials</xsl:if>
          <xsl:if test="$form='p'">Mix Materials</xsl:if>
          <xsl:if test="$form='e' or $form='f'">Maps</xsl:if>
          <xsl:if test="$form='c' or $form='d'">Musical Scores</xsl:if>
          <xsl:if test="$form='i' or $form='j'">Sound Recordings</xsl:if>
          <xsl:if test="$form='m'">Electronic Resources</xsl:if>
        </field>

        <field name="language">
          <xsl:value-of select="substring(marc:controlfield[@tag=008], 36, 3)"/>
        </field>

        <xsl:if test="marc:datafield[@tag=020]/marc:subfield[@code='a']">
          <field name="isbn">
            <xsl:value-of select="marc:datafield[@tag=020]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=022]/marc:subfield[@code='a']">
          <field name="issn">
            <xsl:value-of select="marc:datafield[@tag=022]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:choose>
          <xsl:when test="marc:datafield[@tag=090]">
            <field name="callnumber">
              <xsl:value-of select="marc:datafield[@tag=090]/marc:subfield[@code='a']"/>
              <xsl:value-of select="marc:datafield[@tag='090']/marc:subfield[@code='b']"/>
            </field>
          </xsl:when>
          <xsl:otherwise>
            <xsl:if test="marc:datafield[@tag=050]">
              <field name="callnumber">
                <xsl:value-of select="marc:datafield[@tag=050]/marc:subfield[@code='a']"/>
                <xsl:value-of select="marc:datafield[@tag='050']/marc:subfield[@code='b']"/>
              </field>
            </xsl:if>
          </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="marc:datafield[@tag=100]/marc:subfield[@code='a']">
          <field name="author">
            <xsl:call-template name="chopPunctuation">
              <xsl:with-param name="chopString" select="marc:datafield[@tag=100]/marc:subfield[@code='a']" />
            </xsl:call-template>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=110]/marc:subfield[@code='a']">
          <field name="author">
            <xsl:call-template name="chopPunctuation">
              <xsl:with-param name="chopString" select="marc:datafield[@tag=110]/marc:subfield[@code='a']" />
            </xsl:call-template>
          </field>
        </xsl:if>

        <!--<xsl:if test="marc:datafield[@tag=111]/marc:subfield[@code='a']">
        <field name="author">
			<xsl:call-template name="chopPunctuation">
				<xsl:with-param name="chopString" select="marc:datafield[@tag=111]/marc:subfield[@code='a']" />
			</xsl:call-template>
        </field>
      </xsl:if>-->

        <field name="title">
          <xsl:value-of select="marc:datafield[@tag=245]/marc:subfield[@code='a']"/>
          <xsl:text> </xsl:text>
          <xsl:value-of select="marc:datafield[@tag='245']/marc:subfield[@code='b']"/>
        </field>

        <xsl:for-each select="marc:datafield[@tag=246]">
          <field name="title2">
            <xsl:value-of select="./marc:subfield[@code='a']"/>
          </field>
        </xsl:for-each>

        <xsl:if test="marc:datafield[@tag=240]/marc:subfield[@code='a']">
          <field name="title2">
            <xsl:value-of select="marc:datafield[@tag=240]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=130]/marc:subfield[@code='a']">
          <field name="title2">
            <xsl:value-of select="marc:datafield[@tag=130]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=260]/marc:subfield[@code='b']">
          <field name="publisher">
            <xsl:value-of select="marc:datafield[@tag=260]/marc:subfield[@code='b']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=260]/marc:subfield[@code='c']">
          <field name="publishDate">
            <xsl:value-of select="marc:datafield[@tag=260]/marc:subfield[@code='c']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=300]/marc:subfield[@code='b']">
          <field name="physical">
            <xsl:value-of select="marc:datafield[@tag=300]/marc:subfield[@code='b']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=362]/marc:subfield[@code='a']">
          <field name="dateSpan">
            <xsl:value-of select="marc:datafield[@tag=362]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=440]/marc:subfield[@code='a']">
          <field name="series">
            <xsl:value-of select="marc:datafield[@tag=440]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=505]/marc:subfield[@code='a']">
          <field name="contents">
            <xsl:value-of select="marc:datafield[@tag=505]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=505]/marc:subfield[@code='t']">
          <field name="contents">
            <xsl:for-each select="marc:datafield[@tag=505]/marc:subfield[@code='t']">
              <xsl:value-of select="."/>
              <xsl:text> </xsl:text>
            </xsl:for-each>
          </field>
        </xsl:if>

        <xsl:call-template name="subjects"/>

        <xsl:for-each select="marc:datafield[@tag=700]">
          <field name="author2">
            <xsl:call-template name="chopPunctuation">
              <xsl:with-param name="chopString" select="./marc:subfield[@code='a']" />
            </xsl:call-template>
          </field>
        </xsl:for-each>

        <xsl:if test="marc:datafield[@tag=780]/marc:subfield[@code='a']">
          <field name="oldTitle">
            <xsl:value-of select="marc:datafield[@tag=780]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=785]/marc:subfield[@code='a']">
          <field name="newTitle">
            <xsl:value-of select="marc:datafield[@tag=785]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:if test="marc:datafield[@tag=830]/marc:subfield[@code='a']">
          <field name="series">
            <xsl:value-of select="marc:datafield[@tag=830]/marc:subfield[@code='a']"/>
          </field>
        </xsl:if>

        <xsl:for-each select="marc:datafield[@tag=856]/marc:subfield[@code='u']">
          <field name="url">
            <xsl:value-of select="."/>
          </field>
        </xsl:for-each>
      </doc>
    </xsl:if>

  </xsl:template>
  <xsl:template match="text()" />

  <xsl:template name="subjects">
    <xsl:for-each select="marc:datafield[@tag=600]">
      <field name="subject1">
        <xsl:call-template name="chopPunctuation">
          <xsl:with-param name="chopString" select="./marc:subfield[@code='a']" />
        </xsl:call-template>
      </field>
    </xsl:for-each>
    <xsl:for-each select="marc:datafield[@tag=610]">
      <field name="subject2">
        <xsl:call-template name="chopPunctuation">
          <xsl:with-param name="chopString" select="./marc:subfield[@code='a']" />
        </xsl:call-template>

      </field>
    </xsl:for-each>
    <xsl:for-each select="marc:datafield[@tag=630]">
      <field name="subject3">
        <xsl:call-template name="chopPunctuation">
          <xsl:with-param name="chopString" select="./marc:subfield[@code='a']" />
        </xsl:call-template>
      </field>
    </xsl:for-each>
    <xsl:for-each select="marc:datafield[@tag=650]">
      <xsl:if test="./marc:subfield[@code='a']">
        <field name="subject4a">
          <xsl:call-template name="chopPunctuation">
            <xsl:with-param name="chopString" select="./marc:subfield[@code='a']" />
          </xsl:call-template>

        </field>
      </xsl:if>

      <xsl:if test="./marc:subfield[@code='x']">
        <field name="subject4x">
          <xsl:call-template name="chopPunctuation">
            <xsl:with-param name="chopString" select="./marc:subfield[@code='x']" />
          </xsl:call-template>
        </field>
      </xsl:if>

      <xsl:if test="./marc:subfield[@code='z']">
        <field name="subject4z">
          <xsl:call-template name="chopPunctuation">
            <xsl:with-param name="chopString" select="./marc:subfield[@code='z']" />
          </xsl:call-template>
        </field>
      </xsl:if>
    </xsl:for-each>
    <xsl:for-each select="marc:datafield[@tag=651]">
      <xsl:if test="./marc:subfield[@code='a']">
        <field name="subject5">
          <xsl:call-template name="chopPunctuation">
            <xsl:with-param name="chopString" select="./marc:subfield[@code='a']" />
          </xsl:call-template>
        </field>
      </xsl:if>
    </xsl:for-each>
    <xsl:for-each select="marc:datafield[@tag=655]">
      <xsl:if test="marc:datafield[@tag=655]">
        <field name="subject6">
          <xsl:value-of select="./marc:subfield[@code='a']"/>
        </field>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="chopPunctuation">
    <xsl:param name="chopString"/>
    <xsl:param name="punctuation">
      <xsl:text>.:,;/ </xsl:text>
    </xsl:param>
    <xsl:variable name="length" select="string-length($chopString)"/>
    <xsl:choose>
      <xsl:when test="$length=0"/>
      <xsl:when test="contains($punctuation, substring($chopString,$length,1))">
        <xsl:call-template name="chopPunctuation">
          <xsl:with-param name="chopString" select="substring($chopString,1,$length - 1)"/>
          <xsl:with-param name="punctuation" select="$punctuation"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="not($chopString)"/>
      <xsl:otherwise>
        <xsl:value-of select="$chopString"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
