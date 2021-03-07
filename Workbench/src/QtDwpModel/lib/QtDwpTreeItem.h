/**
  * @file qtdwptreeitem.h
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

#ifndef __QTDWPTREEITEM_H_
#define __QTDWPTREEITEM_H_

// Include Qt header files.
#include <QObject>
#include <QVariant>
#include <QVector>

#include "QtDwpModel_global.h"


/**
 * Define the callback function used with the <code>QtDwpTreeItem</code>
 * traversal methods.
 */
typedef int (*QtDwpTreeItemTraverseCB)(void *caller, void *callData);


/**
 * @brief The QtDwpTreeItem class is used to hold data for the DWP model.
 */
class QTDWPMODEL_EXPORT QtDwpTreeItem: public QObject
{
    Q_OBJECT

  public:

    /**
     * @brief The constructor used to create an item for the DWP model.
     *
     * @param data A reference to the data managed by this item.
     * @param parent A pointer to the parent of this item. By default, it will
     * be NULL.
     */
    QtDwpTreeItem(const QVector<QVariant> &data, QtDwpTreeItem *parent = nullptr);

    /**
     * The destructor.
     *
     * All children items will be deleted.
     */
    ~QtDwpTreeItem();

    /**
     * @brief Retrieve the child item located at the specifed index.
     *
     * @param number The index of the child to retrieve. Range must be
     * ">= 0" or "< number of children".
     *
     * @return Returns the child item at index <i>number</i>. <b>nullptr</b>
     * will be returned if the index is out of range (i.e. "< 0" or ">= number of
     * children".
     */
    QtDwpTreeItem *child(int number);

    /**
     * @brief Retrieve the number of children for this item.
     *
     * @return The number of children is returned. <b>0</b> will be
     * retuned if the item has no children.
     */
    int childCount() const;

    /**
     * @brief Retrieve the index of this item's parent list of children.
     *
     * @return Returns the index of this item's location in it's parent.
     * <b>0</b> will be returned if there is no parent for this item.
     */
    int childNumber() const;

    /**
     * @brief Retrieve the number of columns in this item.
     *
     * @return The number of columns is returned.
     */
    int columnCount() const;

    /**
     * @brief Retrieve the data located at the specified column index.
     *
     * @param column The location of the data to retrieve.
     *
     * @return The data is returned located at the specified column
     * index. if the column is out of range, then an invalid
     * <b>QVariant</b> is returned.
     */
    QVariant data(int column) const;

    /**
     * @brief Insert child items into this item.
     *
     * @param position The location, row index, where the insertion is to
     * occur.
     * @param count The number of child items to insert at <i>position</i>.
     * @param columns The number of columns in the childs row.
     *
     * @return <b>true</b> will be returned if the insertion is successful.
     * Otherwise, <b>false</b> will be returned.
     */
    //bool insertChildren(int position, int count, int columns);
    bool insertChildren(int position, int count, QVector<QtDwpTreeItem *> items);

    /**
     * @brief Insert columns into this item's row.
     *
     * @param position The location, column index, where the insertion
     * is to occur.
     * @param columns The number of columns to insert at <i>position</i>.
     *
     * @return <b>true</b> will be returned if the insertion is successful.
     * Otherwise, <b>false</b> will be returned.
     */
    bool insertColumns(int position, int columns);

    /**
     * @brief Retrieve this item's parent.
     *
     * @return A pointer to this item's parent is returned. It may be
     * <b>nullptr</b>.
     */
    QtDwpTreeItem *parent();

    /**
     * @brief Remove children from this item.
     *
     * @param position The row index where the children should be removed from.
     * @param count The number of children to remove.
     *
     * @return <b>true</b> will be returned if the removal is successful.
     * Otherwise, <b>false</b> will be returned.
     */
    bool removeChildren(int position, int count);

    /**
     * @brief Remove columns from this item.
     *
     * @param position The column index where the columns should be remved from.
     * @param columns The number of columns to remove.
     *
     * @return <b>true</b> will be returned if the removal is successful.
     * Otherwise, <b>false</b> will be returned.
     */
    bool removeColumns(int position, int columns);

    /**
     * @brief Set the data for the specified column.
     *
     * @param column The column index specifying where to set the data.
     * @param value A reference to the data to set.
     *
     * @return <b>true</b> will be returned if the data is set successfully.
     * Otherwise, <b>false</b> will be returned; for example, when the
     * column index is out of range.
     */
    bool setData(int column, const QVariant &value);

    /**
     * @brief Determine if this item has any children items.
     *
     * @return <b>true</b> is returned if this item has any children.
     * Otherwise, <b>false</b> is returned.
     */
    bool hasChildren() { return ! mChildItems.isEmpty(); }

    /**
     * @brief Walk the item hierarchy and call the specified function
     * <b>cb</b> with the parameters <b>caller</b> and <b>calldata</b>.
     *
     * @param cb The callback fucntion to call at each level in the hierarchy.
     * @param caller The caller invoking this traversal.
     * @param calldata The call data to use with the callback.
     */
    void traverse(QtDwpTreeItemTraverseCB cb, void *caller, void *calldata);

    virtual void print();

  private:

    // The list of children.
    QVector<QtDwpTreeItem*> mChildItems;
    // The rows of data in this item.
    QVector<QVariant> mItemData;
    // The parent item.
    QtDwpTreeItem *mParentItem;
};

#endif /** __QTDWPTREEITEM_H_ */
