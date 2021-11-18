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

#include <QDebug>

#include "DwpActorContextMenu.h"

DwpActorContextMenu::DwpActorContextMenu(QObject *parent)
    : DwpContextMenu(parent),
      addHeaderFileAction(nullptr),
      addSourceFileAction(nullptr),
      addPackageAction(nullptr),
      addRoleBindingAction(nullptr),
      addIntPropertyAction(nullptr),
      addFloatPropertyAction(nullptr),
      addStringPropertyAction(nullptr),
      addVector2PropertyAction(nullptr),
      addVector3PropertyAction(nullptr),
      addVector4PropertyAction(nullptr),
      addRotationPropertyAction(nullptr),
      addTransformPropertyAction(nullptr)
{
    // Do nothing extra.
}

DwpActorContextMenu::~DwpActorContextMenu()
{
    if (addHeaderFileAction != nullptr) delete addHeaderFileAction;
    if (addSourceFileAction != nullptr) delete addSourceFileAction;
    if (addPackageAction != nullptr) delete addPackageAction;
    if (addRoleBindingAction != nullptr) delete addRoleBindingAction;
    if (addIntPropertyAction != nullptr) delete addIntPropertyAction;
    if (addFloatPropertyAction != nullptr) delete addFloatPropertyAction;
    if (addStringPropertyAction != nullptr) delete addStringPropertyAction;
    if (addVector2PropertyAction != nullptr) delete addVector2PropertyAction;
    if (addVector3PropertyAction != nullptr) delete addVector3PropertyAction;
    if (addVector4PropertyAction != nullptr) delete addVector4PropertyAction;
    if (addRotationPropertyAction != nullptr) delete addRotationPropertyAction;
    if (addTransformPropertyAction != nullptr) delete addTransformPropertyAction;
}

void
DwpActorContextMenu::init(QtDwpAttribute *attr)
{
    // Call super class method.
    DwpContextMenu::init(attr);

    if (mUseJava) {
        // Support for Java and Android Digital Workprints.

        addPackageAction = new QAction(tr("Add DWP Package Item"), this);
        //addPackageAction->setShortcuts(QKeySequence::New);
        addPackageAction->setStatusTip(tr("Create a new Package item"));
        connect(addPackageAction, &QAction::triggered, this, &DwpActorContextMenu::addPackage);
        mMenu->addAction(addPackageAction);
        //mMenu->addAction("Add DWP Package Item");
        if (attr->hasAttributeType(QtDwpAttribute::DWP_ATTRIBUTE_PACKAGE))
            addPackageAction->setEnabled(false);
    } else {
        // Support for C/C++ Digital Workprints.

        addHeaderFileAction = new QAction(tr("Add DWP HeaderFile Item"), this);
        //addHeaderFileAction->setShortcuts(QKeySequence::New);
        addHeaderFileAction->setStatusTip(tr("Create a new HeaderFile item"));
        connect(addHeaderFileAction, &QAction::triggered, this, &DwpActorContextMenu::addHeaderFile);
        mMenu->addAction(addHeaderFileAction);
        //mMenu->addAction("Add DWP HeaderFile Item");
        if (attr->hasAttributeType(QtDwpAttribute::DWP_ATTRIBUTE_HEADERFILE))
            addHeaderFileAction->setEnabled(false);

        addSourceFileAction = new QAction(tr("Add DWP SourceFile Item"), this);
        //action->setShortcuts(QKeySequence::New);
        addSourceFileAction->setStatusTip(tr("Create a new SourceFile item"));
        connect(addSourceFileAction, &QAction::triggered, this, &DwpActorContextMenu::addSourceFile);
        mMenu->addAction(addSourceFileAction);
        //mMenu->addAction("Add DWP SourceFile Item");
        if (attr->hasAttributeType(QtDwpAttribute::DWP_ATTRIBUTE_SOURCEFILE))
            addSourceFileAction->setEnabled(false);
    }

    addRoleBindingAction = new QAction(tr("Add DWP RoleBinding Item"), this);
    //addRoleBindingAction->setShortcuts(QKeySequence::New);
    addRoleBindingAction->setStatusTip(tr("Create a new RoleBinding item"));
    connect(addRoleBindingAction, &QAction::triggered, this, &DwpActorContextMenu::addRoleBinding);
    mMenu->addAction(addRoleBindingAction);
    //mMenu->addAction("Add DWP RoleBinding Item");
    if (attr->hasAttributeType(QtDwpAttribute::DWP_ATTRIBUTE_ROLEBINDING))
        addRoleBindingAction->setEnabled(false);

    /*
    addPropertyAction = new QAction(tr("Add DWP Property Item"), this);
    //addPropertyAction->setShortcuts(QKeySequence::New);
    addPropertyAction->setStatusTip(tr("Create a new Property item"));
    connect(addPropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addProperty);
    mMenu->addAction(addPropertyAction);
    //mMenu->addAction("Add DWP Property Item");
    */

    QMenu *propertyMenu = mMenu->addMenu("Add DWP Property Item");
    addIntPropertyAction = propertyMenu->addAction( "Add Integer Property" );
    connect(addIntPropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addIntProperty);
    addFloatPropertyAction = propertyMenu->addAction( "Add Float Property" );
    connect(addFloatPropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addFloatProperty);
    addStringPropertyAction = propertyMenu->addAction( "Add String Property" );
    connect(addStringPropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addStringProperty);
    addVector2PropertyAction = propertyMenu->addAction( "Add MlVector2 Property" );
    connect(addVector2PropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addMlVector2Property);
    addVector3PropertyAction = propertyMenu->addAction( "Add MlVector3 Property" );
    connect(addVector3PropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addMlVector3Property);
    addVector4PropertyAction = propertyMenu->addAction( "Add MlVector4 Property" );
    connect(addVector4PropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addMlVector4Property);
    addRotationPropertyAction = propertyMenu->addAction( "Add MlRotation Property" );
    connect(addRotationPropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addMlRotationProperty);
    addTransformPropertyAction = propertyMenu->addAction( "Add MlTransform Property" );
    connect(addTransformPropertyAction, &QAction::triggered, this, &DwpActorContextMenu::addMlTransformProperty);
}

