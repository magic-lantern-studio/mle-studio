#######################################
# The list of libraries we are building seperated by spaces.
# The 'lib_' indicates that these build products will be installed
# in the $(libdir) directory. For example /usr/lib
lib_LTLIBRARIES = libDwpReader.la

#######################################
# Build information for each library

# Sources for libDwpReader
libDwpReader_la_SOURCES = \
    $(top_srcdir)/../../common/JavaDwp/DwpReader/CDwpJNIRegistry.cpp \
    $(top_srcdir)/../../common/JavaDwp/DwpReader/CDwpReaderCache.cpp \
    $(top_srcdir)/../../common/JavaDwp/DwpReader/CDwpTableBuilder.cpp \
    $(top_srcdir)/../../common/JavaDwp/DwpReader/CDwpReader.cpp \
    $(top_srcdir)/../../common/JavaDwp/DwpReader/DwpReader.cpp

# Linker options libTestProgram
libDwpReader_la_LDFLAGS = 

# Compiler options. Here we are adding the include directory
# to be searched for headers included in the source code.
libDwpReader_la_CPPFLAGS = \
	-DMLE_NOT_DLL \
	-I$(top_srcdir)/include \
	-I$(top_srcdir)/../../../include \
    -I/opt/java/jdk1.8.0_111/include \
    -I/opt/java/jdk1.8.0_111/include/linux \
    -I/opt/MagicLantern/include
