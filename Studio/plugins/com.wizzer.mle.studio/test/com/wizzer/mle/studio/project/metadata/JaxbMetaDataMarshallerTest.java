// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2021 Wizzer Works
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

// Declare package.
package com.wizzer.mle.studio.project.metadata;

// Import standard Java classes.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//Import JUnit classes.
import junit.framework.TestCase;

/**
 * This class is a JUnit test for the <code>JaxbMetaDataMarshaller</code>
 * class.
 * 
 * @author Mark S. Millard
 */
public class JaxbMetaDataMarshallerTest extends TestCase
{
	// The name of the project file to test.
	private String PROJECT_FILE_NAME = ".mleprojectTest";
	// Handle to project file.
	private File m_file = null;

	public JaxbMetaDataMarshallerTest(String name)
	{
		super(name);
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void setUp() throws Exception
	{
		m_file = new File(PROJECT_FILE_NAME);
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void tearDown() throws Exception
	{
		if (m_file != null)
		{
			if (m_file.exists())
				m_file.delete();
		}
		m_file = null;
	}

	/**
	 * Test method for {@link com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller#JaxbMetaDataMarshaller()}.
	 */
	public void testJaxbMetaDataMarshaller()
	{
		JaxbMetaDataMarshaller metaDataMarshaller = new JaxbMetaDataMarshaller();
		assertNotNull(metaDataMarshaller);
	}

	/**
	 * Test method for {@link com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller#marshallMetaData(java.io.OutputStream, com.wizzer.mle.studio.project.metadata.ProjectMetaData)}.
	 */
	public void testMarshallMetaData()
	{
		try {
			createProjectFile();
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller#unmarshallMetaData(java.io.InputStream)}.
	 */
	public void testUnmarshallMetaData()
	{
		JaxbMetaDataMarshaller marshaller = new JaxbMetaDataMarshaller();
		assertNotNull(marshaller);

		ProjectMetaData data = null;
		
		try {
			// Make sure there is a project file to read.
			if (! m_file.exists())
				createProjectFile();

			// Create an input stream.
			FileInputStream istream = new FileInputStream(m_file);
			
			// Read the meta-data.
			data = marshaller.unmarshallMetaData(istream);
			
			// Close the input stream.
			istream.close();
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (MetaDataException e) {
			fail(e.getMessage());
		}
		
		assertNotNull(data);
		assertTrue(data instanceof MleTitleMetaData);
		
		MleTitleMetaData titleData = (MleTitleMetaData) data;
		assertTrue(titleData.getMajorVersion() == MleTitleMetaData.MAJOR_VERSION);
		assertTrue(titleData.getMinorVersion() == MleTitleMetaData.MINOR_VERSION);
		assertTrue(titleData.getProjectType().equals(MleTitleMetaData.PROJECT_TYPE));
		assertTrue(titleData.getProjectTypeVersion().equals(MleTitleMetaData.PROJECT_TYPE_VERSION));
		assertNull(titleData.getCreationDate());
		
		Object[] projectData = titleData.getProjectData();
		assertEquals(projectData.length, 1);
		assertTrue(projectData[0] instanceof MleTitleMetaData.ProjectDataElement);
		
		MleTitleMetaData.ProjectDataElement projectElement = (MleTitleMetaData.ProjectDataElement) projectData[0];
		assertTrue(projectElement.getId().equals("www.wizzerworks.com.teapot.project"));
		assertTrue(projectElement.getVersion().equals("1.0"));
		assertTrue(projectElement.getDwpFile().equals("workprints/teapot.dwp"));
		
		Object[] targets = projectElement.getMasterTargets();
		assertNotNull(targets);
		assertEquals(targets.length, 1);
		assertTrue(targets[0] instanceof MleTitleMetaData.MasterTargetElement);
		
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targets[0];
		assertTrue(target.getType().equals("Java2D"));
		assertTrue(target.getId().equals("www.wizzerworks.com.teapot.target"));
		assertTrue(target.getDigitalPlayprint().equals("gen/teapot.dpp"));
		
		assertEquals(target.isVerbose(), false);
		assertEquals(target.isFixedPoint(), false);
		assertEquals(target.isBigEndian(), true);
		assertEquals(target.getDestinationDirectroy(), "src/gen");
		assertEquals(target.getCodeGeneration(), "java");
		assertTrue(target.hasGengroup());
		assertTrue(target.hasGenmedia());
		assertTrue(target.hasGenscene());
		assertTrue(target.hasGentables());
		assertTrue(target.hasGenppscript());
		assertTrue(target.hasGendpp());
		assertEquals(target.getActorIdFile(), "ActorID.java");
		assertEquals(target.getGroupIdFile(), "GroupID.java");
		assertEquals(target.getSceneIdFile(), "SceneID.java");
		assertEquals(target.getHeaderPackage(), "gen");
		assertEquals(target.getBomFile(), "MediaBom.txt");
		assertEquals(target.getScriptFile(), "playprint.py");
		assertEquals(target.getTocName(), "DppTOC");
		assertEquals(target.getSourceDirectory(), "src/gen");
		assertEquals(target.getScriptPath(), "src/gen/playprint.py");
	}
	
	private void createProjectFile() throws FileNotFoundException, IOException
	{
		JaxbMetaDataMarshaller marshaller = new JaxbMetaDataMarshaller();
		assertNotNull(marshaller);
		
		assertNotNull(m_file);

		// Create an new file output stream for the meta-data.
		FileOutputStream ostream = new FileOutputStream(m_file);
		assertNotNull(ostream);
		
		MleTitleMetaData data = new MleTitleMetaData();
		assertNotNull(data);
		assertEquals(MleTitleMetaData.PROJECT_TYPE, data.getProjectType());
		assertEquals(MleTitleMetaData.PROJECT_TYPE_VERSION, data.getProjectTypeVersion());
	
		// Create a new title entry.
		MleTitleMetaData.ProjectDataElement titleData = data.new ProjectDataElement();
		titleData.setId("www.wizzerworks.com.teapot.project");
		titleData.setDwpFile("workprints/teapot.dwp");
		titleData.setVersion("1.0");
		
		// Add a new mastering target.
		MleTitleMetaData.MasterTargetElement targetData = data.new MasterTargetElement();
		targetData.setType("Java2D");
		targetData.setId("www.wizzerworks.com.teapot.target");
		targetData.setDigitalPlayprint("gen/teapot.dpp");
		
		targetData.setVerbose(false);
		targetData.setBigEndian(true);
		targetData.setFixedPoint(false);
		targetData.setDestinationDirectory("src/gen");
		targetData.setCodeGeneration("java");
		targetData.setGengroup(true);
		targetData.setActorIdFile("ActorID.java");
		targetData.setGroupIdFile("GroupID.java");
		targetData.setGenscene(true);
		targetData.setSceneIdFile("SceneID.java");
		targetData.setGenmedia(true);
		targetData.setBomFile("MediaBom.txt");
		targetData.setGenppscript(true);
		targetData.setScriptFile("playprint.py");
		targetData.setTocName("DppTOC");
		targetData.setGendpp(true);
		targetData.setSourceDirectory("src/gen");
		targetData.setScriptPath("src/gen/playprint.py");
		targetData.setHeaderPackage("gen");
		
		MleTitleMetaData.MasterTargetElement[] targets = new MleTitleMetaData.MasterTargetElement[1];
		targets[0] = targetData;
		titleData.setMasterTargets(targets);
		
		// Add the title to the meta-data.
		Object[] titles = new Object[1];
		titles[0] = titleData;
		data.setProjectData(titles);
		
		// Write the meta-data.
		marshaller.marshallMetaData(ostream, data);
		assertTrue(m_file.exists());
		
		// Flush and close the output stream
		ostream.flush();
		ostream.close();
	}

}
