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

#ifndef __DWPCONTEXTMENU_H_
#define __DWPCONTEXTMENU_H_

// Include Qt header files.
#include <QObject>
#include <QMenu>

// Include the Magic Lantern Workbench header files.
#include "qt/QtDwpAttribute.h"

/**
 * @brief The DwpContextMenu class is the base class for all DWP context mentus It is
 * also used to create and manage common context menu actions, like adding a DWP tag
 * and deleting a DWP item.
 */
class DwpContextMenu : public QObject
{
    Q_OBJECT

  public:

    /**
     * @brief A constructor for the context menu.
     *
     * @param parent A pointer to the parent Qt object.
     */
    explicit DwpContextMenu(QObject *parent = nullptr);

    /**
     * @brief The descrtuctor.
     */
    virtual ~DwpContextMenu();

    /**
     * @brief Initialize the context menu.
     *
     * @param attr The DWP Attribute associated with this menu.
     */
    virtual void init(QtDwpAttribute *attr);

    /**
     * @brief Get the menu for this context.
     *
     * @return A pointer to the menu is retruned.
     */
    QMenu *getMenu() const
    { return mMenu; }

    /**
     * @brief Determine whether the context is set for creating a Java or Android
     * Digital Workprint.
     *
     * @return <b>true</b> will be returned if the context is initialized to manage
     * Java based Digital Workprints. Otherwise <b>false</b> will be returned.
     */
    bool isJava()
    { return mUseJava; }

    /**
     * @brief Set the mode for Digital Workrpint generation.
     *
     * @param java Set to <b>true</b> if the context menu is to be used for Java or
     * Androod based Digital Workprints. Set to <b>false<b> if the context menu is
     * to be used for C/C++ based Digital Workprints.
     */
    void useJava(bool java)
    { mUseJava = java; }

  signals:

    /**
     * @brief Signal to insert an attribute.
     *
     * @param type The type of attribute being inserted.
     * @param attr A pointer to the attribute being inserted.
     */
    void insertAttribute(const QtDwpAttribute::AttributeType type, QtDwpAttribute *attr);

    /**
     * @brief Signal to delete an attribute.
     *
     * @param attr A pointer to the attribute being deleted.
     */
    void deleteAttribute(QtDwpAttribute *attr);

    /**
     * @brief Signal to add a tag to an attribute.
     *
     * @param tag The tag to be added.
     * @param attr A pointer to the attribute that the tag is being added to.
     */
    void addTagToAttribute(const QString tag, QtDwpAttribute *attr);

    // Todo: Create signal for rmTagFromAttribute.

  protected:

    // Associated DWP Attribute.
    QtDwpAttribute *mAttr;

    // Associated Qt menu.
    QMenu *mMenu;

    // Flag indicating whether to support Java/Android DWP.
    bool mUseJava;

  private slots:

    // Slot for adding a tag to a DWP item.
    void addTag();

    // Slot for deleting a DWP item.
    void deleteItem();

  private:

    // A pointer to an action used to create a tag to a DWP item.
    QAction *addTagAction;
    // A pointer to an action used to delete a DWP item.
    QAction *deleteAction;
};

#endif // __DWPCONTEXTMENU_H_
