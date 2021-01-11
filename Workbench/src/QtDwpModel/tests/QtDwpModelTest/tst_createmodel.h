#ifndef TST_CREATEMODEL_H
#define TST_CREATEMODEL_H

#include <gtest/gtest.h>
#include <gmock/gmock-matchers.h>

#include "QtDwpModel.h"
#include "mle/DwpItem.h"

using namespace testing;

TEST(QtDwpModel, CreateModel)
{
    QStringList headers;
    MleDwpItem *dwp = nullptr;

    // Test creation of an empty DWP model.
    QtDwpModel *dwpModel = new QtDwpModel(headers, dwp);

    ASSERT_NE(dwpModel, nullptr);
    ASSERT_EQ(dwpModel->getDwp(), nullptr);
    EXPECT_EQ(dwpModel->rowCount(),0);
    EXPECT_EQ(dwpModel->columnCount(), 0);

    delete dwpModel;

    // Test empty DWP model with initial column setup.
    headers.append("Name");
    headers.append("Type");
    headers.append("Value");
    dwpModel = new QtDwpModel(headers, dwp);

    ASSERT_NE(dwpModel, nullptr);
    ASSERT_EQ(dwpModel->getDwp(), nullptr);
    EXPECT_EQ(dwpModel->rowCount(),0);
    EXPECT_EQ(dwpModel->columnCount(), 3);

    delete dwpModel;
}

#endif // TST_CREATEMODEL_H
