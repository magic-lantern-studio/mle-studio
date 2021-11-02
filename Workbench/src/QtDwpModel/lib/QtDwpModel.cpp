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

// Include Magic Lantern Workbench header files.
#include "QtDwpModel.h"
#include "QtDwpTreeItem.h"
#include "QtDwpNameTypeAttribute.h"
#include "QtDwpNameTypeValueAttribute.h"
#include "QtMlVector2.h"
#include "QtMlVector3.h"
#include "QtMlVector4.h"
#include "QtMlTransform.h"
#include "QtMlRotation.h"

// Include Qt header files.
#include <QtWidgets>

// Include Magic Lantern header files.
#include "mle/mlItoa.h"
#include "math/rotation.h"
#include "math/transfrm.h"

// Include Digital Workprint header files.
#include "mle/DwpItem.h"
#include "mle/DwpDatatype.h"
#include "mle/DwpProperty.h"
#include "mle/DwpInclude.h"
#include "mle/DwpDSOFile.h"
#include "mle/DwpHeaderFile.h"
#include "mle/DwpSourceFile.h"
#include "mle/DwpPackage.h"
#include "mle/DwpSet.h"
#include "mle/DwpSetDef.h"
#include "mle/DwpActorDef.h"
#include "mle/DwpRoleDef.h"
#include "mle/DwpPropertyDef.h"
#include "mle/DwpGroup.h"
#include "mle/DwpRoleBinding.h"
#include "mle/DwpRoleAttachment.h"
#include "mle/DwpRoleSetMapping.h"
#include "mle/DwpActor.h"
#include "mle/DwpInt.h"
#include "mle/DwpFloat.h"
#include "mle/DwpString.h"
#include "mle/DwpScalar.h"
#include "mle/DwpVector2.h"
#include "mle/DwpVector3.h"
#include "mle/DwpVector4.h"
#include "mle/DwpIntArray.h"
#include "mle/DwpFloatArray.h"
#include "mle/DwpRotation.h"
#include "mle/DwpTransform.h"
#include "mle/DwpMediaRef.h"
#include "mle/DwpMedia.h"
#include "mle/DwpMediaRefClass.h"
#include "mle/DwpMediaRefSource.h"
#include "mle/DwpMediaRefTarget.h"
#include "mle/DwpStage.h"
#include "mle/DwpStageDef.h"
#include "mle/DwpScene.h"
#include "mle/DwpBoot.h"
#include "mle/DwpGroupRef.h"

QtDwpModel::QtDwpModel(const QStringList &headers, const MleDwpItem *data, QObject *parent)
    : QAbstractItemModel(parent)
{
    QVector<QVariant> rootData;
    for (const QString &header : headers)
        rootData << header;

    // Initialize the model with the header data.
    mRootItem = new QtDwpTreeItem(rootData);
    // Setup the model data.
    setupModelData(data, mRootItem);
    mDwp = data;
    // Configurate status of model.
    mIsModified = false;
}

QtDwpModel::~QtDwpModel()
{
    delete mRootItem;
}

