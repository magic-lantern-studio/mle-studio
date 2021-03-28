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

#include "DwpContextMenu.h"

DwpContextMenu::DwpContextMenu(QObject *parent)
    : mMenu(new QMenu()),
      mUseJava(false),
      addTagAction(nullptr),
      deleteAction(nullptr)
{
    // Do nothing extra.
}

DwpContextMenu::~DwpContextMenu()
{
    if (mMenu) delete mMenu;
    if (addTagAction != nullptr) delete addTagAction;
    if (deleteAction != nullptr) delete deleteAction;
}

void
DwpContextMenu::init(QtDwpAttribute *attr)
{
    // Set associated DWP attribute.
    mAttr = attr;

    // Add menu actions.

    deleteAction = new QAction(tr("Delete DWP Item"), this);
    //deleteAction->setShortcuts(QKeySequence::New);
    deleteAction->setStatusTip(tr("Delete a DWP item"));
    connect(deleteAction, &QAction::triggered, this, &DwpContextMenu::deleteItem);
    mMenu->addAction(deleteAction);
    //mMenu->addAction("Delete DWP Item");

    mMenu->addSeparator();

    addTagAction = new QAction(tr("Add DWP Tag"), this);
    //addTagAction->setShortcuts(QKeySequence::New);
    addTagAction->setStatusTip(tr("Create a new Tag"));
    connect(addTagAction, &QAction::triggered, this, &DwpContextMenu::addTag);
    mMenu->addAction(addTagAction);
    //mMenu->addAction("Add DWP Tag");
}

void
DwpContextMenu::addTag()
{
    qDebug() << "DwpContextMenu: Adding DWP tag";
    QString tag("New tag");
    emit DwpContextMenu::addTagToAttribute(tag, mAttr);
}

void
DwpContextMenu::deleteItem()
{
    qDebug() << "DwpContextMenu: Deleting DWP item";
    emit DwpContextMenu::deleteAttribute(mAttr);
}
