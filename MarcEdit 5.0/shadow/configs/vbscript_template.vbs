'BEGIN
<#SAVED_ARGS#>
'END
'========================================================
' Generated <#CURRENT_DATE#> by the MarcEdit Script Maker.
' Description:  The MarcEdit Script Maker is an separate but add-on 
' utility that can be used to quickly generate vbscripts to process 
' MARC files.
' 
' Author:  Terry Reese
'             Oregon State University
'             terry.reese@oregonstate.edu
'             (541) 737-6384
' Copyright:  Users may freely use, modify, distribute scripts created by
' the MarcEdit Script Maker so long this header remains in the
' file.
'=========================================================

'===========================
' Declare Global Constants
'===========================
Const For_Reading = 1
Const For_Writing = 2
Const For_Appending = 8


'===========================
' Declare Global Variables
'===========================
Dim fso
Dim lret
Dim obj_Reg

Set fso = CreateObject("scripting.FileSystemObject")
Set obj_Reg = new RegExp

'======================================
' Enter the Main Function
'======================================

lret = Main(GetUserInput())

msgbox lret & " records have been processed.  The Script has completed."


'=====================================================
' Function: GetUserInput
' Description:  This function allows users to either drag a file
' to the script and have it run automatically or prompt for a source file.
'=====================================================

