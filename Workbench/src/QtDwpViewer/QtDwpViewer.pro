QT += widgets
requires(qtConfig(treeview))

CONFIG += c++11

DEFINES += MLE_NOT_DLL DEBUG

INCLUDEPATH = $$PWD/../QtDwpModel/lib /opt/Magiclantern/include

HEADERS     =

RESOURCES   = qtdwpmodel.qrc

SOURCES     = main.cpp

LIBS += \
    -L/opt/MagicLantern/lib/mle/qt -lQtDwpModel \
    -L/opt/MagicLantern/lib -lDWP -lmlmath -lmlutil

# Default rules for deployment.
unix {
    target.path = /opt/MagicLantern/bin
}
!isEmpty(target.path): INSTALLS += target

copydata.commands = $(MKDIR) \"$$OUT_PWD/workprints\"; $(COPY_DIR) \"$$PWD/workprints\" \"$$OUT_PWD\"
first.depends = $(first) copydata
export(first.depends)
export(copydata.commands)
QMAKE_EXTRA_TARGETS += first copydata
