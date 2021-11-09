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

#ifndef __DWPTREEVIEW_H_
#define __DWPTREEVIEW_H_

// Include Qt header files.
#include <QTreeView>
#include <QObject>

// Include the Magic Lantern Workbench header files.
#include "qt/QtDwpTreeItem.h"
#include "qt/QtDwpAttribute.h"


/**
 * @brief The DwpTreeView class is a QTreeView used for the DWP Viewer Model/View implementation.
 *
 * The DwpTreeView dispalys the Magic Lantern Digital Workprint using a hierarchical tree,
 * reflecting the structure of the document.
 */
class DwpTreeView : public QTreeView
{
    Q_OBJECT

  public:

    /**
     * @brief Default constructor.
     */
    DwpTreeView();

    /**
     * @brief Get the item at the specified position.
     *
     * @param pos The coordinate position used to retrieve the item.
     *
     * @return A pointer to the tree item is returned.
     */
    QtDwpTreeItem *itemAt(const QPoint &pos) const;

    /**
     * @brief The destructor.
     */
    ~DwpTreeView();

    /**
     * @brief Set flag indicating that DWP is being edited for Java or
     * Android platform.
     *
     * @param value If <b>true</b>, then the tree view will be specific
     * to Java/Android Digital Workprints. if <b>false</b>, then the tree
     * view will be oriented towards C/C++ Digital Workprints.
     */
    void useJava(bool value)
    { mUseJava = value; }

    /**
     * @brief Process a context menu event, displaying a pop-up menu for the associated
     * <b>QtDwpTreeItem</b> in the tree view.
     *
     * @param e The event information.
     */
    void contextMenuEvent(QContextMenuEvent *e);

  signals:

    /**
     * @brief Signal to insert an item into the view at the specified
     * position.
     *
     * @param type The type of item to insert.
     * @param pos The position in the view used for insertion.
     */
    void insertItem(const QtDwpAttribute::AttributeType type, const QPoint pos);

    /**
     * @brief Signal to delete an item from the view at the specified
     * position.
     *
     * @param pos The position in the view used for deletion.
     */
    void deleteItem(const QPoint pos);

    /**
     * @brief Signal to add a tag to the attribute located at the specified
     * position.
     *
     * @param tag The tag to add.
     * @param pos The position in the view used for adding the tag.
     */
    void addTag(const QString tag, const QPoint pos);

  private slots:

    // Slot to add an attribute.
    void addAttribute(const QtDwpAttribute::AttributeType type, QtDwpAttribute *attr);

    // Slot to delete an attribute.
    void deleteAttribute(QtDwpAttribute *attr);

  private:

    // Dispaly the default context menu for the specified attribute.
    void displayDefaultContextMenu(QtDwpAttribute *attr, QPoint pos);

    // Flag indicating that view is being used to edit a Java or Android
    // based Digital Workprint.
    bool mUseJava;

};

#endif // __DWPTREEVIEW_H_
