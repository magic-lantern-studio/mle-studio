include(gtest_dependency.pri)

TEMPLATE = app

CONFIG += console c++11
CONFIG -= app_bundle
CONFIG += thread
CONFIG += widget

DEFINES += MLE_NOT_DLL DEBUG

INCLUDEPATH += $$PWD/../../lib \
    /opt/MagicLantern/include

HEADERS += \
        libdwptest.h \
        tst_createmodel.h \
        tst_datatypes.h

SOURCES += \
        libdwptest.cpp \
        main.cpp

LIBS += -L$$OUT_PWD/../../lib -lQtDwpModel \
    -L/opt/MagicLantern/lib -lDWP -lmlmath -lmlutil
