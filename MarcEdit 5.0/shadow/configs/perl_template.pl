#!/usr/bin/perl -w
use strict;
use Win32::OLE;
use File::Temp qw/ tempfile tempdir /;
use File::Copy;

#BEGIN
<#SAVED_ARGS#>
#END
#========================================================
# Generated <#CURRENT_DATE#> by the MarcEdit Script Maker.
# Description:  The MarcEdit Script Maker is an separate but add-on 
# utility that can be used to quickly generate vbscripts to process 
# MARC files.
# 
# Author:  Terry Reese
#             Oregon State University
#             terry.reese@oregonstate.edu
#             (541) 737-6384
# Copyright:  Users may freely use, modify, distribute scripts created by
# the MarcEdit Script Maker so long this header remains in the
# file.
#=========================================================

#===========================
# Declare Global Constants
#===========================
use constant For_Reading => 1;
use constant For_Writing => 2;
use constant For_Appending => 3;
use constant cdlOFNFileMustExist => 1;
use constant cdlOFNExplorer => 8;

#============================
# Declare Global Variables
#============================
my $lret = 0;

#============================
# Enter the Main Function
#============================
$lret = &Main(&GetUserInput);

print $lret . ' records have been processed.  The script has completed.';

#=====================================================
# Function: GetUserInput
# Description:  This function allows users to either drag a file
# to the script and have it run automatically or prompt for a source file.
#=====================================================

sub GetUserInput {
	#=================================
    # Trap the Error.  If no file is specified, then
    # we assume that a file needs to be given
    # manually.  Prompt for an input box.
    #=================================
    
    my $strSource = "";
    my $dlg_open;
    
    if ($#ARGV >=0) {
		return $ARGV[0];
    }
    
    try {
		$dlg_open = Win32::OLE->new('MSComDlg.CommonDialog.1')
			or $dlg_open = '';
	
		if ($dlg_open ne '') {
			$dlg_open->Filter = 'All Files (*.*)|*.*|';
			$dlg_open->FilterIndex=1;
			$dlg_open->Flags = cdlOFNFileMustExist | cdlOFNExplorer;
			$dlg_open->DialogTitle="Select a file";
			$dlg_open->FileName = "";
			$dlg_open->ShowOpen;
			$strSource = $dlg_open->FileName;
		 
			if ($strSource ne '') {
				die ("Quitting.");
			} else {
				return $strSource;
			}
		}
	} catch {
			print "Enter the full path to your source file:";
			$strSource=<STDIN>;
			chomp($strSource);
			if ($strSource ne '') {
				return $strSource;
			} else {
				die ("Cannot leave source file blank.  Quitting.");
			}
	}
}

#======================================================
#FUNCTION/SUB: Marc_Break
#Description: Encapsulates the MarcBreaker functions
#======================================================

sub Marc_Break {
   my $source = $_[0];
   my $dest = $_[1];
   my $obj_MB;
   my $lret = 0;
   
   if (-e $source) {
	  $obj_MB =  Win32::OLE->new('MARCEngine5.MARC21')
			or die "Cannot start Marc21 Object.\n";
	  $lret = $obj_MB->MarcFile($source, $dest);
	  return $lret;
   } else {
	  die ("Local MARC file could not be located.  Quitting.");
   }
}


#======================================================
#FUNCTION/SUB: Marc_Make
#Description: Encapsulates the MarcMaker Functions
#======================================================

sub Marc_Make {

   my $source = $_[0];
   my $dest = $_[1];
   my $obj_MK;
   my $lret = 0;
   
   if (-e $source) {
	  $obj_MK =  Win32::OLE->new('MARCEngine5.MARC21')
			or die "Cannot start Marc21 Object.\n";
	  $lret = $obj_MK->MMaker($source, $dest);
	  return $lret;
   } else {
	  die ("Local MARC file could not be located.  Quitting.");
   }
}

sub trim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}


sub rtrim($) {
	my $string = shift;
	$string =~ s/^\.//;
	return $string;
}




#==========================================================
# FUNCTION/SUB: IsMARC
# Description: A crude method to quickly see if the file
# is MARC (included so that scripts can access files that 
# are both MARC or mnemonic
#===========================================================
sub IsMARC($) {

my $tString;
my $file = shift;

open(TFILE, "<" . $file) or return -1;
read(TFILE, $tString, 5);
close(TFILE);

	if ($tString=~"m/[^0-9]/") {
		return -1;
	} else {
		return 1;
	}
}



sub IsNumeric($) {
   my $s = shift;
   if ($s =~ /[^0-9]/) {
       return 1;
   } else {
       return -1;
   }
}


sub Main($) {
	my $sfile = shift;
	

	#================
	# Declare Variables
	#================

	my $retval = 0;
	my $str_Dest = "";
	my $str_Data = "";
	my $Marc_String = "";
	my @tmp_Dest;
	my @tmp_handle;
	my $str_Pattern = "";
	my @tmp_array;
	my $tmp_string = "";
	my $bool_IsMarc = -1;
	my $tlcount = 0;
	my @out;
	my $str_subfield = "";
	my $bool_mod_found = -1;

	#==========================
	# Declare variables for the
	# 001, 949 and 229 fields  
	#==========================

	my $str_001 = "";
	my $str_229 = "";
	my $str_949 = "";


	<#NEW VARS#>

	($tmp_handle[0], $tmp_Dest[0]) = tempfile();
	($tmp_handle[1], $tmp_Dest[1]) = tempfile();
	close($tmp_handle[0]);
	close($tmp_handle[1]);
	

	#=======================================================
	# Set the Destination File
	#=======================================================
	
	if (&IsMARC($sfile)>-1) {
		$str_Dest = $sfile . ".rev.mrc";
		$retval = &Marc_Break($sfile, $tmp_Dest[0]);
		$bool_IsMarc=1;
	}else {
		$str_Dest = $sfile . ".rev.mrk";
		copy($sfile, $tmp_Dest[0]);
	}

	
	open(READER, "<" . $tmp_Dest[0]);
	open(WRITER, ">" . $tmp_Dest[1]);
	
	while(<READER>) {
		chomp($_);
		$str_Data = $_;
		if (length(&trim($str_Data))==0) {
			<#ADD/DELETE FIELD#>
			print WRITER $Marc_String . "\r\n";
			$tlcount++;
			$Marc_String = "";
			$str_001="";
			$str_949="";
			$str_229 = "";
		} else {
			<#MOD FIELD#>
			if (length(&trim($str_Data))!=0) {
				$Marc_String .= $str_Data . "\r\n";
			}
		}
	}

	close(READER);
	close(WRITER);

	if ($bool_IsMarc==1) {
		$tlcount= &Marc_Make($tmp_Dest[1], $str_Dest);
	} else {
		copy($tmp_Dest[1], $str_Dest);
	}

	for(my $x = 0; $x<$#tmp_Dest; $x++) {
		unlink($tmp_Dest[$x]);
	}
	
	return $tlcount;

	
}
