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

#ifndef __DWPMEDIAREFCONTEXTMENU_H_
#define __DWPMEDIAREFCONTEXTMENU_H_

// Include DWP Viewer header files.
#include "DwpContextMenu.h"


/**
 * @brief The DwpMediaRefContextMenu class is used to create and manage a context menu
 * associated with an MediaRef DWP item.
 */
class DwpMediaRefContextMenu : public DwpContextMenu
{
  public:

    /**
     * @brief A constructor for the MediaRef context menu.
     *
     * @param parent A pointer to the parent Qt object.
     */
    explicit DwpMediaRefContextMenu(QObject *parent = nullptr);

    ~DwpMediaRefContextMenu();

    /**
     * @brief Initialize the context menu.
     *
     * @param attr The DWP Attribute associated with this menu.
     */
    void init(QtDwpAttribute *attr);

  private slots:

    // Slot for adding a Package DWP item.
    void addPackage();

    // Slot for adding a MediaRefSource DWP item.
    void addMediaRefSource();

    // Slot for adding a MediaRefTarget DWP item.
    void addMediaRefTarget();

  private:

    // A pointer to an action used to create a Package item.
    QAction *addPackageAction;
    // A pointer to an action used to create a MediaRefSource item.
    QAction *addMediaRefSourceAction;
    // A pointer to an action used to create a MediaRefTarget item.
    QAction *addMediaRefTargetActionAction;
};

#endif // __DWPMEDIAREFCONTEXTMENU_H_
