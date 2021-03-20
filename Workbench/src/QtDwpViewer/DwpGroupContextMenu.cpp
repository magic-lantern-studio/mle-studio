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

#include "DwpGroupContextMenu.h"

DwpGroupContextMenu::DwpGroupContextMenu(QObject *parent)
    : DwpContextMenu(parent)
{
    // Do nothing extra.
}

DwpGroupContextMenu::~DwpGroupContextMenu()
{
    // Do nothing.
}

void
DwpGroupContextMenu::init()
{
    // Call super class method.
    DwpContextMenu::init();

    // Add menu actions.
    if (mUseJava) {
        // Support for Java and Android Digital Workprints.
        mMenu->addAction("Add DWP Package Item");
    } else {
        // Support for C/C++ Digital Workprints.
        mMenu->addAction("Add DWP HeaderFile Item");
        mMenu->addAction("Add DWP SourceFile Item");
    }

    mMenu->addAction("Add DWP Actor Item");
    mMenu->addAction("Add DWP RoleAttachment Item");
}
