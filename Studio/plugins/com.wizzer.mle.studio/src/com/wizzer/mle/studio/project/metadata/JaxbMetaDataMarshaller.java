// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2020 Wizzer Works
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

// Import JAXB classes.
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

// Import JAXB binding classes.
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData.MasterTargetElement;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData.ProjectDataElement;
import com.wizzer.mle.studio.project.metadata.jaxb.Project;
import com.wizzer.mle.studio.project.metadata.jaxb.ObjectFactory;
import com.wizzer.mle.studio.project.metadata.jaxb.Target;
import com.wizzer.mle.studio.project.metadata.jaxb.TargetTools;
import com.wizzer.mle.studio.project.metadata.jaxb.Title;

/**
 * This class is the JAXB marshaller for the Magic Lantern project
 * meta-data file.
 * 
 * @author Mark S. Millard
 */
public class JaxbMetaDataMarshaller implements IMetaDataMarshaller
{
    /* Context path for JAXBContext. */
    private static final String CONTEXT_PATH = "com.wizzer.mle.studio.project.metadata.jaxb";

	/**
     * The default constructor.
     */
    public JaxbMetaDataMarshaller()
    {
        super();
    }

    /**
     * Marshall the specified <code>ProjectMetaData</code> to the specified
     * output stream.
     * 
     * @param os The output stream.
     * @param data The project meta-data to marshall.
     * 
     * @see com.wizzer.mle.studio.project.metadata.IMetaDataMarshaller#marshallMetaData(java.io.OutputStream, com.enabletv.vwb.platform.ocap.project.metadata.ProjectMetaData)
     */
	public void marshallMetaData(OutputStream os, ProjectMetaData data)
	    throws IOException
	{
	    try
	    {
	        JAXBContext jc = JAXBContext.newInstance(CONTEXT_PATH, getClass().getClassLoader());
	        Marshaller m = jc.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

	        m.marshal(convertMetaData(data), os);
	    } catch (Exception ex)
	    {
	        throw new IOException(ex.getMessage());
	    }
	}

	/**
     * Unmarshall the specified input stream into project meta-data.
     * 
     * @param is The input stream.
     * 
     * @return The unmarshalled project meta-data is returned as
     * an instance of <code>ProjectMetaData</code>.
     * 
     * @see com.wizzer.mle.studio.project.metadata.IMetaDataMarshaller#unmarshallMetaData(java.io.InputStream)
     */
	public ProjectMetaData unmarshallMetaData(InputStream is)
		throws IOException, MetaDataException
	{
		ProjectMetaData metaData = null;
		com.wizzer.mle.studio.project.metadata.jaxb.Project project = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(CONTEXT_PATH, getClass().getClassLoader());
			Unmarshaller um = jc.createUnmarshaller();
			project = (com.wizzer.mle.studio.project.metadata.jaxb.Project) um.unmarshal(is);
			
			metaData = convertMetaData(project);
		} catch (JAXBException je) {
			throw new IOException(je.getMessage());
		}

