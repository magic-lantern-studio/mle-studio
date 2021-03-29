// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2020-2021 Wizzer Works
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
// COPYRIGHT_END

#include <QtWidgets>

// Include Magic Lantern header files.
#include "qt/QtDwpModel.h"
#include "mle/DwpItem.h"
#include "mle/DwpLoad.h"

#include "MainWindow.h"


MainWindow::MainWindow()
    : mTreeView(new DwpTreeView),
      mModel(nullptr),
      mUseJava(false)
{
    QStringList headers;

    // Set the tree view as the main widget.
    setCentralWidget(mTreeView);

    // Setup the tool and status bar.
    createActions();
    createStatusBar();

    readSettings();

    connect(this->getModel(), &QtDwpModel::contentsChanged,
            this, &MainWindow::documentWasModified);

#ifndef QT_NO_SESSIONMANAGER
    QGuiApplication::setFallbackSessionManagementEnabled(false);
    connect(qApp, &QGuiApplication::commitDataRequest,
            this, &MainWindow::commitData);
#endif // !QT_NO_SESSIONMANAGER

    // Load the Digital Workprint.
    //QFile file(":/workprints/null.dwp");
    //QString filename = file.fileName();
    //MleDwpItem *root = mlLoadWorkprint(filename.toStdString().c_str());
    MleDwpItem *root = mlLoadWorkprint("workprints/null.dwp");

    // Create the default model for the viewer.
    headers.append("DWP Item");
    headers.append("Name");
    headers.append("Type");
    headers.append("Value");
    QtDwpModel *model = new QtDwpModel(headers, root);
    mModel = model;

    // Set model on tree view.
    mTreeView->setModel(model);
    // Set minimal column size to 150 pixels.
    mTreeView->header()->setMinimumSectionSize(150);
    // Install event filter for processing mouse events.
    mTreeView->viewport()->installEventFilter(this);

    setCurrentFile(QString("workprints/null.dwp"));
    setUnifiedTitleAndToolBarOnMac(true);
}

void
MainWindow::setJavaDwp(bool value)
{
    mUseJava = value;
    if (mTreeView != nullptr)
        mTreeView->useJava(mUseJava);
}

void
MainWindow::closeEvent(QCloseEvent *event)
{
    if (maybeSave()) {
        writeSettings();
        event->accept();
    } else {
        event->ignore();
    }
}

/*
bool
MainWindow::eventFilter(QObject *target, QEvent *event)
{
    QTreeView *view = qobject_cast<QTreeView *>(target);
    QMouseEvent *k = static_cast<QMouseEvent *>(event);

    if (k && k->type() == QEvent::MouseButtonPress) {
        if ((k->button()) == Qt::LeftButton) {
            qDebug()<<"QTreeView event: left button.";
        } else if ((k->button()) == Qt::RightButton) {
            qDebug()<<"QTreeView event: right button.";
        }
    }

    return QObject::eventFilter(target, event);
}
*/

void
MainWindow::newFile()
{
    if (maybeSave()) {
        //mTextEdit->clear();
        // Todo: Clear mTreeView (set to null.dwp).
        setCurrentFile(QString());
    }
}

void
MainWindow::open()
{
    if (maybeSave()) {
        QString fileName = QFileDialog::getOpenFileName(this);
        if (!fileName.isEmpty())
            loadFile(fileName);
    }
}

bool
MainWindow::save()
{
    if (mCurFile.isEmpty()) {
        return saveAs();
    } else {
        return saveFile(mCurFile);
    }
}

bool
MainWindow::saveAs()
{
    QFileDialog dialog(this);
    dialog.setWindowModality(Qt::WindowModal);
    dialog.setAcceptMode(QFileDialog::AcceptSave);
    if (dialog.exec() != QDialog::Accepted)
        return false;
    return saveFile(dialog.selectedFiles().first());
}

void
MainWindow::about()
{
   QMessageBox::about(this, tr("About DWP Viewer"),
       tr("The <b>Dwp Viewer</b> may be used to view "
          "and edit a Magic Lantern Digital Workprint."));
}

void
MainWindow::documentWasModified()
{
    // Flag that DWP is modified.
    setWindowModified(this->getModel()->isModified());
}

