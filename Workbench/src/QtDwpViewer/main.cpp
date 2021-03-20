// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2021 Wizzer Works
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
//  For information concerning this header file, contact Mark S. Millard,
//  of Wizzer Works at msm@wizzerworks.com.
//
//  More information concerning Wizzer Works may be found at
//
//      http://www.wizzerworks.com
//
// COPYRIGHT_END

// Include Magic Lantern header files.
#include "qt/QtDwpModel.h"
#include "mle/DwpItem.h"

// Include Qt header files.
#include <QApplication>
#include <QCommandLineParser>
#include <QCommandLineOption>
#include <QFile>

#include "MainWindow.h"

int main(int argc, char *argv[])
{
    Q_INIT_RESOURCE(qtdwpmodel);

    // Initialize DWP library.
    mleDwpInit();

    QApplication app(argc, argv);

    /*
    QTreeView view;
    view.setModel(model);
    view.setWindowTitle(QObject::tr("Digital Workprint Viewer"));
    //view.resizeColumnToContents(0);
    // Set visual elements of the QTreeView
    //qApp->setStyleSheet("QTreeView { alternate-background-color: yellow; }");
    view.show();
    */

    QCoreApplication::setOrganizationName("Wizzer Works");
    QCoreApplication::setOrganizationDomain("wizzerworks.com");
    QCoreApplication::setApplicationName("DWP Viewer");
    QCoreApplication::setApplicationVersion(QT_VERSION_STR);
    QCommandLineParser parser;
    parser.setApplicationDescription(QCoreApplication::applicationName() + ": Display the contents of a Digital Workprint.");
    parser.addHelpOption();
    parser.addVersionOption();
    parser.addPositionalArgument("file", "The file to open.");
    QCommandLineOption verboseOption(QString("verbose"), QString("Be verbose."));
    parser.addOption(verboseOption);
    QCommandLineOption javaOption(QString("java"), QString("Support Java and Android Digital Workprints."));
    parser.addOption(javaOption);
    parser.process(app);

    bool verbose = parser.isSet(verboseOption);
    bool java = parser.isSet(javaOption);

    MainWindow mainWin;
    if (! parser.positionalArguments().isEmpty())
        // The first positional argument contains the DWP filename.
        mainWin.loadFile(parser.positionalArguments().first());
    mainWin.show();

    QtDwpModel *model = mainWin.getModel();
    if ((verbose) && (model != nullptr)) {
        QtDwpTreeItem *root = model->getRoot();
        root->traverse(QtDwpAttribute::dump, root, nullptr);
    }

    if (java) mainWin.setJavaDwp(true);
    else mainWin.setJavaDwp(false);

    return app.exec();
}
