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

#include <QDebug>

#include "DwpDocumentContextMenu.h"

DwpDocumentContextMenu::DwpDocumentContextMenu(QObject *parent)
    : DwpContextMenu(parent),
      addIncludeAction(nullptr),
      addSetDefAction(nullptr),
      addActorDefAction(nullptr),
      addRoleDefAction(nullptr),
      addStageAction(nullptr),
      addSceneAction(nullptr),
      addGroupAction(nullptr),
      addMediaRefAction(nullptr),
      addBootAction(nullptr)
{
    // do nothing extra.
}

DwpDocumentContextMenu::~DwpDocumentContextMenu()
{
    if (addIncludeAction != nullptr) delete addIncludeAction;
    if (addSetDefAction != nullptr) delete addSetDefAction;
    if (addActorDefAction != nullptr) delete addActorDefAction;
    if (addRoleDefAction != nullptr) delete addRoleDefAction;
    if (addStageAction != nullptr) delete addStageAction;
    if (addSceneAction != nullptr) delete addSceneAction;
    if (addGroupAction != nullptr) delete addGroupAction;
    if (addMediaRefAction != nullptr) delete addMediaRefAction;
    if (addBootAction != nullptr) delete addBootAction;
}

void
DwpDocumentContextMenu::init(QtDwpAttribute *attr)
{
    // Call super class method.
    DwpContextMenu::init(attr);

    // Add menu actions.

    addIncludeAction = new QAction(tr("Add DWP Include Item"), this);
    //addIncludeAction->setShortcuts(QKeySequence::New);
    addIncludeAction->setStatusTip(tr("Create a new Include item"));
    connect(addIncludeAction, &QAction::triggered, this, &DwpDocumentContextMenu::addInclude);
    mMenu->addAction(addIncludeAction);
    //mMenu->addAction("Add DWP Include Item");

    addSetDefAction = new QAction(tr("Add DWP SetDef Item"), this);
    //addSetDefAction->setShortcuts(QKeySequence::New);
    addSetDefAction->setStatusTip(tr("Create a new SetDef item"));
    connect(addSetDefAction, &QAction::triggered, this, &DwpDocumentContextMenu::addSetDef);
    mMenu->addAction(addSetDefAction);
    //mMenu->addAction("Add DWP SetDef Item");

    addActorDefAction = new QAction(tr("Add DWP ActorDef Item"), this);
    //addActorDefAction->setShortcuts(QKeySequence::New);
    addActorDefAction->setStatusTip(tr("Create a new ActorDef item"));
    connect(addActorDefAction, &QAction::triggered, this, &DwpDocumentContextMenu::addActorDef);
    mMenu->addAction(addActorDefAction);
    //mMenu->addAction("Add DWP ActorDef Item");

    addRoleDefAction = new QAction(tr("Add DWP RoleDef Item"), this);
    //addRoleDefAction->setShortcuts(QKeySequence::New);
    addRoleDefAction->setStatusTip(tr("Create a new RoleDef item"));
    connect(addRoleDefAction, &QAction::triggered, this, &DwpDocumentContextMenu::addRoleDef);
    mMenu->addAction(addRoleDefAction);
    //mMenu->addAction("Add DWP RoleDef Item");

    addStageAction = new QAction(tr("Add DWP Stage Item"), this);
    //addStageAction->setShortcuts(QKeySequence::New);
    addStageAction->setStatusTip(tr("Create a new Stage item"));
    connect(addStageAction, &QAction::triggered, this, &DwpDocumentContextMenu::addStage);
    mMenu->addAction(addStageAction);
    //mMenu->addAction("Add DWP Stage Item");

    addSceneAction = new QAction(tr("Add DWP Scene Item"), this);
    //addSceneAction->setShortcuts(QKeySequence::New);
    addSceneAction->setStatusTip(tr("Create a new Scene item"));
    connect(addSceneAction, &QAction::triggered, this, &DwpDocumentContextMenu::addScene);
    mMenu->addAction(addSceneAction);
    //mMenu->addAction("Add DWP Scene Item");

    addGroupAction = new QAction(tr("Add DWP Group Item"), this);
    //addGroupAction->setShortcuts(QKeySequence::New);
    addGroupAction->setStatusTip(tr("Create a new Group item"));
    connect(addGroupAction, &QAction::triggered, this, &DwpDocumentContextMenu::addGroup);
    mMenu->addAction(addGroupAction);
    //mMenu->addAction("Add DWP Group Item");

    addMediaRefAction = new QAction(tr("Add DWP MediaRef Item"), this);
    //addMediaRefAction->setShortcuts(QKeySequence::New);
    addMediaRefAction->setStatusTip(tr("Create a new MediaRef item"));
    connect(addMediaRefAction, &QAction::triggered, this, &DwpDocumentContextMenu::addMediaRef);
    mMenu->addAction(addMediaRefAction);
    //mMenu->addAction("Add DWP MediaRef Item");

    addBootAction = new QAction(tr("Add DWP Boot Item"), this);
    //addBootAction->setShortcuts(QKeySequence::New);
    addBootAction->setStatusTip(tr("Create a new Boot item"));
    connect(addBootAction, &QAction::triggered, this, &DwpDocumentContextMenu::addBoot);
    mMenu->addAction(addBootAction);
    //mMenu->addAction("Add DWP Boot Item");
}

void
DwpDocumentContextMenu::addInclude()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP Include item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_INCLUDE, mAttr);
}

void
DwpDocumentContextMenu::addSetDef()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP SetDef item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_SETDEF, mAttr);
}

void
DwpDocumentContextMenu::addActorDef()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP ActorDef item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_ACTORDEF, mAttr);
}

void
DwpDocumentContextMenu::addRoleDef()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP RoleDef item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_ROLEDEF, mAttr);
}

void
DwpDocumentContextMenu::addStage()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP Stage item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_STAGE, mAttr);
}

void
DwpDocumentContextMenu::addScene()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP Scene item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_SCENE, mAttr);
}

void
DwpDocumentContextMenu::addGroup()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP Group item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_GROUP, mAttr);
}

void
DwpDocumentContextMenu::addMediaRef()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP MediaRef item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_MEDIAREF, mAttr);
}

void
DwpDocumentContextMenu::addBoot()
{
    qDebug() << "DwpDocumentContextMenu: Adding DWP Boot item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_BOOT, mAttr);
}
