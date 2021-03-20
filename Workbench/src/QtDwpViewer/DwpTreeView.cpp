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

// Include Qt header files.
#include <QMouseEvent>
#include <QMenu>
#include <QTreeWidgetItem>
#include <QDebug>

// Include Digital Workprint header files.
#include "mle/DwpActor.h"
#include "mle/DwpActorDef.h"
#include "mle/DwpGroup.h"
#include "mle/DwpItem.h"
#include "mle/DwpInclude.h"
#include "mle/DwpMediaRef.h"
#include "mle/DwpMediaRefSource.h"
#include "mle/DwpMediaRefTarget.h"
#include "mle/DwpStage.h"
#include "mle/DwpSet.h"

// Include Magic Lantern Workbench header files.
#include "DwpTreeView.h"
#include "DwpActorContextMenu.h"
#include "DwpActorDefContextMenu.h"
#include "DwpDocumentContextMenu.h"
#include "DwpGroupContextMenu.h"
#include "DwpMediaRefContextMenu.h"
#include "DwpMediaRefSourceContextMenu.h"
#include "DwpMediaRefTargetContextMenu.h"
#include "DwpStageContextMenu.h"
#include "DwpSetContextMenu.h"
#include "qt/QtDwpModel.h"
#include "qt/QtDwpAttribute.h"


DwpTreeView:: DwpTreeView()
    : QTreeView(),
      mUseJava(false)
{
    // Do nothing extra.
}

DwpTreeView::~DwpTreeView()
{
    // Do nothing.
}

QtDwpTreeItem *
DwpTreeView::itemAt(const QPoint &pos) const
{
    QModelIndex index = indexAt(pos);
    if (! index.isValid())
        return nullptr;

    QtDwpModel *model = static_cast<QtDwpModel *>(this->model());

    QtDwpTreeItem *item = nullptr;
    if (model)
        item = model->getItem(index);
    return item;
}

void
DwpTreeView::contextMenuEvent(QContextMenuEvent *e)
{
    QtDwpAttribute *attr = static_cast<QtDwpAttribute *>(itemAt(e->pos()));
    if (attr == nullptr)
        return;
    qDebug() << "Attribute" << attr->getAttributeName();

    const MleDwpItem *dwpItem = attr->getDwpItem();
    if (dwpItem->isa(MleDwpStage::typeId)) {
        DwpStageContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } if (dwpItem->isa(MleDwpSet::typeId)) {
        DwpSetContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } if (dwpItem->isa(MleDwpActor::typeId)) {
        DwpActorContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } if (dwpItem->isa(MleDwpActorDef::typeId)) {
        DwpActorDefContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } if (dwpItem->isa(MleDwpGroup::typeId)) {
        DwpGroupContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } else if (dwpItem->isa(MleDwpInclude::typeId)) {
        DwpDocumentContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } else if (dwpItem->isa(MleDwpMediaRefSource::typeId)) {
        DwpMediaRefSourceContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } else if (dwpItem->isa(MleDwpMediaRefTarget::typeId)) {
        DwpMediaRefTargetContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    } else if (dwpItem->isa(MleDwpMediaRef::typeId)) {
        DwpMediaRefContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init();

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        if (selected) {
            qDebug() << "Selected" << selected->text();
        }
    }
}
