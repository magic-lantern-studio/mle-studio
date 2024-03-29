QT += widgets
requires(qtConfig(treeview))

CONFIG += c++11

DEFINES += MLE_NOT_DLL DEBUG QT_NO_CLIPBOARD

#INCLUDEPATH = $$PWD/../QtDwpModel/lib /opt/MagicLantern/include
INCLUDEPATH = /opt/MagicLantern/include

HEADERS = \
    DwpActorContextMenu.h \
    DwpActorDefContextMenu.h \
    DwpContextMenu.h \
    DwpDocumentContextMenu.h \
    DwpGroupContextMenu.h \
    DwpMediaRefContextMenu.h \
    DwpMediaRefSourceContextMenu.h \
    DwpMediaRefTargetContextMenu.h \
    DwpPropertyDefContextMenu.h \
    DwpRoleDefContextMenu.h \
    DwpSceneContextMenu.h \
    DwpSetContextMenu.h \
    DwpSetDefContextMenu.h \
    DwpStageContextMenu.h \
    DwpTreeView.h \
    MainWindow.h

RESOURCES = qtdwpmodel.qrc

SOURCES = \
    DwpActorContextMenu.cpp \
    DwpActorDefContextMenu.cpp \
    DwpContextMenu.cpp \
    DwpDocumentContextMenu.cpp \
    DwpGroupContextMenu.cpp \
    DwpMediaRefContextMenu.cpp \
    DwpMediaRefSourceContextMenu.cpp \
    DwpMediaRefTargetContextMenu.cpp \
    DwpPropertyDefContextMenu.cpp \
    DwpRoleDefContextMenu.cpp \
    DwpSceneContextMenu.cpp \
    DwpSetContextMenu.cpp \
    DwpSetDefContextMenu.cpp \
    DwpStageContextMenu.cpp \
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
