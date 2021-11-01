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


#ifndef __QTDWPMODEL_H_
#define __QTDWPMODEL_H_

// Include Qt header files.
#include <QAbstractItemModel>
#include <QModelIndex>
#include <QVariant>

// Include DWP Model header files.
#include "QtDwpModel_global.h"
#include "QtDwpNameAttribute.h"
#include "QtDwpNameTypeAttribute.h"

class QtDwpTreeItem;

class MleDwpActor;
class MleDwpActorDef;
class MleDwpBoot;
class MleDwpDSOFile;
class MleDwpGroup;
class MleDwpGroupRef;
class MleDwpHeaderFile;
class MleDwpInclude;
class MleDwpItem;
class MleDwpMedia;
class MleDwpMediaRef;
class MleDwpMediaRefClass;
class MleDwpMediaRefSource;
class MleDwpMediaRefTarget;
class MleDwpPackage;
class MleDwpProperty;
class MleDwpPropertyDef;
class MleDwpRoleAttachment;
class MleDwpRoleBinding;
class MleDwpRoleDef;
class MleDwpRoleSetMapping;
class MleDwpScene;
class MleDwpSet;
class MleDwpSetDef;
class MleDwpSourceFile;
class MleDwpStage;
class MleDwpStageDef;

/**
 * @brief The QtDwpModel class is used to construct and manage the
 * DWP model.
 */
class QTDWPMODEL_EXPORT QtDwpModel : public QAbstractItemModel
{
    Q_OBJECT

  public:

    /**
     * @brief The default constructor.
     */
    QtDwpModel() {}

    /**
     * @brief A constructor specifiying the <i>headers</i> and
     * initialization <i>data</i> for the DWP model.
     *
     * @param headers The list of headers for the model.
     * @param data A pointer to a Digital Workprint for the model data.
     * @param parent A pointer to the parent object for this model.
     * By default it will be <b>nullptr</b>
     */
    QtDwpModel(const QStringList &headers, const MleDwpItem *data,
               QObject *parent = nullptr);

    /**
     * The destructor.
     */
    virtual ~QtDwpModel();

    /**
     * @brief Retrieve the data for the specified model <i>index</i>
     * and <i>role</i>.
     *
     * @param index The model index used retireve the data.
     * @param role The model role used to retrieve the data. Must be
     * <b>Qt::DisplayRole</b> or <b>Qt::EditRole</b>.
     *
     * @return The data is returned for the specifed index and role.
     * If the <i>index</i> is not valid, then an invalid <b>QVariant</b> is returned.
     * If the <i>role</i> is not <b>Qt::DisplayRole</b> or <b>Qt::EditRole</b> then
     * an invalid <b>QVariant</b> will be returned.
     */
    QVariant data(const QModelIndex &index, int role) const override;

    /**
     * @brief Retrieve the header data for the specified <i>section</i>,
     * <i>orientation</i> and <i>role</i>.
     *
     * @param section The column index for the header data location.
     * @param orientation The header orientation. Must be <b>Qt::Horizontal</b>.
     * @param role The role for the header. Must be <b>Qt::DisplayRole</b>.
     *
     * @return The header data is returned for the specifed <i>section</i>,
     * <i>orientation</i>, and <i>role</i>. An invalid <b>QVariant</b> will
     * be returned if the input constratins are not met.
     */
    QVariant headerData(int section, Qt::Orientation orientation,
                        int role = Qt::DisplayRole) const override;

    /**
     * @brief Returns the model index of the item in the model specified by the
     * given <i>row</i>, <i>column</i> and <i>parent</i> index.
     *
     * @param row The row number.
     * @param column The column number.
     * @param parent The model index for the parent.
     *
     * @return The model index is reterned. An invalid <b>QModelIndex</b> will be
     * returned if the parent item can not be resolved.
     */
    QModelIndex index(int row, int column,
                      const QModelIndex &parent = QModelIndex()) const override;

    /**
     * @brief Retrieve the parent model index for the specifed <i>index</i>.
     *
     * @param index The model index.
     *
     * @return Returns the parent of the model item with the given index. If the
     * item has no parent, an invalid <b>QModelIndex</b> is returned.
     */
    QModelIndex parent(const QModelIndex &index) const override;