void
MainWindow::createActions()
{
    QMenu *fileMenu = menuBar()->addMenu(tr("&File"));
    QToolBar *fileToolBar = addToolBar(tr("File"));
    const QIcon newIcon = QIcon::fromTheme("document-new", QIcon(":/images/new.png"));
    QAction *newAct = new QAction(newIcon, tr("&New"), this);
    newAct->setShortcuts(QKeySequence::New);
    newAct->setStatusTip(tr("Create a new file"));
    connect(newAct, &QAction::triggered, this, &MainWindow::newFile);
    fileMenu->addAction(newAct);
    fileToolBar->addAction(newAct);

    const QIcon openIcon = QIcon::fromTheme("document-open", QIcon(":/images/open.png"));
    QAction *openAct = new QAction(openIcon, tr("&Open..."), this);
    openAct->setShortcuts(QKeySequence::Open);
    openAct->setStatusTip(tr("Open an existing file"));
    connect(openAct, &QAction::triggered, this, &MainWindow::open);
    fileMenu->addAction(openAct);
    fileToolBar->addAction(openAct);

    const QIcon saveIcon = QIcon::fromTheme("document-save", QIcon(":/images/save.png"));
    QAction *saveAct = new QAction(saveIcon, tr("&Save"), this);
    saveAct->setShortcuts(QKeySequence::Save);
    saveAct->setStatusTip(tr("Save the document to disk"));
    connect(saveAct, &QAction::triggered, this, &MainWindow::save);
    fileMenu->addAction(saveAct);
    fileToolBar->addAction(saveAct);

    const QIcon saveAsIcon = QIcon::fromTheme("document-save-as");
    QAction *saveAsAct = fileMenu->addAction(saveAsIcon, tr("Save &As..."), this, &MainWindow::saveAs);
    saveAsAct->setShortcuts(QKeySequence::SaveAs);
    saveAsAct->setStatusTip(tr("Save the document under a new name"));

    fileMenu->addSeparator();

    const QIcon exitIcon = QIcon::fromTheme("application-exit");
    QAction *exitAct = fileMenu->addAction(exitIcon, tr("E&xit"), this, &QWidget::close);
    exitAct->setShortcuts(QKeySequence::Quit);
    exitAct->setStatusTip(tr("Exit the application"));

    QMenu *editMenu = menuBar()->addMenu(tr("&Edit"));
    QToolBar *editToolBar = addToolBar(tr("Edit"));

#ifndef QT_NO_CLIPBOARD
    const QIcon cutIcon = QIcon::fromTheme("edit-cut", QIcon(":/images/cut.png"));
    QAction *cutAct = new QAction(cutIcon, tr("Cu&t"), this);
    cutAct->setShortcuts(QKeySequence::Cut);
    cutAct->setStatusTip(tr("Cut the current selection's contents to the "
                            "clipboard"));
    connect(cutAct, &QAction::triggered, mTextEdit, &QPlainTextEdit::cut);
    editMenu->addAction(cutAct);
    editToolBar->addAction(cutAct);

    const QIcon copyIcon = QIcon::fromTheme("edit-copy", QIcon(":/images/copy.png"));
    QAction *copyAct = new QAction(copyIcon, tr("&Copy"), this);
    copyAct->setShortcuts(QKeySequence::Copy);
    copyAct->setStatusTip(tr("Copy the current selection's contents to the "
                             "clipboard"));
    connect(copyAct, &QAction::triggered, mTextEdit, &QPlainTextEdit::copy);
    editMenu->addAction(copyAct);
    editToolBar->addAction(copyAct);

    const QIcon pasteIcon = QIcon::fromTheme("edit-paste", QIcon(":/images/paste.png"));
    QAction *pasteAct = new QAction(pasteIcon, tr("&Paste"), this);
    pasteAct->setShortcuts(QKeySequence::Paste);
    pasteAct->setStatusTip(tr("Paste the clipboard's contents into the current "
                              "selection"));
    connect(pasteAct, &QAction::triggered, mTextEdit, &QPlainTextEdit::paste);
    editMenu->addAction(pasteAct);
    editToolBar->addAction(pasteAct);

    menuBar()->addSeparator();
#endif // !QT_NO_CLIPBOARD

    QMenu *helpMenu = menuBar()->addMenu(tr("&Help"));
    QAction *aboutAct = helpMenu->addAction(tr("&About"), this, &MainWindow::about);
    aboutAct->setStatusTip(tr("Show the application's About box"));

    QAction *aboutQtAct = helpMenu->addAction(tr("About &Qt"), qApp, &QApplication::aboutQt);
    aboutQtAct->setStatusTip(tr("Show the Qt library's About box"));

#ifndef QT_NO_CLIPBOARD
    cutAct->setEnabled(false);
    copyAct->setEnabled(false);
    connect(mTextEdit, &QPlainTextEdit::copyAvailable, cutAct, &QAction::setEnabled);
    connect(mTextEdit, &QPlainTextEdit::copyAvailable, copyAct, &QAction::setEnabled);
#endif // !QT_NO_CLIPBOARD
}

