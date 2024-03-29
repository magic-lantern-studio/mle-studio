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

#include "DwpPropertyDefContextMenu.h"

DwpPropertyDefContextMenu::DwpPropertyDefContextMenu(QObject *parent)
    : DwpContextMenu(parent),
      addMediaRefAction(nullptr)
{
    // Do nothing extra.
}

DwpPropertyDefContextMenu::~DwpPropertyDefContextMenu()
{
    if (addMediaRefAction != nullptr) delete addMediaRefAction;
}

void
DwpPropertyDefContextMenu::init(QtDwpAttribute *attr)
{
    // Call super class method.
    DwpContextMenu::init(attr);

    addMediaRefAction = new QAction(tr("Add DWP MediaRef Item"), this);
    //addMediaAction->setShortcuts(QKeySequence::New);
    addMediaRefAction->setStatusTip(tr("Create a new MediaRef item"));
    connect(addMediaRefAction, &QAction::triggered, this, &DwpPropertyDefContextMenu::addMediaRef);
    mMenu->addAction(addMediaRefAction);
    //mMenu->addAction("Add DWP MediaRef Item");
}

void
DwpPropertyDefContextMenu::addMediaRef()
{
    qDebug() << "DwpPropertyContextMenu: Adding DWP MediaRef item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_MEDIAREF, mAttr);
}
