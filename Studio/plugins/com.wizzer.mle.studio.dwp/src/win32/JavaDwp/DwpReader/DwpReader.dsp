# Microsoft Developer Studio Project File - Name="DwpReader" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Dynamic-Link Library" 0x0102

CFG=DwpReader - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "DwpReader.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "DwpReader.mak" CFG="DwpReader - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "DwpReader - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "DwpReader - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "DwpReader - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "DWPREADER_EXPORTS" /YX /FD /c
# ADD CPP /nologo /Zp8 /MT /W3 /GX /O2 /I "../../../../include" /I "$(JAVA_HOME)/include" /I "$(JAVA_HOME)/include/win32" /I "$(MLE_ROOT)/include" /D "ML_CDECL" /D "MLE_NOT_DLL" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "DWPREADER_EXPORTS" /YX /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /machine:I386
# ADD LINK32 DWP.lib util.lib mlmath.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib psapi.lib /nologo /dll /incremental:yes /machine:I386 /libpath:"$(MLE_HOME)/lib/tools" /libpath:"$(MLE_HOME)/Core/util/win32/lib"
# SUBTRACT LINK32 /pdb:none
# Begin Special Build Tool
SOURCE="$(InputPath)"
PostBuild_Desc=Copying DLL to Eclipse directory.
PostBuild_Cmds=mkdir ..\..\..\..\os\win32\x86	copy Release\DwpReader.dll ..\..\..\..\os\win32\x86\DwpReader.dll
# End Special Build Tool

!ELSEIF  "$(CFG)" == "DwpReader - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MTd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "DWPREADER_EXPORTS" /YX /FD /GZ /c
# ADD CPP /nologo /Zp8 /MTd /W3 /Gm /GX /ZI /Od /I "../../../../include" /I "$(JAVA_HOME)/include" /I "$(JAVA_HOME)/include/win32" /I "$(MLE_HOME)/include" /D "ML_CDECL" /D "MLE_NOT_DLL" /D "MLE_DEBUG" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "DWPREADER_EXPORTS" /YX /FD /GZ /c
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /debug /machine:I386 /pdbtype:sept
# ADD LINK32 DWPd.lib utild.lib mlmathd.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib psapi.lib /nologo /dll /debug /machine:I386 /pdbtype:sept /libpath:"$(MLE_HOME)/lib/tools" /libpath:"$(MLE_HOME)/Core/util/win32/lib"
# SUBTRACT LINK32 /pdb:none
# Begin Special Build Tool
SOURCE="$(InputPath)"
PostBuild_Desc=Copying DLL to Eclipse directory.
PostBuild_Cmds=mkdir ..\..\..\..\os\win32\x86	copy Debug\DwpReader.dll ..\..\..\..\os\win32\x86\DwpReader.dll
# End Special Build Tool

!ENDIF 

# Begin Target

# Name "DwpReader - Win32 Release"
# Name "DwpReader - Win32 Debug"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\CDwpJNIRegistry.cpp
# End Source File
# Begin Source File

SOURCE=.\CDwpReader.cpp
# End Source File
# Begin Source File

SOURCE=.\CDwpReaderCache.cpp
# End Source File
# Begin Source File

SOURCE=.\CDwpTableBuilder.cpp
# End Source File
# Begin Source File

SOURCE=.\DwpReader.cpp
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\CDwpJNIRegistry.h
# End Source File
# Begin Source File

SOURCE=.\CDwpReader.h
# End Source File
# Begin Source File

SOURCE=.\CDwpReaderCache.h
# End Source File
# Begin Source File

SOURCE=.\CDwpTableBuilder.h
# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# End Target
# End Project