QtDwpAttribute *
QtDwpModel::createActor(MleDwpActor *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Actor";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getActorClass();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createActorDef(MleDwpActorDef *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "ActorDef";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createBoot(MleDwpBoot *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Boot";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createDSOFile(MleDwpDSOFile *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "DSOFile";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createGroup(MleDwpGroup *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Group";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getGroupClass();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createGroupRef(MleDwpGroupRef *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "GroupRef";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createHeaderFile(MleDwpHeaderFile *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "HeaderFile";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createInclude(MleDwpInclude *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Include";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createMedia(MleDwpMedia *item, QtDwpTreeItem *parent)
{
    // Todo: not a name or name/type attribute.
    return nullptr;
}

QtDwpAttribute *
QtDwpModel::createMediaRef(MleDwpMediaRef *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "MediaRef";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getMediaRefClass();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createMediaRefClass(MleDwpMediaRefClass *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "MediaRefClass";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getClassName();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createMediaRefSource(MleDwpMediaRefSource *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "MediaRefSource";
    QString sDwpItem(dwpItem);

    int flags = item->getFlags();
    char buffer[20];
    mlItoa(flags, buffer, 10);
    QString sName(buffer);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createMediaRefTarget(MleDwpMediaRefTarget *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "MediaRefTarget";
    QString sDwpItem(dwpItem);

    int flags = item->getFlags();
    char buffer[20];
    mlItoa(flags, buffer, 10);
    QString sName(buffer);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createPackage(MleDwpPackage *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Package";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createProperty(MleDwpProperty *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Property";
    QString sDwpItem(dwpItem);

    QtDwpAttribute *attr = nullptr;
    const MleDwpDatatype *dataType = item->getDatatype();

    if (dataType != nullptr) {
        if (dataType->isa(MleDwpInt::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value.
            int value;
            dataType->get(&(item->m_data),&value);
            QVariant vValue(value);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpFloat::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value.
            float value;
            dataType->get(&(item->m_data),&value);
            QVariant vValue(value);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpString::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value.
            char *value;
            dataType->get(&(item->m_data),&value);
            QVariant vValue(value);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpScalar::typeId)) {
            // Get the name;
            const char *name = item->getName();
            QString sName(name);

            // Get the type;
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Map scalar type to a float.
            float value;
            dataType->get(&(item->m_data),&value);
            QVariant vValue(value);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpVector2::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value,
            MlVector2 value;
            dataType->get(&(item->m_data),&value);

            // And convert it to a QVariant.
            QtMlVector2 qValue(value);
            QVariant vValue;
            vValue.setValue(qValue);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpVector3::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value,
            MlVector3 value;
            dataType->get(&(item->m_data),&value);

            // And convert it to a QVariant.
            QtMlVector3 qValue(value);
            QVariant vValue;
            vValue.setValue(qValue);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpVector4::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value,
            MlVector4 value;
            dataType->get(&(item->m_data),&value);

            // And convert it to a QVariant.
            QtMlVector4 qValue(value);
            QVariant vValue;
            vValue.setValue(qValue);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpIntArray::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value,
            MleArray<int> value;
            dataType->get(&(item->m_data),&value);
            int size = value.size();

            // Create an array of integers.
            int intValue[size];
            for (int i = 0; i < size; i++) {
                intValue[i] = value[i];
            }

            // And convert it to a QVariant.
            QVector<int> qiarray;
            for (int i = 0; i < size; i++) {
                qiarray.append(intValue[i]);
            }
            QVariant vValue = QVariant::fromValue(qiarray);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpFloatArray::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value,
            MleArray<float> value;
            dataType->get(&(item->m_data),&value);
            int size = value.size();

            // Create an array of integers.
            int floatValue[size];
            for (int i = 0; i < size; i++) {
                floatValue[i] = value[i];
            }

            // And convert it to a QVariant.
            QVector<float> qfarray;
            for (int i = 0; i < size; i++) {
                qfarray.append(floatValue[i]);
            }
            QVariant vValue = QVariant::fromValue(qfarray);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpRotation::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value,
            MlRotation value;
            dataType->get(&(item->m_data),&value);

            // And convert it to a QVariant.
            QtMlRotation qValue(value);
            QVariant vValue;
            vValue.setValue(qValue);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        } else if (dataType->isa(MleDwpTransform::typeId)) {
            // Get the name.
            const char *name = item->getName();
            QString sName(name);

            // Get the type.
            const MleDwpDatatype *dataType = item->getDatatype();
            const char *type = dataType->getName();
            QString sType(type);

            // Get the value,
            MlTransform value;
            dataType->get(&(item->m_data),&value);

            // And convert it to a QVariant.
            QtMlTransform qValue(value);
            QVariant vValue;
            vValue.setValue(qValue);

            // Create and initialize the data for the Qt model representation.
            QVector<QVariant> data;
            data << sDwpItem;
            data << sName;
            data << sType;
            data << vValue;

            attr = new QtDwpNameTypeValueAttribute(data, parent);
            attr->setDwpItem(item);
        }
    }

    return attr;
}


QtDwpAttribute *
QtDwpModel::createPropertyDef(MleDwpPropertyDef *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "PropertyDef";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getType();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createRoleAttachment(MleDwpRoleAttachment *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "RoleAttachment";
    QString sDwpItem(dwpItem);

    const char *name = item->getParent();
    QString sName(name);

    const char *type = item->getChild();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createRoleBinding(MleDwpRoleBinding *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "RoleBinding";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getSet();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createRoleDef(MleDwpRoleDef *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "RoleDef";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createRoleSetMapping(MleDwpRoleSetMapping *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "RoleSetMapping";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getSet();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createScene(MleDwpScene *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Scene";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getSceneClass();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createSet(MleDwpSet *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Set";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getType();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createSetDef(MleDwpSetDef *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "SetDef";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createSourceFile(MleDwpSourceFile *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "SourceFile";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createStage(MleDwpStage *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "Stage";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    const char *type = item->getStageClass();
    QString sType(type);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;
    data << sType;

    QtDwpAttribute *attr = new QtDwpNameTypeAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

QtDwpAttribute *
QtDwpModel::createStageDef(MleDwpStageDef *item, QtDwpTreeItem *parent)
{
    const char *dwpItem = "StageDef";
    QString sDwpItem(dwpItem);

    const char *name = item->getName();
    QString sName(name);

    QVector<QVariant> data;
    data << sDwpItem;
    data << sName;

    QtDwpAttribute *attr = new QtDwpNameAttribute(data, parent);
    attr->setDwpItem(item);

    return attr;
}

bool
QtDwpModel::setupModelData(const MleDwpItem *item, QtDwpTreeItem *parent)
{
    bool rval = false;
    QtDwpAttribute *attr = nullptr;

    // Check if item is NULL.
    if (item == nullptr) return rval;

    // Walk the Digital Workprint item herarchy.
    if (item->isa(MleDwpSet::typeId))
    {
        // Create the Set attribute.
        attr = createSet((MleDwpSet *)item, parent);
    } else if (item->isa(MleDwpActorDef::typeId))
    {
        // Create the ActorDef attribute.
        attr = createActorDef((MleDwpActorDef *)item, parent);
    } else if (item->isa(MleDwpRoleDef::typeId))
    {
        // Create the RoleDef attribute.
        attr = createRoleDef((MleDwpRoleDef *)item, parent);
    } else if (item->isa(MleDwpSetDef::typeId))
    {
        // Create the SetDef attribute.
        attr = createSetDef((MleDwpSetDef *)item, parent);
    } else if (item->isa(MleDwpPropertyDef::typeId))
    {
        // Create the PropertyDef attribute.
        attr = createPropertyDef((MleDwpPropertyDef *)item, parent);
    } else if (item->isa(MleDwpInclude::typeId))
    {
        // Create the Include attribute.
        attr = createInclude((MleDwpInclude *)item, parent);
    } else if (item->isa(MleDwpDSOFile::typeId))
    {
        // Create the DSOFile attribute.
        attr = createDSOFile((MleDwpDSOFile *)item, parent);
    } else if (item->isa(MleDwpHeaderFile::typeId))
    {
        // Create the HeaderFile attribute.
        attr = createHeaderFile((MleDwpHeaderFile *)item, parent);
    } else if (item->isa(MleDwpSourceFile::typeId))
    {
        // Create the SourceFile attribute.
        attr = createSourceFile((MleDwpSourceFile *)item, parent);
    } else if (item->isa(MleDwpPackage::typeId))
    {
        // Create the Package attribute.
        attr = createPackage((MleDwpPackage *)item, parent);
    } else if (item->isa(MleDwpRoleBinding::typeId))
    {
        // Create the RoleBinding attribute.
        attr = createRoleBinding((MleDwpRoleBinding *)item, parent);
    } else if (item->isa(MleDwpRoleAttachment::typeId))
    {
        // Create the RoleAttachment attribute.
        attr = createRoleAttachment((MleDwpRoleAttachment *)item, parent);
    } else if (item->isa(MleDwpRoleSetMapping::typeId))
    {
        // Create the RoleSetMapping attribute.
        attr = createRoleSetMapping((MleDwpRoleSetMapping *)item, parent);
    } else if (item->isa(MleDwpActor::typeId))
    {
        // Create the Actor attribute.
        attr = createActor((MleDwpActor *)item, parent);
    } else if (item->isa(MleDwpGroup::typeId))
    {
        // Create the Group attribute.
        attr = createGroup((MleDwpGroup *)item, parent);
    } else if (item->isa(MleDwpProperty::typeId))
    {
        // Create the Property attribute.
        attr = createProperty((MleDwpProperty *)item, parent);
    } else if (item->isa(MleDwpMediaRef::typeId))
    {
        // Create the MediaRef attribute.
        attr = createMediaRef((MleDwpMediaRef *)item, parent);
    } else if (item->isa(MleDwpMediaRefSource::typeId))
    {
        // Create the MediaRefSource attribute.
        attr = createMediaRefSource((MleDwpMediaRefSource *)item, parent);
    } else if (item->isa(MleDwpMediaRefTarget::typeId))
    {
        // Create the MediaRefTarget attribute.
        attr = createMediaRefTarget((MleDwpMediaRefTarget *)item, parent);
    } else if (item->isa(MleDwpMedia::typeId))
    {
        // Create the Media attribute.
        attr = createMedia((MleDwpMedia *)item, parent);
    } else if (item->isa(MleDwpMediaRefClass::typeId))
    {
        // Create the MediaRefClass attribute.
        attr = createMediaRefClass((MleDwpMediaRefClass *)item, parent);
    } else if (item->isa(MleDwpStageDef::typeId))
    {
        // Create the StageDef attribute.
        attr = createStageDef((MleDwpStageDef *)item, parent);
    } else if (item->isa(MleDwpStage::typeId))
    {
        // Create the Stage attribute.
        attr = createStage((MleDwpStage *)item, parent);
    } else if (item->isa(MleDwpScene::typeId))
    {
        // Create the Scene attribute.
        attr = createScene((MleDwpScene *)item, parent);
    } else if (item->isa(MleDwpBoot::typeId))
    {
        // Create the Boot attribute.
        attr = createBoot((MleDwpBoot *)item, parent);
    } else if (item->isa(MleDwpGroupRef::typeId))
    {
        // Create the GroupRef attribute.
        attr = createGroupRef((MleDwpGroupRef *)item, parent);
    }

    // Add the Attribute to the DWP domain table.
    if (attr != nullptr)
    {
        //addAttribute(attr, parent);
        QVector<QtDwpTreeItem *> columnData;
        columnData.append(attr);
        parent->insertChildren(parent->childCount(), 1, columnData);

        // Add the tags to the attribute.
        //addAttributeTags(item, attr);
    }

    /* Recurse over children. */
    MleDwpItem *child = item->getFirstChild();
    while (child)
    {
        /* Build up child attributes */
        if (this->setupModelData(child, attr))
        {
            rval = true;
        }
         child = child->getNext();
    }

    return rval;
}

bool
QtDwpModel::removeModelData(const MleDwpItem *item)
{
    // Todo - Implement
    // Find matching QtDwpAttribute in mRootItem.
    // Remove children.
    // Remove this item.

    return false;
}

QtDwpTreeItem *
QtDwpModel::getItem(const QModelIndex &index) const
{
    if (index.isValid()) {
        QtDwpTreeItem *item = static_cast<QtDwpTreeItem*>(index.internalPointer());
        if (item)
            return item;
    }
    return mRootItem;
}

QVariant
QtDwpModel::data(const QModelIndex &index, int role) const
{
    if (! index.isValid())
        return QVariant();

    if (role != Qt::DisplayRole && role != Qt::EditRole)
        return QVariant();

    QtDwpTreeItem *item = getItem(index);

    return item->data(index.column(), role);
}

QVariant
QtDwpModel::headerData(int section, Qt::Orientation orientation,
                       int role) const
{
    if (orientation == Qt::Horizontal && role == Qt::DisplayRole)
        return mRootItem->data(section, role);

    return QVariant();
}

Qt::ItemFlags
QtDwpModel::flags(const QModelIndex &index) const
{
    Qt::ItemFlags flags;

    if (! index.isValid())
        return Qt::NoItemFlags;

    int column = index.column();
    if (column == 0) {
        // The first column in the model is readonly and not editable
        // or selectable.
        //flags = QAbstractItemModel::flags(index) & (~Qt::ItemIsEnabled);
        flags = QAbstractItemModel::flags(index) | Qt::ItemIsEnabled | Qt::ItemIsSelectable;
    } else {
        //int row = index.row();
        //std::cout << "Processing row = " << row << " column = " <<  column << std::endl;
        QtDwpTreeItem *item = getItem(index);
        flags = item->flags(column);
    }

    return flags;
}

int
QtDwpModel::rowCount(const QModelIndex &parent) const
{
    const QtDwpTreeItem *parentItem = getItem(parent);

    return parentItem ? parentItem->childCount() : 0;
}

int
QtDwpModel::columnCount(const QModelIndex &parent) const
{
    Q_UNUSED(parent);
    return mRootItem->columnCount();
}

bool
QtDwpModel::setData(const QModelIndex &index, const QVariant &value, int role)
{
    if (role != Qt::EditRole)
        return false;

    QtDwpTreeItem *item = getItem(index);
    bool result = item->setData(index.column(), value);

    if (result)
        emit dataChanged(index, index, {Qt::DisplayRole, Qt::EditRole});

    return result;
}

bool
QtDwpModel::setHeaderData(int section, Qt::Orientation orientation,
                          const QVariant &value, int role)
{
    if (role != Qt::EditRole || orientation != Qt::Horizontal)
        return false;

    const bool result = mRootItem->setData(section, value);

    if (result)
        emit headerDataChanged(orientation, section, section);

    return result;
}

QModelIndex
QtDwpModel::index(int row, int column, const QModelIndex &parent) const
{
    if (parent.isValid() && parent.column() != 0)
        return QModelIndex();

    QtDwpTreeItem *parentItem = getItem(parent);
    if (! parentItem)
        return QModelIndex();

    QtDwpTreeItem *childItem = parentItem->child(row);
    if (childItem)
        return createIndex(row, column, childItem);

    return QModelIndex();
}

bool
QtDwpModel::insertColumns(int position, int columns, const QModelIndex &parent)
{
    beginInsertColumns(parent, position, position + columns - 1);
    const bool success = mRootItem->insertColumns(position, columns);
    endInsertColumns();

    return success;
}

bool
QtDwpModel::insertRows(int position, int rows, const QModelIndex &parent)
{
    QtDwpTreeItem *parentItem = getItem(parent);
    if (! parentItem)
        return false;

    beginInsertRows(parent, position, position + rows - 1);
    // Insert empty column data.
    QVector<QVariant> data(mRootItem->columnCount());
    QVector<QtDwpTreeItem *> items;
    for (int column = 0; column <  mRootItem->columnCount(); column++)
        items[column] = new QtDwpTreeItem(data, parentItem);
    const bool success = parentItem->insertChildren(position, rows, items);
    endInsertRows();

    return success;
}

bool
QtDwpModel::removeColumns(int position, int columns, const QModelIndex &parent)
{
    beginRemoveColumns(parent, position, position + columns - 1);
    const bool success = mRootItem->removeColumns(position, columns);
    endRemoveColumns();

    if (mRootItem->columnCount() == 0)
        removeRows(0, rowCount());

    return success;
}

bool
QtDwpModel::removeRows(int position, int rows, const QModelIndex &parent)
{
    QtDwpTreeItem *parentItem = getItem(parent);
    if (! parentItem)
        return false;

    beginRemoveRows(parent, position, position + rows - 1);
    const bool success = parentItem->removeChildren(position, rows);
    endRemoveRows();

    return success;
}

QModelIndex
QtDwpModel::parent(const QModelIndex &index) const
{
    if (! index.isValid())
        return QModelIndex();

    QtDwpTreeItem *childItem = getItem(index);
    QtDwpTreeItem *parentItem = childItem ? childItem->parent() : nullptr;

    if (parentItem == mRootItem || !parentItem)
        return QModelIndex();

    return createIndex(parentItem->childNumber(), 0, parentItem);
}

void
QtDwpModel::setDwp(const MleDwpItem *item)
{
    if (mDwp != nullptr) {
        // Remove the old model.
        removeModelData(item);
    }
    setupModelData(item, mRootItem);
    mDwp = item;
}

void
QtDwpModel::setModified(bool value)
{
    mIsModified = value;
    emit QtDwpModel::contentsChanged();
}

QtDwpAttribute *
QtDwpModel::addAttribute(const QtDwpAttribute::AttributeType type, QtDwpAttribute *parent)
{
    //this->beginResetModel();
    QModelIndex index = findAttribute(parent);
    if (! index.isValid()) return nullptr;
    int first = rowCount(index);
    int last = first;

    this->beginInsertRows(index, first, last);

    // Create a new attribute of the specified type.
    QtDwpAttribute *attr = nullptr;
    if (type == QtDwpAttribute::DWP_ATTRIBUTE_SOURCEFILE) {
        MleDwpSourceFile *item = new MleDwpSourceFile();
        item->setName("SourceFile");
        item->setFilename("SourceFile.cpp");
        attr = this->createSourceFile(item, parent);
    }

    // Add the Attribute to the DWP domain table.
    if (attr != nullptr)
    {
        QVector<QtDwpTreeItem *> columnData;
        columnData.append(attr);
        parent->insertChildren(parent->childCount(), 1, columnData);
    }

    //this->endResetModel();
    this->endInsertRows();

    return attr;
}

void
QtDwpModel::deleteAttribute(const QtDwpAttribute *attr)
{
    // Find the index for the attribute.
    QModelIndex index = findAttribute(attr);
    if (! index.isValid())
        return;

    // Get the index for the parent.
    QModelIndex parentIndex = this->parent(index);

    // Remove the model rows.
    int position = 0;
    removeRows(position, 1, parentIndex);

    // Todo: Update DWP item hierarchy.

    setModified(true);
}

QModelIndex
QtDwpModel::findAttribute(const QtDwpAttribute *attr, const QModelIndex &parent)
{
    if (attr == nullptr)
        // Return an invalid index.
        return QModelIndex();

    int numRows = rowCount(parent);

    for (int row = 0; row < numRows; row++) {
        QModelIndex next = this->index(row, 0, parent);
        QtDwpTreeItem *item = static_cast<QtDwpTreeItem *>(next.internalPointer());
        if (attr == item)
            return next;

        if (this->hasChildren(next)) {
            QModelIndex found = this->findAttribute(attr, next);
            if (found.isValid()) return found;
        }
    }

    // Not found, return an invalid index.
    return QModelIndex();
}
