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
package com.wizzer.mle.studio.framework.command;

/**
 * This class describes the Command pattern for execute, undo and redo
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
