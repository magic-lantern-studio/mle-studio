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

#include "DwpDocumentContextMenu.h"

DwpDocumentContextMenu::DwpDocumentContextMenu(QObject *parent)
    : QObject(parent),
      mMenu(new QMenu())
{
    // Add menu actions.
    mMenu->addAction("Add DWP Include Item");
    mMenu->addAction("Add DWP SetDef Item");
    mMenu->addAction("Add DWP ActorDef Item");
    mMenu->addAction("Add DWP RoleDef Item");
    mMenu->addAction("Add DWP Stage Item");
    mMenu->addAction("Add DWP Scene Item");
    mMenu->addAction("Add DWP Group Item");
    mMenu->addAction("Add DWP MediaDef Item");
    mMenu->addAction("Add DWP Boot Item");
    mMenu->addSeparator();
    mMenu->addAction("Add DWP Tag");
    mMenu->addSeparator();
    mMenu->addAction("Delete DWP Item");
}

DwpDocumentContextMenu::~DwpDocumentContextMenu()
{
    if (mMenu) delete mMenu;
}
