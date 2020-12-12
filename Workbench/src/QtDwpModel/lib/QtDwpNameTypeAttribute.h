// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2020 Wizzer Works
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

#ifndef __QTDWPNAMETYPEATTRIBUTE_H_
#define __QTDWPNAMETYPEATTRIBUTE_H_

// Include Qt header files.
#include <QObject>

#include "QtDwpModel_global.h"
#include "QtDwpAttribute.h"

class QTDWPMODEL_EXPORT QtDwpNameTypeAttribute : public QtDwpAttribute
{
    Q_OBJECT

  public:

    /**
     * @brief The constructor used to create an name/type attribute for the DWP model.
     *
     * @param data A reference to the data managed by this item.
     * @param parent A pointer to the parent of this item. By default, it will
     * be <b>nullptr</b>.
     */
    QtDwpNameTypeAttribute(const QVector<QVariant> &data, QtDwpTreeItem *parent = nullptr);

    /**
     * @brief The destructor.
     */
    virtual ~QtDwpNameTypeAttribute();
};

#endif /* __QTDWPNAMETYPEATTRIBUTE_H_ */
