// COPYRIGHT_BEGIN
// COPYRIGHT_END

#ifndef __MAINWINDOW_H_
#define __MAINWINDOW_H_

#include <QMainWindow>

#include "qt/QtDwpModel.h"

QT_BEGIN_NAMESPACE
class QAction;
class QMenu;
class QTreeView;
class QSessionManager;
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

  public:
    MainWindow();

    void loadFile(const QString &fileName);

    QtDwpModel *getModel()
    { return mModel; }

  protected:
    void closeEvent(QCloseEvent *event) override;

  private slots:
    void newFile();
    void open();
    bool save();
    bool saveAs();
    void about();
    void documentWasModified();
#ifndef QT_NO_SESSIONMANAGER
    void commitData(QSessionManager &);
#endif

  private:
    void createActions();
    void createStatusBar();
    void readSettings();
    void writeSettings();
    bool maybeSave();
    bool saveFile(const QString &fileName);
    void setCurrentFile(const QString &fileName);
    QString strippedName(const QString &fullFileName);

    QTreeView *mTreeView;
    QString mCurFile;

    // The model loaded from the Digital Workprint.
    QtDwpModel *mModel;
};

#endif // __MAINWINDOW_H_
