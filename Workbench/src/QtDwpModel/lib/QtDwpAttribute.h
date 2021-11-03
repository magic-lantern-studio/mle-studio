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

#ifndef __QTDWPATTRIBUTE_H_
#define __QTDWPATTRIBUTE_H_

// Include Qt header files.
#include <QObject>

#include "QtDwpModel_global.h"
#include "QtDwpTreeItem.h"

class MleDwpItem;

class QTDWPMODEL_EXPORT QtDwpAttribute : public QtDwpTreeItem
{
    Q_OBJECT

  public:

    /**
     * @brief The AttributeType enumerates the types of valid
     * attributes.
     */
    enum AttributeType {
        DWP_ATTRIBUTE_ACTOR,
        DWP_ATTRIBUTE_ACTORDEF,
        DWP_ATTRIBUTE_BOOT,
        DWP_ATTRIBUTE_DSOFILE,
        DWP_ATTRIBUTE_GROUP,
        DWP_ATTRIBUTE_GROUPREF,
        DWP_ATTRIBUTE_HEADERFILE,
        DWP_ATTRIBUTE_INCLUDE,
        DWP_ATTRIBUTE_MEDIA,
        DWP_ATTRIBUTE_MEDIAREF,
        DWP_ATTRIBUTE_MEDIAREFSOURCE,
        DWP_ATTRIBUTE_MEDIAREFTARGET,
        DWP_ATTRIBUTE_PACKAGE,
        DWP_ATTRIBUTE_PROPERTY,
        DWP_ATTRIBUTE_PROPERTYDEF,
        DWP_ATTRIBUTE_ROLEATTACHMENT,
        DWP_ATTRIBUTE_ROLEBINDING,
        DWP_ATTRIBUTE_ROLEDEF,
        DWP_ATTRIBUTE_SCENE,
        DWP_ATTRIBUTE_SET,
        DWP_ATTRIBUTE_SETDEF,
        DWP_ATTRIBUTE_SOURCEFILE,
        DWP_ATTRIBUTE_STAGE,
        DWP_ATTRIBUTE_STAGEDEF,
        DWP_ATTRIBUTE_TAG,
        DWP_ATTRIBUTE_UNKNOWN
    };

  public:

    /**
     * @brief The constructor used to create an attribute for the DWP model.
     *
     * @param data A reference to the data managed by this item.
     * @param parent A pointer to the parent of this item. By default, it will
     * be NULL.
     */
    QtDwpAttribute(const QVector<QVariant> &data, QtDwpTreeItem *parent = nullptr);

    /**
     * @brief The destructor.
     */
    virtual ~QtDwpAttribute();

    /**
     * @brief Get the associated DWP item.
     *
     * @return A pointer to the DWP item is returned. It may be <b>nullptr</b>
     * if it was never set.
     */
    const MleDwpItem *getDwpItem() const
    { return mDwpItem; }

    /**
     * @brief Set the associated DWP item.
     *
     * @param item The DWP item to set.
     */
    void setDwpItem(const MleDwpItem *item)
    { mDwpItem = item; }

    /**
     * @brief Retrieve the name of the DWP attribute.
     *
     * @return A <b>QString</b> is returned containing the name of the attribute.
     */
    QString getAttributeName() const;

    /**
     * @brief setType Set the DWP attribute type.
     *
     * @param type The attibute type to set. By default, the type is
     * <b>DWP_ATTRIBUTE_UNKNWON<b>.
     */
    void setType(AttributeType type)
    { mType = type; }

    /**
     * @brief Determine if this attribute is the specified type.
     *
     * @param type The DWP attrubute type to test for.
     *
     * @return <b>true</b> will be returned if this attribute is of
     * type <b>type</b>. Otherwise, <b>false</b> will be returned.
     */
    bool isAttributeType(AttributeType type);

    /**
     * @brief Determine if this attribute has any children of the specified type.
     *
     * @param type The DWP attribute type to search for.
     *
     * @return <b>true</b> will be returned if a child of this attribute is of
     * type <b>type</b>. Otherwise, <b>false</b> will be returned.
     */
    bool hasAttributeType(AttributeType type);

    /**
     * @brief Print the contents of the attribute to stdout.
     */
    virtual void print();

    /**
     * @brief Dump the contents of the attribute.
     *
     * @param caller A pointer to the caller that will be dumped.
     * @param calldata A pointer to caller specific data.
     *
     * @return If the dump is successful, then <b>1</b> is returned.
     * Otherwise, <b>0</b> will be returned.
     */
    static int dump(void *caller, void *calldata);

  protected:

    // The DWP item associated with this attribute.
    const MleDwpItem *mDwpItem;

    // The type of attribute.
    AttributeType mType;

};

#endif /* __QTDWPATTRIBUTE_H_ */

