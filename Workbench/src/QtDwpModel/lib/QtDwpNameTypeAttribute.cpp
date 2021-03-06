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

#include <iostream>

// Include Magic Lantern header files.
#include "QtDwpNameTypeAttribute.h"

QtDwpNameTypeAttribute::QtDwpNameTypeAttribute(const QVector<QVariant> &data, QtDwpTreeItem *parent)
  :QtDwpAttribute(data, parent)
{
    // Do nothing extra.
}

QtDwpNameTypeAttribute::~QtDwpNameTypeAttribute()
{
    // Do nothing.
}


void
QtDwpNameTypeAttribute::print()
{
    std::cout << "Printing QtDwpNameTypeAttribute" << std::endl;

    std::cout << "{" << std::endl;
    int columns = this->columnCount();
    for (int column = 0; column < columns; column++) {
        std::cout << "  { \"column" << column << "\" : ";
        QVariant data = this->data(column, -1);
        switch (data.userType()) {
            case QMetaType::QString: {
                std::cout << data.toString().toStdString();
                break;
            }
        }
        std::cout << " }" << std::endl;
    }
    std::cout << "}" << std::endl;
    std::cout << std::flush;
}

int
QtDwpNameTypeAttribute::dump(void *caller, void *calldata)
{
    QtDwpNameTypeAttribute *item = reinterpret_cast<QtDwpNameTypeAttribute *>(caller);
    item->print();
    return 1;
}