		return metaData;
	}

	/**
     * Converts a domain <code>ProjectMetaData</code> into a JAXB <code>Project</code>.
     * 
     * @param data A domain <code>ProjectMetaData</code>.
     * 
     * @return A JAXB <code>Project</code> is returned.
     */
	private com.wizzer.mle.studio.project.metadata.jaxb.Project convertMetaData(
		    com.wizzer.mle.studio.project.metadata.ProjectMetaData data)
	    throws JAXBException
	{
		ObjectFactory factory = new ObjectFactory();
		com.wizzer.mle.studio.project.metadata.jaxb.Project project = factory.createProject();

        // Set the attributes on the project.
        project.setMajorVersion(data.getMajorVersion());
        project.setMinorVersion(data.getMinorVersion());
		try {
			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar();
	        Calendar creationDate = data.getCreationDate();
	        if (creationDate != null)
	        {
	        	date.setYear(creationDate.get(Calendar.YEAR));
	        	date.setDay(creationDate.get(Calendar.DAY_OF_MONTH));
	        	date.setMonth(creationDate.get(Calendar.MONTH) + 1);
	        	date.setHour(creationDate.get(Calendar.HOUR_OF_DAY));
	        	date.setMinute(creationDate.get(Calendar.MINUTE));
	        	date.setSecond(creationDate.get(Calendar.SECOND));
	        	date.setMillisecond(creationDate.get(Calendar.MILLISECOND));
	        	// Calendar ZONE_OFFSET and DST_OFFSET fields are in milliseconds.
	        	int offsetInMinutes = (creationDate.get(Calendar.ZONE_OFFSET) + creationDate.get(Calendar.DST_OFFSET)) / (60 * 1000);
	        	date.setTimezone(offsetInMinutes); 

	        	project.setDate(date);
	        }
		} catch (DatatypeConfigurationException e) {
			throw new JAXBException(e.getCause());
		}
		
        // Set the MLE project type and project type version.
        Project.ProjectType projectType = factory.createProjectProjectType();
        projectType.setValue(data.getProjectType());
        projectType.setVersion(data.getProjectTypeVersion());
        project.setProjectType(projectType);

        // Set the meta-data.
        List<Title> titleData = project.getTitleData();
        Object[] metaData = data.getProjectData();
        for (int i = 0; i < metaData.length; i++)
        {
        	// Process global project data.
            com.wizzer.mle.studio.project.metadata.jaxb.Title title = factory.createTitle();
            MleTitleMetaData.ProjectDataElement projectElement = (MleTitleMetaData.ProjectDataElement) metaData[i];
            title.setId(projectElement.getId());
            title.setDigitalWorkprint(projectElement.getDwpFile());
            title.setVersion(projectElement.getVersion());
            
            // Process the mastering targets.
            Object[] targets = projectElement.getMasterTargets();
            for (int j = 0; j < targets.length; j++)
            {
            	if (targets[j] instanceof MleTitleMetaData.MasterTargetElement)
            	{
            		MleTitleMetaData.MasterTargetElement targetElement = (MleTitleMetaData.MasterTargetElement) targets[j];
	            	
            		Target target = factory.createTarget();
	            	target.setType(targetElement.getType());
	            	target.setId(targetElement.getId());
	            	target.setDigitalPlayprint(targetElement.getDigitalPlayprint());	            	
	            	target.setVerbose(targetElement.isVerbose());

	            	TargetTools tools = factory.createTargetTools();
	            	tools.setBigEndian(targetElement.isBigEndian());
	            	tools.setFixedPoint(targetElement.isFixedPoint());
	            	tools.setDestinationDirectory(targetElement.getDestinationDirectroy());
	            	tools.setCodeGeneration(targetElement.getCodeGeneration());
	            	tools.setHeaderPackage(targetElement.getHeaderPackage());
	            	
	            	if (targetElement.hasGengroup())
	            	{
		            	TargetTools.Gengroup gengroup = factory.createTargetToolsGengroup();
		            	gengroup.setActorIdFile(targetElement.getActorIdFile());
		            	gengroup.setGroupIdFile(targetElement.getGroupIdFile());
		            	tools.setGengroup(gengroup);
	            	}
	            	
	            	if (targetElement.hasGenmedia())
	            	{
		            	TargetTools.Genmedia genmedia = factory.createTargetToolsGenmedia();
		            	genmedia.setBomFile(targetElement.getBomFile());
		            	tools.setGenmedia(genmedia);
	            	}
	            	
	            	if (targetElement.hasGenscene())
	            	{
		            	TargetTools.Genscene genscene = factory.createTargetToolsGenscene();
		            	genscene.setSceneIdFile(targetElement.getSceneIdFile());
		            	tools.setGenscene(genscene);
	            	}
	            	
	            	if (targetElement.hasGenppscript())
	            	{
		            	TargetTools.Genppscript genppscript = factory.createTargetToolsGenppscript();
		            	genppscript.setScriptFile(targetElement.getScriptFile());
		            	genppscript.setTocName(targetElement.getTocName());
		            	tools.setGenppscript(genppscript);
	            	}
	            	
	            	if (targetElement.hasGendpp())
	            	{
		            	TargetTools.Gendpp gendpp = factory.createTargetToolsGendpp();
		            	gendpp.setSourceDirectory(targetElement.getSourceDirectory());
		            	gendpp.setScriptPath(targetElement.getScriptPath());
		            	tools.setGendpp(gendpp);
	            	}
	            	
	            	ArrayList<String> tags = targetElement.getTags();
	            	if ((tags != null) && (! tags.isEmpty()))
	            	{
	            		List<String> toolTags = tools.getTags();
	            		for (int k = 0; k < tags.size(); k++)
	            			toolTags.add(new String(tags.get(k)));
	            	}
	            	
	            	target.setTools(tools);
	            	
	            	title.getMasterTarget().add(target);
            	}
            }
            
            // XXX - Process user data here.
            
            titleData.add(title);
        }


        // XXX - Process the project user data.

		return project;
	}
	
	/**
     * Converts a JAXB <code>Project</code> into a domain <code>ProjectMetaData</code>.
     * 
     * @param data A JAXB <code>Project</code>.
     * 
     * @return A domain <code>ProjectMetaData</code> is returned.
     * 
     * @throws MetaDataException This exception is thrown if data in the JAXB
     * <code>Project</code> is invalid.
     */
    private com.wizzer.mle.studio.project.metadata.ProjectMetaData convertMetaData(
            com.wizzer.mle.studio.project.metadata.jaxb.Project project)
        throws MetaDataException
    {
        com.wizzer.mle.studio.project.metadata.MleTitleMetaData metaData = new MleTitleMetaData();

        // Set the attributes on the project.
        int majorVersion = project.getMajorVersion();
        if (majorVersion != metaData.getMajorVersion())
            throw new MetaDataException("Invalid project meta-data version: major value = " + majorVersion + ".");
        int minorVersion = project.getMinorVersion();
        if (minorVersion != metaData.getMinorVersion())
            throw new MetaDataException("Invalid project meta-data version: minor value = " + minorVersion + ".");
        XMLGregorianCalendar creationDate = project.getDate();
        if (creationDate != null)
        {
	        GregorianCalendar date = creationDate.toGregorianCalendar();
	        metaData.setCreationDate(date);
        }

        // Set the MLE project type and project type version.
        String projectType = project.getProjectType().getValue();
        if (! projectType.equals(metaData.getProjectType()))
            throw new MetaDataException("Invalid project type: " + projectType + ".");
        String projectTypeVersion = project.getProjectType().getVersion();
        if (! projectTypeVersion.equals(metaData.getProjectTypeVersion()))
            throw new MetaDataException("Invalid project type version: " + projectTypeVersion + ".");

        // Set the meta-data.
        List<Title> titleData = project.getTitleData();
        Vector<MleTitleMetaData.ProjectDataElement> elements = new Vector<ProjectDataElement>();
        for (int i = 0; i < titleData.size(); i++)
        {
        	// Process global project data.
            com.wizzer.mle.studio.project.metadata.jaxb.Title title = (com.wizzer.mle.studio.project.metadata.jaxb.Title) titleData.get(i);
            MleTitleMetaData.ProjectDataElement element = metaData.new ProjectDataElement();
            element.setId(title.getId());
            element.setDwpFile(title.getDigitalWorkprint());
            element.setVersion(title.getVersion());
            
            // Process the mastering targets.
            List<Target> targets = title.getMasterTarget();
            if (! targets.isEmpty())
            {
            	MleTitleMetaData.MasterTargetElement[] targetElements = new MleTitleMetaData.MasterTargetElement[targets.size()];
            	for (int j = 0; j < targets.size(); j++)
            	{
            		targetElements[j] = metaData.new MasterTargetElement();
            		targetElements[j].setType(targets.get(j).getType());
            		targetElements[j].setId(targets.get(j).getId());
            		targetElements[j].setDigitalPlayprint(targets.get(j).getDigitalPlayprint());
            		targetElements[j].setVerbose(targets.get(j).isVerbose());
            		
            		TargetTools tools = targets.get(j).getTools();
            		targetElements[j].setBigEndian(tools.isBigEndian());
            		targetElements[j].setFixedPoint(tools.isFixedPoint());
            		targetElements[j].setDestinationDirectory(tools.getDestinationDirectory());
            		targetElements[j].setCodeGeneration(tools.getCodeGeneration());
            		targetElements[j].setHeaderPackage(tools.getHeaderPackage());
            		
            		TargetTools.Gengroup gengroup = tools.getGengroup();
            		if (gengroup != null)
            		{
            			targetElements[j].setGengroup(true);
            			targetElements[j].setActorIdFile(gengroup.getActorIdFile());
            			targetElements[j].setGroupIdFile(gengroup.getGroupIdFile());
            		} else
            			targetElements[j].setGengroup(false);
            		
            		TargetTools.Genscene genscene = tools.getGenscene();
            		if (genscene != null)
            		{
            			targetElements[j].setGenscene(true);
            			targetElements[j].setSceneIdFile(genscene.getSceneIdFile());
            		} else
            			targetElements[j].setGenscene(false);
            		
            		TargetTools.Genmedia genmedia = tools.getGenmedia();
            		if (genmedia != null)
            		{
            			targetElements[j].setGenmedia(true);
            			targetElements[j].setBomFile(genmedia.getBomFile());
            		} else
            			targetElements[j].setGenmedia(false);
            		
            		TargetTools.Genppscript genppscript = tools.getGenppscript();
            		if (genppscript != null)
            		{
            			targetElements[j].setGenppscript(true);
            			targetElements[j].setScriptFile(genppscript.getScriptFile());
            			targetElements[j].setTocName(genppscript.getTocName());
            		} else
            			targetElements[j].setGenppscript(false);
            		
            		TargetTools.Gendpp gendpp = tools.getGendpp();
            		if (gendpp != null)
            		{
            			targetElements[j].setGendpp(true);
            			targetElements[j].setSourceDirectory(gendpp.getSourceDirectory());
            			targetElements[j].setScriptPath(gendpp.getScriptPath());
            		} else
            			targetElements[j].setGendpp(false);
            		
            		List<String> tags = tools.getTags();
            		if ((tags !=  null) && (! tags.isEmpty()))
            		   targetElements[j].setTags(tags);
            	}
            	element.setMasterTargets(targetElements);
            }

            // XXX - Process user data here.

            elements.add(element);
        }
        metaData.setProjectData(elements.toArray());

    	// XXX - Process the project user data.

        return metaData;
    }
}
