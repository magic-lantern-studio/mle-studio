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

#include "DwpGroupContextMenu.h"

DwpGroupContextMenu::DwpGroupContextMenu(QObject *parent)
    : DwpContextMenu(parent),
    addHeaderFileAction(nullptr),
    addSourceFileAction(nullptr),
    addPackageAction(nullptr),
    addActorAction(nullptr),
    addRoleAttachmentAction(nullptr)
{
    // Do nothing extra.
}

DwpGroupContextMenu::~DwpGroupContextMenu()
{
    if (addHeaderFileAction != nullptr) delete addHeaderFileAction;
    if (addSourceFileAction != nullptr) delete addSourceFileAction;
    if (addPackageAction != nullptr) delete addPackageAction;
    if (addActorAction != nullptr) delete addActorAction;
    if (addRoleAttachmentAction != nullptr) delete addRoleAttachmentAction;
}

void
DwpGroupContextMenu::init(QtDwpAttribute *attr)
{
    // Call super class method.
    DwpContextMenu::init(attr);

    if (mUseJava) {
        // Support for Java and Android Digital Workprints.

        addPackageAction = new QAction(tr("Add DWP Package Item"), this);
        //addPackageAction->setShortcuts(QKeySequence::New);
        addPackageAction->setStatusTip(tr("Create a new Package item"));
        connect(addPackageAction, &QAction::triggered, this, &DwpGroupContextMenu::addPackage);
        mMenu->addAction(addPackageAction);
        //mMenu->addAction("Add DWP Package Item");
        if (attr->hasAttributeType(QtDwpAttribute::DWP_ATTRIBUTE_PACKAGE))
            addPackageAction->setEnabled(false);
    } else {
        // Support for C/C++ Digital Workprints.

        addHeaderFileAction = new QAction(tr("Add DWP HeaderFile Item"), this);
        //addHeaderFileAction->setShortcuts(QKeySequence::New);
        addHeaderFileAction->setStatusTip(tr("Create a new HeaderFile item"));
        connect(addHeaderFileAction, &QAction::triggered, this, &DwpGroupContextMenu::addHeaderFile);
        mMenu->addAction(addHeaderFileAction);
        //mMenu->addAction("Add DWP HeaderFile Item");
        if (attr->hasAttributeType(QtDwpAttribute::DWP_ATTRIBUTE_HEADERFILE))
            addHeaderFileAction->setEnabled(false);

        addSourceFileAction = new QAction(tr("Add DWP SourceFile Item"), this);
        //action->setShortcuts(QKeySequence::New);
        addSourceFileAction->setStatusTip(tr("Create a new SourceFile item"));
        connect(addSourceFileAction, &QAction::triggered, this, &DwpGroupContextMenu::addSourceFile);
        mMenu->addAction(addSourceFileAction);
        //mMenu->addAction("Add DWP SourceFile Item");
        if (attr->hasAttributeType(QtDwpAttribute::DWP_ATTRIBUTE_SOURCEFILE))
            addSourceFileAction->setEnabled(false);
    }

    addActorAction = new QAction(tr("Add DWP Actor Item"), this);
    //addActorAction->setShortcuts(QKeySequence::New);
    addActorAction->setStatusTip(tr("Create a new Actor item"));
    connect(addActorAction, &QAction::triggered, this, &DwpGroupContextMenu::addActor);
    mMenu->addAction(addActorAction);
    //mMenu->addAction("Add DWP Actor Item");

    addRoleAttachmentAction = new QAction(tr("Add DWP RoleAttachment Item"), this);
    //addRoleAttachmentAction->setShortcuts(QKeySequence::New);
    addRoleAttachmentAction->setStatusTip(tr("Create a new RoleAttachment item"));
    connect(addRoleAttachmentAction, &QAction::triggered, this, &DwpGroupContextMenu::addRoleAttachment);
    mMenu->addAction(addRoleAttachmentAction);
    //mMenu->addAction("Add DWP RoleAttachment Item");
}


void
DwpGroupContextMenu::addHeaderFile()
{
    qDebug() << "DwpGroupContextMenu: Adding DWP HeaderFile item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_HEADERFILE, mAttr);
}

void
DwpGroupContextMenu::enableHeaderFile(bool flag)
{
    qDebug() << "DwpGroupContextMenu: Setting DWP HeaderFile menu visibility";
    addHeaderFileAction->setEnabled(flag);
}

void
DwpGroupContextMenu::addSourceFile()
{
    qDebug() << "DwpGroupContextMenu: Adding DWP SourceFile item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_SOURCEFILE, mAttr);
}

void
DwpGroupContextMenu::enableSourceFile(bool flag)
{
    qDebug() << "DwpGroupContextMenu: Setting DWP SourceFile menu visibility";
    addSourceFileAction->setEnabled(flag);
}

void
DwpGroupContextMenu::addPackage()
{
    qDebug() << "DwpGroupContextMenu: Adding DWP Package item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_PACKAGE, mAttr);
}

void
DwpGroupContextMenu::addActor()
{
    qDebug() << "DwpGroupContextMenu: Adding DWP Actor item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_ACTOR, mAttr);
}

void
DwpGroupContextMenu::addRoleAttachment()
{
    qDebug() << "DwpGroupContextMenu: Adding DWP RoleAttachment item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_ROLEATTACHMENT, mAttr);
}