void
DwpActorContextMenu::addHeaderFile()
{
    qDebug() << "DwpActorContextMenu: Adding DWP HeaderFile item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_HEADERFILE, mAttr);
}

void
DwpActorContextMenu::addSourceFile()
{
    qDebug() << "DwpActorContextMenu: Adding DWP SourceFile item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_SOURCEFILE, mAttr);
}

void
DwpActorContextMenu::addPackage()
{
    qDebug() << "DwpActorContextMenu: Adding DWP Package item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_PACKAGE, mAttr);
}

void
DwpActorContextMenu::addRoleBinding()
{
    qDebug() << "DwpActorContextMenu: Adding DWP RoleBinding item";
    emit DwpContextMenu::insertAttribute(QtDwpAttribute::DWP_ATTRIBUTE_ROLEBINDING, mAttr);
}

void
DwpActorContextMenu::addIntProperty()
{
    qDebug() << "DwpActorContextMenu: Adding DWP Integer Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_INT);
}

void
DwpActorContextMenu::addFloatProperty()
{
    qDebug() << "DwpActorContextMenu: Adding DWP Float Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_FLOAT);
}

void
DwpActorContextMenu::addStringProperty()
{
    qDebug() << "DwpActorContextMenu: Adding DWP String Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_STRING);
}

void
DwpActorContextMenu::addMlVector2Property()
{
    qDebug() << "DwpActorContextMenu: Adding DWP MlVector2 Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_VECTOR2);
}

void
DwpActorContextMenu::addMlVector3Property()
{
    qDebug() << "DwpActorContextMenu: Adding DWP MlVector3 Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_VECTOR3);
}

void
DwpActorContextMenu::addMlVector4Property()
{
    qDebug() << "DwpActorContextMenu: Adding DWP MlVector4 Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_VECTOR4);
}

void
DwpActorContextMenu::addMlRotationProperty()
{
    qDebug() << "DwpActorContextMenu: Adding DWP MlRotation Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_ROTATION);
}

void
DwpActorContextMenu::addMlTransformProperty()
{
    qDebug() << "DwpActorContextMenu: Adding DWP MlTransform Property item";
    emit DwpContextMenu::insertProperty(QtDwpAttribute::DWP_ATTRIBUTE_PROPERTY, mAttr, QtDwpAttribute::PropertyType::DWP_PROPERTY_TRANSFORM);
}
