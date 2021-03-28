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

#ifndef __DWPDOCUMENTCONTEXTMENU_H_
#define __DWPDOCUMENTCONTEXTMENU_H_

// Include DWP Viewer header files.
#include "DwpContextMenu.h"


/**
 * @brief The DwpDocumentContextMenu class is used to create and manage a context menu
 * associated with multiple, top-level DWP items. These DWP items are the Include, SetDef,
 * ActorDef, RoleDef, Stage, Scene, Group, MediaRef and Boot DWP items.
 */
class DwpDocumentContextMenu : public DwpContextMenu
{
  public:

    /**
     * @brief A constructor for the main Document context menu.
     *
     * @param parent A pointer to the parent Qt object.
     */
    explicit DwpDocumentContextMenu(QObject *parent = nullptr);

    /**
     * The destructor.
     */
    ~DwpDocumentContextMenu();

    /**
     * @brief Initialize the context menu.
     *
     * @param attr The DWP Attribute associated with this menu.
     */
    void init(QtDwpAttribute *attr);

  private slots:

    // Slot for adding a Include DWP item.
    void addInclude();
    // Slot for adding a SetDef DWP item.
    void addSetDef();
    // Slot for adding a ActorDef DWP item.
    void addActorDef();
    // Slot for adding a RoleDef DWP item.
    void addRoleDef();
    // Slot for adding a Stage DWP item.
    void addStage();
    // Slot for adding a Scene DWP item.
    void addScene();
    // Slot for adding a Group DWP item.
    void addGroup();
    // Slot for adding a MediaRef DWP item.
    void addMediaRef();
    // Slot for adding a Boot DWP item.
    void addBoot();

  private:

    // A pointer to an action used to create a Include item.
    QAction *addIncludeAction;
    // A pointer to an action used to create a SetDef item.
    QAction *addSetDefAction;
    // A pointer to an action used to create a ActorDef item.
    QAction *addActorDefAction;
    // A pointer to an action used to create a RoleDef item.
    QAction *addRoleDefAction;
    // A pointer to an action used to create a Stage item.
    QAction *addStageAction;
    // A pointer to an action used to create a Scene item.
    QAction *addSceneAction;
    // A pointer to an action used to create a Group item.
    QAction *addGroupAction;
    // A pointer to an action used to create a MediaRef item.
    QAction *addMediaRefAction;
    // A pointer to an action used to create a Boot item.
    QAction *addBootAction;
};

#endif // __DWPDOCUMENTCONTEXTMENU_H_
