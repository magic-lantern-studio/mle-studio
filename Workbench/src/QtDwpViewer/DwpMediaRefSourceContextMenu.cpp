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

#include "DwpMediaRefSourceContextMenu.h"


DwpMediaRefSourceContextMenu::DwpMediaRefSourceContextMenu(QObject *parent)
    : DwpContextMenu(parent),
      addMediaAction(nullptr)
{
    // Do nothing extra.
}

DwpMediaRefSourceContextMenu::~DwpMediaRefSourceContextMenu()
{
    if (addMediaAction != nullptr) delete addMediaAction;
}

void
DwpMediaRefSourceContextMenu::init(QtDwpAttribute *attr)
{
    // Call super class method.
    DwpContextMenu::init(attr);

    addMediaAction = new QAction(tr("Add DWP Media Item"), this);
    //addMediaAction->setShortcuts(QKeySequence::New);
    addMediaAction->setStatusTip(tr("Create a new Media item"));
    connect(addMediaAction, &QAction::triggered, this, &DwpMediaRefSourceContextMenu::addMedia);
    mMenu->addAction(addMediaAction);
    //mMenu->addAction("Add DWP Media Item");
}

void
DwpMediaRefSourceContextMenu::addMedia()
{
    qDebug() << "DwpMediaRefSourceContextMenu: Adding DWP Media item";
    emit DwpContextMenu::insertItem(QtDwpAttribute::DWP_ATTRIBUTE_MEDIA, mAttr);
}
