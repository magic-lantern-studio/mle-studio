// COPYRIGHT_BEGIN
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

#ifndef __DWPACTORCONTEXTMENU_H_
#define __DWPACTORCONTEXTMENU_H_

// Include DWP Viewer header files.
#include "DwpContextMenu.h"


/**
 * @brief The DwpActorContextMenu class is used to create and manage a context menu
 * associated with an Actor DWP item.
 */
class DwpActorContextMenu : public DwpContextMenu
{
  public:

    /**
     * @brief A constructor for the Actor context menu.
     *
     * @param parent A pointer to the parent Qt object.
     */
    explicit DwpActorContextMenu(QObject *parent = nullptr);

    /**
     * The destructor.
     */
    ~DwpActorContextMenu();

    /**
     * @brief Initialize the context menu.
     *
     * @param attr The DWP Attribute associated with this menu.
     */
    void init(QtDwpAttribute *attr);

  private slots:

    // Slot for adding a HeaderFile DWP item.
    void addHeaderFile();

    // Slot for adding a SourceFile DWP item.
    void addSourceFile();

    // Slot for adding a Package DWP item.
    void addPackage();

    // Slot for adding a RoleBinding DWP item.
    void addRoleBinding();

    // Slot for adding a Int Property DWP item.
    void addIntProperty();
    void addFloatProperty();
    void addStringProperty();
    void addMlVector2Property();
    void addMlVector3Property();
    void addMlVector4Property();
    void addMlRotationProperty();
    void addMlTransformProperty();

  private:

    // A pointer to an action used to create a HeaderFile item.
    QAction *addHeaderFileAction;
    // A pointer to an action used to create a SourceFile item.
    QAction *addSourceFileAction;
    // A pointer to an action used to create a Package item.
    QAction *addPackageAction;
    // A pointer to an action used to create a RoleBinding item.
    QAction *addRoleBindingAction;
    // A pointer to an action used to create a Property item.
    QAction *addPropertyAction;
};

#endif // _DWPACTORCONTEXTMENU_H_