    /**
     * @brief Returns the number of rows under the given <i>parent</i>. When the
     * parent is valid it means that rowCount is returning the number of
     * children of parent.
     *
     * @param parent The model index for the parent item.
     *
     * @return An integer indicating the number of rows will be returned.
     */
    int rowCount(const QModelIndex &parent = QModelIndex()) const override;

    /**
     * @brief Returns the number of columns for the children of the given
     * <i>parent</i>.
     *
     * @param parent The model index for the parent item.
     *
     * @return An integer indicating the number of columns will be returned.
     */
    int columnCount(const QModelIndex &parent = QModelIndex()) const override;

    /**
     * @brief Returns the item flags for the given <i>index</i>.
     *
     * @param index The model index.
     *
     * @return The flags for the item located by <i>index</i> is returned.
     */
    Qt::ItemFlags flags(const QModelIndex &index) const override;

    /**
     * @brief Sets the <i>role</i> data for the item at <i>index</i>
     * to <i>value</i>.
     *
     * The <i>dataChanged()</i> signal will be emitted if the data is
     * successfully set.
     *
     * @param index The model index for the item to set.
     * @param value The value to set
     * @param role The role that is being set. By default, <b>Qt::EditRole</b>
     * will be used.
     *
     * @return Returns <b>true</b> if successful; otherwise returns <b>false</b>.
     */
    bool setData(const QModelIndex &index, const QVariant &value,
                 int role = Qt::EditRole) override;

    /**
     * @brief Sets the data for the given <i>role</i> and <i>section</i>
     * in the header with the specified <i>orientation</i> to the
     * <i>value</i> supplied.
     *
     * @param section The section, or column, in the header to set.
     * @param orientation The orientation of the header. Must be
     * <b>Qt::Horizontal</b>
     * @param value The value of the data to set.
     * @param role The role of the data to set; must be <b>Qt::EditRole</b>.
     *
     * @return Returns <b>true</b> if the header's data was updated;
     * otherwise returns <b>false</b>.
     */
    bool setHeaderData(int section, Qt::Orientation orientation,
                       const QVariant &value, int role = Qt::EditRole) override;

    /**
     * @brief Inserts <i>columns</i> new columns into the model before the
     * given column <i>position</i>.
     * <p>
     * The items in each new column will be children of the item represented by
     * the <i>parent</i> model index.
     * </p><p>
     * If <i>position</i> is 0, the columns are prepended to any existing columns.
     * <p></p>
     * If <i>postion</i> is <i>columnCount()</i>, the columns are appended to any
     * existing columns.
     * <p></p>
     * If <i>parent</i> has no children, a single row with count columns is inserted.
     * </p>
     *
     * @param position The position of the column to start insertion.
     * @param columns The number, or count, of columns to insert.
     * @param parent The parent model index.
     *
     * @return Returns <b>true</b> if the columns were successfully inserted;
     * otherwise returns <b>false</b>.
     */
    bool insertColumns(int position, int columns,
                       const QModelIndex &parent = QModelIndex()) override;

    /**
     * @brief Removes count <i>columns</i> starting with the given column
     * <i>position</i> under parent <i>parent</i> from the model.
     *
     * @param position The position of the columns to remove.
     * @param columns The number of columns to remove.
     * @param parent The parent model index.
     *
     * @return Returns <b>true</b> if the columns were successfully removed;
     * otherwise returns <b>false</b>.
     */
    bool removeColumns(int position, int columns,
                       const QModelIndex &parent = QModelIndex()) override;

    /**
     * @brief Inserts count <i>rows</i> into the model before the given row
     * <i>position</i>.
     * <p>
     * Items in the new row will be children of the item represented by the parent model index.
     * <p></p>
     * If <i>position</i> is 0, the rows are prepended to any existing rows in the parent.
     * <p></p>
     * If <i>postition</i> is <i>rowCount()</i>, the rows are appended to any existing rows in the parent.
     * <p></p>
     * If <i>parent</i> has no children, a single column with count <i>rows</i> is inserted.
     * </p>

     * @param position The position of the row to insert.
     * @param rows The number of rows to insert.
     * @param parent The parent model index.
     *
     * @return Returns <b>true</b> if the rows were successfully inserted;
     * otherwise returns <b>false</b>.
     */
    bool insertRows(int position, int rows,
                    const QModelIndex &parent = QModelIndex()) override;

