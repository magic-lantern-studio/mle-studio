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
#include "mle/DwpHeaderFile.h"
#include "mle/DwpItem.h"
#include "mle/DwpInclude.h"
#include "mle/DwpMediaRef.h"
#include "mle/DwpMediaRefSource.h"
#include "mle/DwpMediaRefTarget.h"
#include "mle/DwpPackage.h"
#include "mle/DwpProperty.h"
#include "mle/DwpPropertyDef.h"
#include "mle/DwpRoleDef.h"
#include "mle/DwpScene.h"
#include "mle/DwpSourceFile.h"
#include "mle/DwpStage.h"
#include "mle/DwpSet.h"
#include "mle/DwpSetDef.h"

// Include Magic Lantern Workbench header files.
#include "DwpTreeView.h"
#include "DwpActorContextMenu.h"
#include "DwpActorDefContextMenu.h"
#include "DwpDocumentContextMenu.h"
#include "DwpGroupContextMenu.h"
#include "DwpMediaRefContextMenu.h"
#include "DwpMediaRefSourceContextMenu.h"
#include "DwpMediaRefTargetContextMenu.h"
#include "DwpPropertyDefContextMenu.h"
#include "DwpRoleDefContextMenu.h"
#include "DwpSceneContextMenu.h"
#include "DwpStageContextMenu.h"
#include "DwpSetContextMenu.h"
#include "DwpSetDefContextMenu.h"
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
    if (attr == nullptr) {
        // No attribute is associated with the event. Therfore, display top-level
        // document context menu.
        DwpDocumentContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        return;
    }
    qDebug() << "DwpTreeView: Attribute" << attr->getAttributeName();

    const MleDwpItem *dwpItem = attr->getDwpItem();
    if (dwpItem->isa(MleDwpStage::typeId)) {
        DwpStageContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "DwpTreeView: Selected" << selected->text();
        //    emit DwpTreeView::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_HEADERFILE, e->pos());
        //}
    } if (dwpItem->isa(MleDwpSet::typeId)) {
        DwpSetContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } if (dwpItem->isa(MleDwpActor::typeId)) {
        DwpActorContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } if (dwpItem->isa(MleDwpActorDef::typeId)) {
        DwpActorDefContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } if (dwpItem->isa(MleDwpGroup::typeId)) {
        DwpGroupContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpMediaRefSource::typeId)) {
        DwpMediaRefSourceContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpMediaRefTarget::typeId)) {
        DwpMediaRefTargetContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpMediaRef::typeId)) {
        DwpMediaRefContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpPropertyDef::typeId)) {
        DwpPropertyDefContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpRoleDef::typeId)) {
        DwpRoleDefContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpScene::typeId)) {
        DwpSceneContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpSetDef::typeId)) {
        DwpSetDefContextMenu context;
        if (mUseJava) context.useJava(true);
        else context.useJava(false);
        context.init(attr);
        connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
        connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

        QMenu *m = context.getMenu();
        QAction *selected = m->exec(mapToGlobal(e->pos()));
        //if (selected) {
        //    qDebug() << "Selected" << selected->text();
        //}
    } else if (dwpItem->isa(MleDwpInclude::typeId))  {
        displayDefaultContextMenu(attr, e->pos());
    } else if (dwpItem->isa(MleDwpHeaderFile::typeId))  {
        displayDefaultContextMenu(attr, e->pos());
    } else if (dwpItem->isa(MleDwpSourceFile::typeId))  {
        displayDefaultContextMenu(attr, e->pos());
    } else if (dwpItem->isa(MleDwpPackage::typeId))  {
       displayDefaultContextMenu(attr, e->pos());
    } else if (dwpItem->isa(MleDwpProperty::typeId))  {
        displayDefaultContextMenu(attr, e->pos());
    }
}

void
DwpTreeView::addAttribute(const QtDwpAttribute::AttributeType type, QtDwpAttribute *attr)
{
    qDebug() << "DwpTreeView: Adding DWP Item of type" << type;

    QtDwpModel *model = static_cast<QtDwpModel *>(this->model());
    QtDwpAttribute *newAttr = model->addAttribute(type, attr);
    if (newAttr != nullptr) {
    }
}

void
DwpTreeView::deleteAttribute(QtDwpAttribute *attr)
{
    qDebug() << "DwpTreeView: Deleting DWP Item";
}

void
DwpTreeView::displayDefaultContextMenu(QtDwpAttribute *attr, QPoint pos)
{
    DwpContextMenu context;
    if (mUseJava) context.useJava(true);
    else context.useJava(false);
    context.init(attr);
    connect(&context, &DwpContextMenu::insertAttribute, this, &DwpTreeView::addAttribute);
    connect(&context, &DwpContextMenu::deleteAttribute, this, &DwpTreeView::deleteAttribute);

    QMenu *m = context.getMenu();
    QAction *selected = m->exec(mapToGlobal(pos));
    //if (selected) {
    //    qDebug() << "Selected" << selected->text();
    //}
}
