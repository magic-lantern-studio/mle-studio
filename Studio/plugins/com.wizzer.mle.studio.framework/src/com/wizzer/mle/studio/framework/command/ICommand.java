// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
//
//  Wizzer Works makes available all content in this file ("Content").
//  Unless otherwise indicated below, the Content is provided to you
//  under the terms and conditions of the Common Public License Version 1.0
//  ("CPL"). A copy of the CPL is available at
//
//      http://opensource.org/licenses/cpl1.0.php
//
//  For purposes of the CPL, "Program" will mean the Content.
//
//  For information concerning this Makefile, contact Mark S. Millard,
//  of Wizzer Works at msm@wizzerworks.com.
//
//  More information concerning Wizzer Works may be found at
//
//      http://www.wizzerworks.com
//
// COPYRIGHT_END
 
// Declare package.
package com.wizzer.mle.studio.framework.command;

/**
 * This class descibes the Command pattern for execute, undo and redo
 * functionality.
 * 
 * @author Mark S. Millard
 */
public interface ICommand
{
	/**
	 * Execute the Command.
	 * 
	 * @param The arguments to pass to the command.
	 * 
	 * @return If the command is successfully executed, then <b>true</b>
	 * will be returned. Otherwise <b>false</b> will be returned.
	 */
	public boolean doIt(Object[] args);
	
	/**
	 * Undo the Command.
	 * 
	 * @return If the command is successfully undone, then <b>true</b>
	 * will be returned. Otherwise <b>false</b> will be returned.
	 */
	public boolean undoIt();
	
	/**
	 * Test whether the Command can be undone.
	 * 
	 * @return If the command can be undone, then <b>true</b>
	 * will be returned. Otherwise <b>false</b> will be returned.
	 */
	public boolean isUndoable();

}
