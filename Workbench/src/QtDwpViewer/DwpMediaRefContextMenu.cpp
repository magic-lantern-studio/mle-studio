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

#include "DwpMediaRefContextMenu.h"

DwpMediaRefContextMenu::DwpMediaRefContextMenu(QObject *parent)
    : DwpContextMenu(parent),
      addPackageAction(nullptr),
      addMediaRefSourceAction(nullptr),
      addMediaRefTargetActionAction(nullptr)
{
    // Do nothing extra.
}

DwpMediaRefContextMenu::~DwpMediaRefContextMenu()
{
    if (addPackageAction != nullptr) delete addPackageAction;
    if (addMediaRefSourceAction != nullptr) delete addMediaRefSourceAction;
    if (addMediaRefTargetActionAction != nullptr) delete addMediaRefTargetActionAction;
}

void
DwpMediaRefContextMenu::init(QtDwpAttribute *attr)
{
    // Call super class method.
    DwpContextMenu::init(attr);

    // Add menu actions.
    if (mUseJava) {
        // Support for Java and Android Digital Workprints.

        addPackageAction = new QAction(tr("Add DWP Package Item"), this);
        //addPackageAction->setShortcuts(QKeySequence::New);
        addPackageAction->setStatusTip(tr("Create a new Package item"));
        connect(addPackageAction, &QAction::triggered, this, &DwpMediaRefContextMenu::addPackage);
        mMenu->addAction(addPackageAction);
        //mMenu->addAction("Add DWP Package Item");
    }

    addMediaRefSourceAction = new QAction(tr("Add DWP MediaRefSource Item"), this);
    //addMediaRefSourceAction->setShortcuts(QKeySequence::New);
    addMediaRefSourceAction->setStatusTip(tr("Create a new MediaRefSource item"));
    connect(addMediaRefSourceAction, &QAction::triggered, this, &DwpMediaRefContextMenu::addPackage);
    mMenu->addAction(addMediaRefSourceAction);
    //mMenu->addAction("Add DWP MediaRefSource Item");

    addMediaRefTargetActionAction = new QAction(tr("Add DWP MediaRefTarget Item"), this);
    //addMediaRefTargetActionAction->setShortcuts(QKeySequence::New);
    addMediaRefTargetActionAction->setStatusTip(tr("Create a new Package item"));
    connect(addMediaRefTargetActionAction, &QAction::triggered, this, &DwpMediaRefContextMenu::addPackage);
    mMenu->addAction(addMediaRefTargetActionAction);
    //mMenu->addAction("Add DWP MediaRefTarget Item");
}

void
DwpMediaRefContextMenu::addPackage()
{
    qDebug() << "DwpMediaRefContextMenu: Adding DWP Package item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_PACKAGE, mAttr);
}

void
DwpMediaRefContextMenu::addMediaRefSource()
{
    qDebug() << "DwpMediaRefContextMenu: Adding DWP MediaRefSource item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_MEDIAREFSOURCE, mAttr);
}

void
DwpMediaRefContextMenu::addMediaRefTarget()
{
    qDebug() << "DwpMediaRefContextMenu: Adding DWP MediaRefTarget item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_MEDIAREFTARGET, mAttr);
}