void
MainWindow::createStatusBar()
{
    statusBar()->showMessage(tr("Ready"));
}

void
MainWindow::readSettings()
{
    QSettings settings(QCoreApplication::organizationName(), QCoreApplication::applicationName());
    const QByteArray geometry = settings.value("geometry", QByteArray()).toByteArray();
    if (geometry.isEmpty()) {
        const QRect availableGeometry = screen()->availableGeometry();
        resize(availableGeometry.width() / 3, availableGeometry.height() / 2);
        move((availableGeometry.width() - width()) / 2,
             (availableGeometry.height() - height()) / 2);
    } else {
        restoreGeometry(geometry);
    }
}

void
MainWindow::writeSettings()
{
    QSettings settings(QCoreApplication::organizationName(), QCoreApplication::applicationName());
    settings.setValue("geometry", saveGeometry());
}

bool
MainWindow::maybeSave()
{
    // Check if DWP document is modified. If not, return true.
    if (! this->getModel()->isModified())
        return true;

    const QMessageBox::StandardButton ret
        = QMessageBox::warning(this, tr("DWP Viewer"),
                               tr("The document has been modified.\n"
                                  "Do you want to save your changes?"),
                               QMessageBox::Save | QMessageBox::Discard | QMessageBox::Cancel);
    switch (ret) {
      case QMessageBox::Save:
        return save();
      case QMessageBox::Cancel:
        return false;
      default:
        break;
    }
    return true;
}

void
MainWindow::loadFile(const QString &fileName)
{
    QFile file(fileName);
    if (! file.open(QFile::ReadOnly | QFile::Text)) {
        QMessageBox::warning(this, tr("Application"),
                             tr("Cannot read file %1:\n%2.")
                             .arg(QDir::toNativeSeparators(fileName), file.errorString()));
        return;
    }

    QTextStream in(&file);
#ifndef QT_NO_CURSOR
    QGuiApplication::setOverrideCursor(Qt::WaitCursor);
#endif
    //mTextEdit->setPlainText(in.readAll());
    // Todo: load Digital Workprint.
#ifndef QT_NO_CURSOR
    QGuiApplication::restoreOverrideCursor();
#endif

    setCurrentFile(fileName);
    statusBar()->showMessage(tr("File loaded"), 2000);
}

bool
MainWindow::saveFile(const QString &fileName)
{
    QString errorMessage;

    QGuiApplication::setOverrideCursor(Qt::WaitCursor);
    QSaveFile file(fileName);
    if (file.open(QFile::WriteOnly | QFile::Text)) {
        QTextStream out(&file);
        //out << mTextEdit->toPlainText();
        // Todo: save Digital Workprint to file.
        if (! file.commit()) {
            errorMessage = tr("Cannot write file %1:\n%2.")
                           .arg(QDir::toNativeSeparators(fileName), file.errorString());
        }
    } else {
        errorMessage = tr("Cannot open file %1 for writing:\n%2.")
                       .arg(QDir::toNativeSeparators(fileName), file.errorString());
    }
    QGuiApplication::restoreOverrideCursor();

    if (! errorMessage.isEmpty()) {
        QMessageBox::warning(this, tr("Application"), errorMessage);
        return false;
    }

    setCurrentFile(fileName);
    statusBar()->showMessage(tr("File saved"), 2000);
    return true;
}

void
MainWindow::setCurrentFile(const QString &fileName)
{
    mCurFile = fileName;
    // Set DWP modified to false.
    this->getModel()->setModified(false);
    setWindowModified(false);

    QString shownName = mCurFile;
    if (mCurFile.isEmpty())
        shownName = "untitled.txt";
    setWindowFilePath(shownName);
}

QString
MainWindow::strippedName(const QString &fullFileName)
{
    return QFileInfo(fullFileName).fileName();
}

#ifndef QT_NO_SESSIONMANAGER
void
MainWindow::commitData(QSessionManager &manager)
{
    if (manager.allowsInteraction()) {
        if (! maybeSave())
            manager.cancel();
    } else {
        // Non-interactive: save without asking.

        // Save DWP if it is modifiled.
        if (this->getModel()->isModified())
            save();
    }
}
#endif // !QT_NO_SESSIONMANAGER