Function GetUserInput
    '=================================
    ' Trap the Error.  If no file is specified, then
    ' we assume that a file needs to be given
    ' manually.  Prompt for an input box.
    '=================================

       Const cdlOFNFileMustExist = &H1000
       Const cdlOFNExplorer = &H80000

    Dim str_Source
    Dim dlg_Open   ' This holds the Dialog object property if available

    On Error Resume Next
    GetUserInput = Wscript.Arguments(0)

    If Err<>0 then
    On error resume next
    Set dlg_Open = CreateObject("MSComDlg.CommonDialog.1")
    if Err=0 then
       with dlg_Open 
           .Filter = "All files (*.*)|*.*|"
           .FilterIndex = 1  
           .Flags = cdlOFNFileMustExist Or cdlOFNExplorer
           .DialogTitle = "Select a file"
           .FileName = ""
           .CancelError = False
           .MaxFileSize=255
           .ShowOpen
           str_Source = .FileName ' retrieve the result
       end with

           if str_Source = chr(0) or len(trim(str_source))=0 then
              wscript.quit
           end if 
           GetUserInput = str_Source


    else 
      str_Source = InputBox("Enter your Source path (Remember that this file must be in tab delimited format)")
              if Len(trim(str_Source))=0 then
                msgbox "You must enter a path name....Program stopping."
                Wscript.Quit
              Else
                If Instr(str_Source, "\")=0 then
                  str_Source = Mid(WScript.ScriptFullName, 1, InstrRev(WScript.ScriptFullName, "\")) & str_Source
                  GetUserInput = str_Source
                Else
                  GetUserInput = str_Source
                End if
              End if
     End if
     End if

End Function

'======================================================
'FUNCTION/SUB: Marc_Break
'Description: Encapsulates the MarcBreaker functions
'======================================================

Function Marc_Break(source, dest)

Dim obj_MB
Dim lret


if fso.FileExists(source)=false then
    msgbox "Local MarcFile Could not be located.  Quitting"
    wscript.quit
end if

Set obj_MB=CreateObject("MARCEngine5.MARC21")
lret=obj_MB.MarcFile(source, dest)


set obj_MB=Nothing
Marc_Break=lret

end Function

'======================================================
'FUNCTION/SUB: Marc_Make
'Description: Encapsulates the MarcMaker Functions
'======================================================

Function Marc_Make(source, dest)

Dim obj_MK

Set obj_MK=CreateObject("MARCEngine5.MARC21")
lret=obj_MK.MMaker(source, dest)

Set obj_MK=Nothing

Marc_Make=lret
end function

'=======================================================
' FUNCTION/SUB: CreateTempFile
' Description: Retrieves the Name of a Temporary file located in the
' Temp. Directory.
'=======================================================
Function CreateTempFile

Dim tfolder, tname
Const TemporaryFolder = 2

Set tfolder = fso.GetSpecialFolder(TemporaryFolder)
tname = fso.GetTempName
CreateTempFile = tname

End Function


'==========================================================
' FUNCTION/SUB: CJoin
' Description: Custom join command to make certain that the MarcData is
' re-created correctly
'==========================================================
Function CJoin(t(), del)
  Dim lx
  Dim tMarc
  for lx = 0 to Ubound(t)
      if len(trim(t(lx)))<>0 then
         tMarc = tMARC & t(lx) & vbcrlf
      end if
   next
   CJoin = tMARC
End Function

'==========================================================
' FUNCTION/SUB: SortListL
' Description: Sorts MARC Fields
'==========================================================
Sub SortListL(l(), max)
    Dim count
    Dim x
    Dim inc
    Dim temp
    Dim y

    'Get out if there's nothing to do.
    If max < 1 Then
        Exit Sub
    End If

    inc = max \ 2

    Do Until inc < 1
        For x = inc + 1 To max
            temp = l(x)
            For y = x - inc To 1 Step -inc
                If temp >= l(y) Then
                    Exit For
                End If
                l(y + inc) = l(y)
            Next
            l(y + inc) = temp
        Next
        inc = inc \ 2
    Loop

End Sub

'==========================================================
' FUNCTION/SUB: IsMARC
' Description: A crude method to quickly see if the file
' is MARC (included so that scripts can access files that 
' are both MARC or mnemonic
'===========================================================
Function IsMARC(sFile)

Dim tString, tfile

Set tfile = fso.OpenTextFile(sFile, 1)
tString = tfile.read(5)
tfile.close
if isnumeric(tString)=TRUE then 
    IsMARC = TRUE
else
    IsMARC = FALSE
end if
End Function 

'====================================================
' Function: Main
' Description:  Main is the Main procedure for the script.
'====================================================


Function Main(str_Source)


'================
' Declare Objects
'================
Dim obj_Source
Dim obj_Dest(1)
Dim Matches


'================
' Declare Variables
'================

Dim retval
Dim str_Dest
Dim str_Data
Dim Marc_String
Dim tmp_Dest(1)
Dim str_Pattern
Dim tmp_array
Dim tmp_string
Dim bool_isMarc
Dim tlcount

'==========================
' Declare variables for the
' 001, 949 and 229 fields  
'==========================

Dim str_001
Dim str_229
Dim str_949


<#NEW VARS#>

tmp_Dest(0) = CreateTempFile
tmp_Dest(1) = CreateTempFile
tmp_string = Mid(str_source, 1, instrrev(str_source, "\"))
'====================================================
' We need to set the Temp Destinations because the 
' script engine will fill in the path, but the path 
' has to be explicitly defined for MarcEdit's Functions
'=====================================================

tmp_Dest(0) = tmp_string & tmp_Dest(0) 
tmp_Dest(1) = tmp_string & tmp_Dest(1) 

'=======================================================
' Set the Destination File
'=======================================================

str_Dest = tmp_string & Mid(str_Source, Instrrev(str_Source, "\"))
If IsMARC(str_Source)=TRUE then
     str_Dest = Mid(str_Dest, 1, Len(str_dest) - 4) & "rev.mrc"
     retval = Marc_Break(str_Source, tmp_Dest(0))
     bool_IsMARC=TRUE
else
     str_Dest = Mid(str_Dest, 1, Len(str_Dest) - 4) & "rev.mrk"
     fso.CopyFile str_Source, tmp_Dest(0)
end if

Set obj_Dest(0) = fso.OpenTextFile(tmp_Dest(0), For_Reading)
Set obj_Dest(1) = fso.OpenTextFile(tmp_Dest(1), For_Writing, TRUE)

Do while not obj_Dest(0).AtEndOfStream
    str_Data = obj_Dest(0).ReadLine
    if len(trim(str_Data))=0 then
		<#ADD/DELETE FIELD#>
        obj_Dest(1).Write Marc_String & vbcrlf 
		tlcount = tlcount + 1
		Marc_String = ""
		str_001 = ""
		str_949 = ""	
		str_229 = ""	
    else
		<#MOD FIELD#>
		if Len(Trim(str_Data))<>0 then
			Marc_String = Marc_String & str_Data & vbcrlf
		End if
    end if
Loop

For x = 0 to Ubound(obj_Dest)
  obj_Dest(x).Close
Next

if bool_IsMARC=TRUE then 
   Main = Marc_Make(tmp_Dest(1), str_Dest)
else
   fso.CopyFile tmp_Dest(1), str_Dest
   Main = tlcount
end if

For x = 0 to Ubound(tmp_Dest)
  fso.DeleteFile (tmp_Dest(x))
Next
End Function
