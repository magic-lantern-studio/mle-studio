/**
 * @file qtdwptreeitem.cpp
 *
 * A container for items of data supplied by the DWP model.
 */

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

// Include DWP model header files.
#include "QtDwpTreeItem.h"


QtDwpTreeItem::QtDwpTreeItem(const QVector<QVariant> &data, QtDwpTreeItem *parent)
    : mItemData(data),
      mParentItem(parent)
{}

QtDwpTreeItem::~QtDwpTreeItem()
{
    qDeleteAll(mChildItems);
}

QtDwpTreeItem *QtDwpTreeItem::child(int number)
{
    if (number < 0 || number >= mChildItems.size())
        return nullptr;
    return mChildItems.at(number);
}

int
QtDwpTreeItem::childCount() const
{
    return mChildItems.count();
}

int
QtDwpTreeItem::childNumber() const
{
    if (mParentItem)
        return mParentItem->mChildItems.indexOf(const_cast<QtDwpTreeItem*>(this));
    return 0;
}

int
QtDwpTreeItem::columnCount() const
{
    return mItemData.count();
}

QVariant
QtDwpTreeItem::data(int column) const
{
    if (column < 0 || column >= mItemData.size())
        return QVariant();
    return mItemData.at(column);
}

bool
//QtDwpTreeItem::insertChildren(int position, int count, int columns)
QtDwpTreeItem::insertChildren(int position, int count, QVector<QtDwpTreeItem *> items)
{
    if (position < 0 || position > mChildItems.size())
        return false;

    if (count > items.size())
        return false;

    for (int row = 0; row < count; ++row) {
        //QVector<QVariant> data(columns);
        //QtDwpTreeItem *item = new QtDwpTreeItem(data, this);
        QtDwpTreeItem *item = items[row];
        mChildItems.insert(position, item);
    }

    return true;
}

bool
QtDwpTreeItem::insertColumns(int position, int columns)
{
    if (position < 0 || position > mItemData.size())
        return false;

    for (int column = 0; column < columns; ++column)
        mItemData.insert(position, QVariant());

    for (QtDwpTreeItem *child : qAsConst(mChildItems))
        child->insertColumns(position, columns);

    return true;
}

QtDwpTreeItem *QtDwpTreeItem::parent()
{
    return mParentItem;
}

bool
QtDwpTreeItem::removeChildren(int position, int count)
{
    if (position < 0 || position + count > mChildItems.size())
        return false;

    for (int row = 0; row < count; ++row)
        delete mChildItems.takeAt(position);

    return true;
}

bool
QtDwpTreeItem::removeColumns(int position, int columns)
{
    if (position < 0 || position + columns > mItemData.size())
        return false;

    // Remove columns from this item.
    for (int column = 0; column < columns; ++column)
        mItemData.remove(position);

    // Remove columns from the item's children.
    for (QtDwpTreeItem *child : qAsConst(mChildItems))
        child->removeColumns(position, columns);

    return true;
}

bool
QtDwpTreeItem::setData(int column, const QVariant &value)
{
    if (column < 0 || column >= mItemData.size())
        return false;

    mItemData[column] = value;
    return true;
}

void
QtDwpTreeItem::traverse(QtDwpTreeItemTraverseCB cb, void *caller, void *callData)
{
    // Call traversal callback for this item.
    (cb)(caller, callData);

    // Traverse the remaining children.
    if (hasChildren()) {
        for (int next = 0; next < mChildItems.size(); next++) {
            // Call traversal for child item.
            mChildItems[next]->traverse(cb, mChildItems[next], callData);
        }
    }
}

void
QtDwpTreeItem::print()
{
    std::cout << "Printing QtDwpTreeItem" << std::endl;
}
