// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2021 Wizzer Works
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
//  For information concerning this header file, contact Mark S. Millard,
//  of Wizzer Works at msm@wizzerworks.com.
//
//  More information concerning Wizzer Works may be found at
//
//      http://www.wizzerworks.com
//
// COPYRIGHT_END

#ifndef TST_DATATYPES_H
#define TST_DATATYPES_H

// Include Google Test header files.
#include <gtest/gtest.h>
#include <gmock/gmock-matchers.h>

// Include Magic Lantern header files.
#include "QtDwpModel.h"
#include "mle/DwpItem.h"
#include "mle/DwpLoad.h"

using namespace testing;

class DataTypesTestFixture: public ::testing::Test
{
  public:
    DataTypesTestFixture( ) {
         // Initialize DWP library.
         mleDwpInit();
    }

    void SetUp( ) {
        // Code here will execute just before the test ensues .
        if (g_workprint == NULL)
            g_workprint = mlLoadWorkprint("../workprints/TestDWPProperties.dwp");
    }

    void TearDown( ) {
        // Code here will be called just after the test completes.
        // Ok to throw exceptions from here if need be.

        // Todo: unload the Digital Workprint.
    }

    ~DataTypesTestFixture( )  {
        // Cleanup any pending stuff, but no exceptions allowed.

        // Todo: delete g_workprint.
    }

    // Put in any custom data members that you need.
    static MleDwpItem *g_workprint;
};

MleDwpItem *DataTypesTestFixture::g_workprint = NULL;

TEST_F(DataTypesTestFixture, CreateModel)
{
    QStringList headers;
    MleDwpItem *dwp = nullptr;

    // Test DWP data types.
    headers.append("DWP Item");
    headers.append("Name");
    headers.append("Type");
    headers.append("Value");
    dwp = DataTypesTestFixture::g_workprint;
    QtDwpModel *dwpModel = new QtDwpModel(headers, dwp);

    ASSERT_NE(dwpModel, nullptr);
    ASSERT_NE(dwpModel->getDwp(), nullptr);
    EXPECT_EQ(dwpModel->rowCount(),1);
    EXPECT_EQ(dwpModel->columnCount(), 4);

    QtDwpTreeItem *root = dwpModel->getRoot();
    root->traverse(QtDwpAttribute::dump, root, nullptr);

    delete dwpModel;
}

#endif // TST_DATATYPES_H
