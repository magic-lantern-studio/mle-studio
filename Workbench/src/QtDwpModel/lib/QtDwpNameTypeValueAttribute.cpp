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

// Include Magic Lantern header files.
#include "mle/DwpItem.h"
#include "mle/DwpDatatype.h"
#include "mle/DwpProperty.h"
#include "mle/DwpVector2.h"
#include "mle/DwpVector3.h"
#include "mle/DwpVector4.h"
#include "mle/DwpIntArray.h"
#include "mle/DwpFloatArray.h"
#include "mle/DwpTransform.h"
#include "mle/DwpRotation.h"
#include "math/vector.h"
#include "math/rotation.h"
#include "math/transfrm.h"
#include "QtDwpNameTypeValueAttribute.h"
#include "QtMlVector2.h"
#include "QtMlVector3.h"
#include "QtMlVector4.h"
#include "QtMlRotation.h"
#include "QtMlTransform.h"

QtDwpNameTypeValueAttribute::QtDwpNameTypeValueAttribute(const QVector<QVariant> &data, QtDwpTreeItem *parent)
  :QtDwpAttribute(data, parent)
{
    // Do nothing extra.
}

QtDwpNameTypeValueAttribute::~QtDwpNameTypeValueAttribute()
{
    // Do nothing.
}

QVariant
QtDwpNameTypeValueAttribute::data(int column, int role) const
{
    if (column < 0 || column >= mItemData.size())
        return QVariant();

    // Todo: validate role.

    if (column == 3 && role == Qt::DisplayRole) {
        QVariant vData = mItemData.at(column);
        QString displayText = getValueAsString(vData);
        return displayText;
    }

    return mItemData.at(column);
}

void
QtDwpNameTypeValueAttribute::print()
{
    std::cout << "Printing QtDwpNameTypeValueAttribute" << std::endl;

    std::cout << "{" << std::endl;
    int columns = this->columnCount();
    for (int column = 0; column < columns; column++) {
        std::cout << "  { \"column" << column << "\" : ";
        QVariant data = this->data(column, -1);
        int vType = data.userType();
        switch (vType) {
            case QMetaType::QString: {
                QString str = getValueAsString(data);
                std::cout << str.toStdString();
                //std::cout << data.toString().toStdString();
                break;
            }
            case QMetaType::Int: {
                QString str = getValueAsString(data);
                std::cout << str.toStdString();
                //std::cout << data.toInt();
                break;
            }
            case QMetaType::Float: {
                QString str = getValueAsString(data);
                std::cout << str.toStdString();
                //std::cout << data.toFloat();
                break;
            }
            default: {
                const MleDwpItem *dwpItem = this->getDwpItem();
                if (dwpItem->isa(MleDwpProperty::typeId)) {
                    const MleDwpProperty *property = reinterpret_cast<const MleDwpProperty *>(dwpItem);
                    const MleDwpDatatype *dataType = property->getDatatype();

                    if (dataType != nullptr) {
                        if (dataType->isa(MleDwpVector2::typeId)) {
                            QString str = getValueAsString(data);
                            std::cout << "[" << str.toStdString() << "]";

                            /*
                            QtMlVector2 stored = data.value<QtMlVector2>();
                            MlVector2 value = stored.value();

                            std::cout << "[" <<  value[0] << "," << value[1] << "]";
                            */
                        } else if (dataType->isa(MleDwpVector3::typeId)) {
                            QString str = getValueAsString(data);
                            std::cout << "[" << str.toStdString() << "]";

                            /*
                            QtMlVector3 stored = data.value<QtMlVector3>();
                            MlVector3 value = stored.value();

                            std::cout << "[" <<  value[0] << "," << value[1] << "," << value[2] << "]";
                            */
                        } if (dataType->isa(MleDwpVector4::typeId)) {
                            QString str = getValueAsString(data);
                            std::cout << "[" << str.toStdString() << "]";

                            /*
                            QtMlVector4 stored = data.value<QtMlVector4>();
                            MlVector4 value = stored.value();

                            std::cout << "[" <<  value[0] << "," << value[1] << "," << value[2] << "," << value[3] << "]";
                            */
                        } if (dataType->isa(MleDwpIntArray::typeId)) {
                            QString str = getValueAsString(data);
                            std::cout << "[" << str.toStdString() << "]";

                            /*
                            QVector<int> qarray = data.value<QVector<int>>();

                            std::cout << "[";
                            for (int i = 0; i < qarray.size(); i ++) {
                                 std::cout << qarray[i];
                                 if (i != qarray.size() - 1)
                                     std::cout << ",";
                            }
                            std::cout << "]";
                            */
                        } if (dataType->isa(MleDwpFloatArray::typeId)) {
                            QString str = getValueAsString(data);
                            std::cout << "[" << str.toStdString() << "]";

                            /*
                            QVector<float> qarray = data.value<QVector<float>>();

                            std::cout << "[";
                            for (int i = 0; i < qarray.size(); i ++) {
                                 std::cout << qarray[i];
                                 if (i != qarray.size() - 1)
                                     std::cout << ",";
                            }
                            std::cout << "]";
                            */
                        } if (dataType->isa(MleDwpRotation::typeId)) {
                            QString str = getValueAsString(data);
                            std::cout << "[" << str.toStdString() << "]";

                            /*
                            QtMlRotation stored = data.value<QtMlRotation>();
                            MlRotation value = stored.value();

                            std::cout << "[" <<  value[0] << "," << value[1] << "," << value[2] << "," << value[3] << "]";
                            */
                        } if (dataType->isa(MleDwpTransform::typeId)) {
                            QString str = getValueAsString(data);
                            std::cout << "[" << str.toStdString() << "]";

                            /*
                            QtMlTransform stored = data.value<QtMlTransform>();
                            MlTransform value = stored.value();

                            std::cout << "[";
                            std::cout <<  value[0][0] << "," << value[0][1] << "," << value[0][2] << "," << value[0][3] << ",";
                            std::cout <<  value[1][0] << "," << value[1][1] << "," << value[1][2] << "," << value[1][3] << ",";
                            std::cout <<  value[2][0] << "," << value[2][1] << "," << value[2][2] << "," << value[2][3] << ",";
                            std::cout <<  value[3][0] << "," << value[3][1] << "," << value[3][2] << "," << value[3][3];
                            std::cout << "]";
                            */
                        }
                    }
                    break;
                }
            }  // case QVariant
        } // switch userType
        std::cout << " }" << std::endl;
    }
    std::cout << "{" << std::endl;
    std::cout << std::flush;
}

