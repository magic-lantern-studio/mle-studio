QT += widgets
requires(qtConfig(treeview))

CONFIG += c++11

DEFINES += MLE_NOT_DLL DEBUG QT_NO_CLIPBOARD

#INCLUDEPATH = $$PWD/../QtDwpModel/lib /opt/Magiclantern/include
INCLUDEPATH = /opt/Magiclantern/include

HEADERS = \
    DwpDocumentContextMenu.h \
    DwpTreeView.h \
    MainWindow.h

RESOURCES = qtdwpmodel.qrc

SOURCES = \
    DwpDocumentContextMenu.cpp \
    DwpTreeView.cpp \
    main.cpp \
    MainWindow.cpp

LIBS += \
    -L/opt/MagicLantern/lib/mle/qt -lQtDwpModel \
    -L/opt/MagicLantern/lib -lDWP -lmlmath -lmlutil

# Default rules for deployment.
unix {
    target.path = /opt/MagicLantern/bin
    headers.path = /opt/MagicLantern/include/qt
    headers.files = $$HEADERS
}
!isEmpty(target.path): INSTALLS += target headers

copydata.commands = $(MKDIR) \"$$OUT_PWD/workprints\"; $(COPY_DIR) \"$$PWD/workprints\" \"$$OUT_PWD\"
first.depends = $(first) copydata
export(first.depends)
export(copydata.commands)
QMAKE_EXTRA_TARGETS += first copydata