    /**
     * @brief Removes count <i>rows</i> starting with the given row <i>postion</i>
     * under parent <i>parent</i> from the model.
     *
     * @param position The position of the rows to remove.
     * @param rows The number of rows to remove.
     * @param parent The parent model index.
     *
     * @return Returns <b>true</b> if the rows were successfully removed;
     * otherwise returns <b>false</b>.
     */
    bool removeRows(int position, int rows,
                    const QModelIndex &parent = QModelIndex()) override;

    /**
     * @brief Get the associated Digital Workprint item.
     *
     * @return A pointer to the Digital Workprint heiarchy is returned.
     * The retun value may be <code>nullptr</code>.
     */
    const MleDwpItem *getDwp() { return mDwp; }

    /**
     * @brief Set the associated Digital Workprint item.
     * <p>
     * If an existing model has already been established, then
     * it will be deleted and replaced by this one.
     * </p>
     *
     * @param item The associated Digital Workprint to set.
     */
    void setDwp(const MleDwpItem *item);

    /**
     * @brief Get the root of the model.
     *
     * @return A pointer to the root <code>QtDwpTreeItem</code> is returned.
     */
    QtDwpTreeItem *getRoot() { return mRootItem; }

    /**
     *@ brief  Get the item for the specified model index.
     *
     * @param index The model index used to retrieve the tree item.
     *
     * @return A pointer to the tree item located at the specifed index
     * will be returned.
     */
    QtDwpTreeItem *getItem(const QModelIndex &index) const;

    QtDwpTreeItem *addAttribute(const QtDwpAttribute::AttributeType type);

    void deleteAttribute(const QtDwpAttribute *attr);

    /**
     * @brief Set the model as modified or not.
     *
     * @param value If <b>true</b>, then the model will be flagged as being
     * modified. If <b>false</b>, then the model will be flagged as not
     * being modified.
     */
    void setModified(bool value);

    /**
     * @brief Check whether the model has been modified.
     *
     * @return <b>true</b> will be returned if the model has been modified.
     * Otherwise <b>false</b> will be returned.
     */
    bool isModified()
    { return mIsModified; }

  signals:

    /**
     * @brief A Signal indicating that the contents of the model has changed.
     */
    void contentsChanged();

  protected:

    QtDwpAttribute *createActor(MleDwpActor *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createActorDef(MleDwpActorDef *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createBoot(MleDwpBoot *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createDSOFile(MleDwpDSOFile *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createGroup(MleDwpGroup *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createGroupRef(MleDwpGroupRef *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createHeaderFile(MleDwpHeaderFile *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createInclude(MleDwpInclude *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createMedia(MleDwpMedia *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createMediaRef(MleDwpMediaRef *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createMediaRefClass(MleDwpMediaRefClass *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createMediaRefSource(MleDwpMediaRefSource *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createMediaRefTarget(MleDwpMediaRefTarget *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createPackage(MleDwpPackage *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createProperty(MleDwpProperty *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createPropertyDef(MleDwpPropertyDef *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createRoleAttachment(MleDwpRoleAttachment *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createRoleBinding(MleDwpRoleBinding *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createRoleDef(MleDwpRoleDef *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createRoleSetMapping(MleDwpRoleSetMapping *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createScene(MleDwpScene *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createSet(MleDwpSet *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createSetDef(MleDwpSetDef *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createSourceFile(MleDwpSourceFile *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createStage(MleDwpStage *item, QtDwpTreeItem *parent);
    QtDwpAttribute *createStageDef(MleDwpStageDef *item, QtDwpTreeItem *parent);

    QModelIndex findAttribute(const QtDwpAttribute *attr);

  private:

    // Setup the model data for this object.
    //void setupModelData(const QStringList &lines, QtDwpTreeItem *parent);
    bool setupModelData(const MleDwpItem *item, QtDwpTreeItem *parent);

    // Remove the model data for the sepcified item.
    bool removeModelData(const MleDwpItem *item);

    // The root item for the DWP model.
    QtDwpTreeItem *mRootItem;

    // The associated Digital Workprint.
    const MleDwpItem *mDwp;

    // Flag indicating whether the model has been modified since initial
    // setup configuration.
    bool mIsModified;
};

#endif /* __QTDWPMODEL_H_ */
