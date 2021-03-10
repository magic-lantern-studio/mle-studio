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
#include "QtDwpModel.h"
#include "mle/DwpItem.h"
#include "mle/DwpLoad.h"

// Include Qt header files.
#include <QApplication>
#include <QFile>
#include <QTreeView>

int main(int argc, char *argv[])
{
    QStringList headers;

    Q_INIT_RESOURCE(qtdwpmodel);

    // Initialize DWP library.
    mleDwpInit();

    QApplication app(argc, argv);

    // Load the Digita Workprint.
    QFile file(":/workprints/null.dwp");
    QString filename = file.fileName();
    //MleDwpItem *root = mlLoadWorkprint(filename.toStdString().c_str());
    MleDwpItem *root = mlLoadWorkprint("workprints/null.dwp");

    // Create the model for the viewer.
    headers.append("DWP Item");
    headers.append("Name");
    headers.append("Type");
    headers.append("Value");
    QtDwpModel *model = new QtDwpModel(headers, root);

    QTreeView view;
    view.setModel(model);
    view.setWindowTitle(QObject::tr("Digital Workprint Viewer"));
    view.show();

    return app.exec();
}
