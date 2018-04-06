/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wizzer.mle.studio.android.project;

import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Utility class that manages the AndroidManifest.xml file.
 */
public class AndroidManifestFromPath {

    private static XPathFactory sFactory = XPathFactory.newInstance();

    private File mFile;
    private XPath mXPath;

    /**
     * Creates an AndroidManifest based on a file path.
     *
     * Use Exists() to check if the manifest file really exists.
     *
     * @param osManifestFilePath
     */
    public AndroidManifestFromPath(String osManifestFilePath) {
        mFile = new File(osManifestFilePath);
        mXPath = sFactory.newXPath();
    }

    /**
     * Returns true either if an androidManifest.xml file was found in the project
     * or if the given file path exists.
     */
    public boolean exists() {
        return mFile != null && mFile.exists();
    }

     /**
     * Returns the package name defined in the manifest file.
     *
     * @return A String object with the package or null if any error happened.
     */
    public String getPackageName() {
        try {
            return getPackageNameInternal(mXPath, getSource());
        } catch (XPathExpressionException e) {
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * Returns the i-th activity defined in the manifest file.
     *
     * @param index The 1-based index of the activity to return.
     * @return A String object with the activity or null if any error happened.
     */
    public String getActivityName(int index) {
        try {
            return getActivityNameInternal(index, mXPath, getSource());
        } catch (XPathExpressionException e) {
        } catch (FileNotFoundException e) {
        }

        return null;
    }

    /**
     * Returns an InputSource for XPath.
     *
     * @throws FileNotFoundException if file does not exists.
     */
    private InputSource getSource() throws FileNotFoundException {
        return new InputSource(new FileReader(mFile));
    }

    /**
     * Performs the actual XPath evaluation to get the package name.
     * Extracted so that we can share it with AndroidManifestFromProject.
     */
    static String getPackageNameInternal(XPath xpath, InputSource source)
        throws XPathExpressionException {
        return xpath.evaluate("/manifest/@package", source);  //$NON-NLS-1$
    }

    /**
     * Performs the actual XPath evaluation to get the activity name.
     * Extracted so that we can share it with AndroidManifestFromProject.
     */
    static String getActivityNameInternal(int index, XPath xpath, InputSource source)
        throws XPathExpressionException {
        return xpath.evaluate("/manifest/application/activity[" //$NON-NLS-1$
                              + index
                              + "]/@class", //$NON-NLS-1$
                              source);
    }

}