int
QtDwpNameTypeValueAttribute::dump(void *caller, void *calldata)
{
    QtDwpNameTypeValueAttribute *item = reinterpret_cast<QtDwpNameTypeValueAttribute *>(caller);
    item->print();
    return 1;
}

QString
QtDwpNameTypeValueAttribute::getValueAsString(QVariant vData) const
{
    QString str;

    int vType = vData.userType();
    switch (vType) {
        case QMetaType::QString: {
            str = vData.toString();
            break;
        }
        case QMetaType::Int: {
            str = vData.toInt();
            break;
        }
        case QMetaType::Float: {
            str = vData.toFloat();
            break;
        }
        default: {
            const MleDwpItem *dwpItem = getDwpItem();
            if (dwpItem->isa(MleDwpProperty::typeId)) {
                const MleDwpProperty *property = reinterpret_cast<const MleDwpProperty *>(dwpItem);
                const MleDwpDatatype *dataType = property->getDatatype();

                if (dataType != nullptr) {
                    if (dataType->isa(MleDwpVector2::typeId)) {
                        QtMlVector2 stored = vData.value<QtMlVector2>();
                        MlVector2 value = stored.value();

                        QString v0 = QString::number(value[0]);
                        QString v1 = QString::number(value[1]);
                        str = v0 + " " + v1;

                    } else if (dataType->isa(MleDwpVector3::typeId)) {
                        QtMlVector3 stored = vData.value<QtMlVector3>();
                        MlVector3 value = stored.value();

                        QString v0 = QString::number(value[0]);
                        QString v1 = QString::number(value[1]);
                        QString v2 = QString::number(value[2]);
                        str = v0 + " " + v1 + " " + v2;

                    } if (dataType->isa(MleDwpVector4::typeId)) {
                        QtMlVector4 stored = vData.value<QtMlVector4>();
                        MlVector4 value = stored.value();

                        QString v0 = QString::number(value[0]);
                        QString v1 = QString::number(value[1]);
                        QString v2 = QString::number(value[2]);
                        QString v3 = QString::number(value[3]);
                        str = v0 + " " + v1 + " " + v2 + " " +  v3;

                    } if (dataType->isa(MleDwpIntArray::typeId)) {
                        QVector<int> qarray = vData.value<QVector<int>>();

                        for (int i = 0; i < qarray.size(); i++) {
                             str += qarray[i];
                             if (i != qarray.size() - 1)
                                 str += " ";
                        }

                    } if (dataType->isa(MleDwpFloatArray::typeId)) {
                        QVector<float> qarray = vData.value<QVector<float>>();

                        for (int i = 0; i < qarray.size(); i++) {
                             str += qarray[i];
                             if (i != qarray.size() - 1)
                                 str += " ";
                        }

                    } if (dataType->isa(MleDwpRotation::typeId)) {
                        QtMlRotation stored = vData.value<QtMlRotation>();
                        MlRotation value = stored.value();

                        QString v0 = QString::number(value[0]);
                        QString v1 = QString::number(value[1]);
                        QString v2 = QString::number(value[2]);
                        QString v3 = QString::number(value[3]);
                        str = v0 + " " + v1 + " " + v2 + " " +  v3;

                    } if (dataType->isa(MleDwpTransform::typeId)) {
                        QtMlTransform stored = vData.value<QtMlTransform>();
                        MlTransform value = stored.value();

                        str += QString::number(value[0][0]) + " ";
                        str += QString::number(value[0][1]) + " ";
                        str += QString::number(value[0][2]) + " ";
                        str += QString::number(value[0][3]) + " ";
                        str += QString::number(value[1][0]) + " ";
                        str += QString::number(value[1][1]) + " ";
                        str += QString::number(value[1][2]) + " ";
                        str += QString::number(value[1][3]) + " ";
                        str += QString::number(value[2][0]) + " ";
                        str += QString::number(value[2][1]) + " ";
                        str += QString::number(value[2][2]) + " ";
                        str += QString::number(value[2][3]) + " ";
                        str += QString::number(value[3][0]) + " ";
                        str += QString::number(value[3][1]) + " ";
                        str += QString::number(value[3][2]) + " ";
                        str += QString::number(value[3][3]);

                    }
                }
                break;
            }
        }  // case QVariant
    } // switch userType

    return str;
}
