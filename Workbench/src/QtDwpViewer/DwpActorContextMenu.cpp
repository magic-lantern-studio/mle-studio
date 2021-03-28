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

#include "DwpActorContextMenu.h"

DwpActorContextMenu::DwpActorContextMenu(QObject *parent)
    : DwpContextMenu(parent),
      addHeaderFileAction(nullptr),
      addSourceFileAction(nullptr),
      addPackageAction(nullptr),
      addRoleBindingAction(nullptr),
      addPropertyAction(nullptr)
{
    // Do nothing extra.
}

DwpActorContextMenu::~DwpActorContextMenu()
{
    if (addHeaderFileAction != nullptr) delete addHeaderFileAction;
    if (addSourceFileAction != nullptr) delete addSourceFileAction;
    if (addPackageAction != nullptr) delete addPackageAction;
    if (addRoleBindingAction != nullptr) delete addRoleBindingAction;
    if (addPropertyAction != nullptr) delete addPropertyAction;
}

void
DwpActorContextMenu::init(QtDwpAttribute *attr)
{
    // Call super class method.
    DwpContextMenu::init(attr);

    if (mUseJava) {
        // Support for Java and Android Digital Workprints.

        addPackageAction = new QAction(tr("Add DWP Package Item"), this);
        //addPackageAction->setShortcuts(QKeySequence::New);
        addPackageAction->setStatusTip(tr("Create a new Package item"));
        connect(addPackageAction, &QAction::triggered, this, &DwpActorContextMenu::addPackage);
        mMenu->addAction(addPackageAction);
        //mMenu->addAction("Add DWP Package Item");
    } else {
        // Support for C/C++ Digital Workprints.

        addHeaderFileAction = new QAction(tr("Add DWP HeaderFile Item"), this);
        //addHeaderFileAction->setShortcuts(QKeySequence::New);
        addHeaderFileAction->setStatusTip(tr("Create a new HeaderFile item"));
        connect(addHeaderFileAction, &QAction::triggered, this, &DwpActorContextMenu::addHeaderFile);
        mMenu->addAction(addHeaderFileAction);
        //mMenu->addAction("Add DWP HeaderFile Item");

        addSourceFileAction = new QAction(tr("Add DWP SourceFile Item"), this);
        //action->setShortcuts(QKeySequence::New);
        addSourceFileAction->setStatusTip(tr("Create a new SourceFile item"));
        connect(addSourceFileAction, &QAction::triggered, this, &DwpActorContextMenu::addSourceFile);
        mMenu->addAction(addSourceFileAction);
        //mMenu->addAction("Add DWP SourceFile Item");
    }

    addRoleBindingAction = new QAction(tr("Add DWP RoleBinding Item"), this);
    //addRoleBindingAction->setShortcuts(QKeySequence::New);
    addRoleBindingAction->setStatusTip(tr("Create a new RoleBinding item"));
    connect(addRoleBindingAction, &QAction::triggered, this, &DwpActorContextMenu::addRoleBinding);
    mMenu->addAction(addRoleBindingAction);
    //mMenu->addAction("Add DWP RoleBinding Item");

    addPropertyAction = new QAction(tr("Add DWP Property Item"), this);
    //addPropertyAction->setShortcuts(QKeySequence::New);
    addPropertyAction->setStatusTip(tr("Create a new Property item"));
    connect(addPropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addProperty);
    mMenu->addAction(addPropertyAction);
    //mMenu->addAction("Add DWP Property Item");
}

void
DwpActorContextMenu::addHeaderFile()
{
    qDebug() << "DwpActorContextMenu: Adding DWP HeaderFile item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_HEADERFILE, mAttr);
}

void
DwpActorContextMenu::addSourceFile()
{
    qDebug() << "DwpActorContextMenu: Adding DWP SourceFile item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_SOURCEFILE, mAttr);
}

void
DwpActorContextMenu::addPackage()
{
    qDebug() << "DwpActorContextMenu: Adding DWP Package item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_PACKAGE, mAttr);
}

void
DwpActorContextMenu::addRoleBinding()
{
    qDebug() << "DwpActorContextMenu: Adding DWP RoleBinding item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_ROLEBINDING, mAttr);
}

void
DwpActorContextMenu::addProperty()
{
    qDebug() << "DwpActorContextMenu: Adding DWP Property item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr);
}
