#######################################
# The list of libraries we are building seperated by spaces.
# The 'lib_' indicates that these build products will be installed
# in the $(libdir) directory. For example /usr/lib
lib_LTLIBRARIES = libDwpReader.la

#######################################
# Build information for each library

# Sources for libDwpReader
libDwpReader_la_SOURCES = \
    $(top_srcdir)/../../common/jni/CDwpJNIRegistry.cpp \
    $(top_srcdir)/../../common/jni/CDwpReaderCache.cpp \
    $(top_srcdir)/../../common/jni/CDwpTableBuilder.cpp \
    $(top_srcdir)/../../common/jni/CDwpReader.cpp \
    $(top_srcdir)/../../common/jni/DwpReader.cpp

# Linker options libDwpReader
libDwpReader_la_LDFLAGS = -L/opt/MagicLantern/lib -lDWP -lmlutil

# Compiler options. Here we are adding the include directory
# to be searched for headers included in the source code.
libDwpReader_la_CPPFLAGS = \
	-DMLE_NOT_DLL \
	-I$(top_srcdir)/include \
	-I$(top_srcdir)/../../common/include \
    -I/opt/java/jdk1.8.0_111/include \
    -I/opt/java/jdk1.8.0_111/include/linux \
    -I/opt/MagicLantern/include

install_plugin:
	../install-sh .libs/libDwpReader.so $(MLE_HOME)/Studio/plugins/com.wizzer.mle.studio.dwp.reader.linux/os/linux/x86_64/libDwpReader.so

uninstall_plugin:
	$(RM) $(MLE_HOME)/Studio/plugins/com.wizzer.mle.studio.dwp.reader.linux/os/linux/x86_64/libDwpReader.so
