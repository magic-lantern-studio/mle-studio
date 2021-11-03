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

#include "QtDwpAttribute.h"

QtDwpAttribute::QtDwpAttribute(const QVector<QVariant> &data, QtDwpTreeItem *parent)
  :QtDwpTreeItem(data, parent),
   mDwpItem(nullptr),
   mType(DWP_ATTRIBUTE_UNKNOWN)
{
    // Do nothing extra.
}

QtDwpAttribute::~QtDwpAttribute()
{
    // Do nothing.
}

QString
QtDwpAttribute::getAttributeName() const
{
    // The name of the atribute is the value of the first column, 0.
    QVariant data = this->data(0, -1);
    return data.toString();
}

bool
QtDwpAttribute::isAttributeType(AttributeType type)
{
    return (mType == type);
}

bool
QtDwpAttribute::hasAttributeType(AttributeType type)
{
    // Search just the immediate children for the specified type.
    if (hasChildren()) {
        int n = childCount();
        for (int i = 0; i < n; i++) {
            QtDwpTreeItem *next = child(i);
            QtDwpAttribute *attr = static_cast<QtDwpAttribute *>(next);
            if (attr->isAttributeType(type)) return true;
        }
    }

    return false;
}

void
QtDwpAttribute::print()
{
    std::cout << "Printing QtDwpAttribute" << std::endl;
}

int
QtDwpAttribute::dump(void *caller, void *calldata)
{
    QtDwpTreeItem *item = reinterpret_cast<QtDwpTreeItem *>(caller);
    item->print();
    return 1;
}
