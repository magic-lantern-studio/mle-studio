QT += widgets

TEMPLATE = lib
TARGET = QtDwpModel
DEFINES += QTDWPMODEL_LIBRARY

CONFIG += c++11

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

DEFINES += MLE_NOT_DLL DEBUG

INCLUDEPATH = /opt/MagicLantern/include

SOURCES += \
    QtDwpModel.cpp \
    QtDwpNameAttribute.cpp \
    QtDwpNameTypeAttribute.cpp \
    QtDwpNameTypeValueAttribute.cpp \
    QtDwpTreeItem.cpp \
    QtDwpAttribute.cpp

HEADERS += \
    QtDwpModel_global.h \
    QtDwpModel.h \
    QtDwpNameAttribute.h \
    QtDwpNameTypeAttribute.h \
    QtDwpNameTypeValueAttribute.h \
    QtDwpTreeItem.h \
    QtDwpAttribute.h

LIBS += -L/opt/MagicLantern/lib -lDWP -lmlmath -lmlutil

# Default rules for deployment.
unix {
    target.path = /opt/MagicLantern/lib/mle/qt
}
!isEmpty(target.path): INSTALLS += target
