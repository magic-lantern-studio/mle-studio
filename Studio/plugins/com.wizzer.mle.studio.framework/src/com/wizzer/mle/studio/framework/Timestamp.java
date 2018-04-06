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
package com.wizzer.mle.studio.framework;

// Import standard Java packages.
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * This class is used for generating time stamps based on Universal Coordinate Time,
 * or UTC.
 * 
 * @author Mark S. Millard
 */
public class Timestamp
{
    private GregorianCalendar m_calendar = null;
    private GregorianCalendar m_refCalendar = new GregorianCalendar(1980,Calendar.APRIL,6);
    
    private short m_GPSOffset = 0;
    
	/**
	 * The default constructor.
	 * <p>
	 * A <code>GregorianCalendar</code> is instantiated using the current time
	 * in the default time zone with the default locale.
	 * </p>
	 */
	public Timestamp()
	{
		super();
		m_calendar = new GregorianCalendar();
	}
		
	/**
	 * Construct a time stamp from another <code>Calendar</code>.
	 * 
	 * @param calendar A reference to another <code>Calendar</code>.
	 */
	public Timestamp(Calendar calendar)
	{
		super();
		m_calendar = newCalendar(calendar);
	}
	
	/**
	 * Construct a time stamp from another time stamp (in seconds).
	 * The new <code>Timestamp</code> will be in UTC seconds since the epoch.
	 * 
	 * @param seconds The new time in UTC seconds.
	 * @param useReferenceCalendar If <b>true</b> then the seconds passed in
	 * are relative to the default reference calendar.
	 */
	public Timestamp(long seconds,boolean useReferenceCalendar)
	{
		super();
		m_calendar = new GregorianCalendar();
		
		if (useReferenceCalendar)
		{
			long referenceTimestamp = calculateSeconds(m_refCalendar);
			seconds = seconds + referenceTimestamp;
		}
		
		// Set the calendar.
        m_calendar.setTimeInMillis(seconds * 1000);
	}
	
	// Get a new Gregorian Calendar based on the specifed argument.
	private GregorianCalendar newCalendar(Calendar calendar)
	{
		GregorianCalendar newCalendar = new GregorianCalendar(
			calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH),
			calendar.get(Calendar.DATE),
			calendar.get(Calendar.HOUR),
			calendar.get(Calendar.MINUTE),
			calendar.get(Calendar.SECOND));
        return newCalendar;
	}
	
	/**
	 * Get the <code>GregorianCalendar</code> that was instantiated at the time of
	 * construction.
	 * 
	 * @return A reference to the <code>GregorianCalendar</code is returned.
	 */
	public GregorianCalendar getCalendar()
	{
		return m_calendar;
	}
	
	/**
	 * Set the reference Calendar.
	 * <p>
	 * By default, the reference calendar is set to 0000 Hours UTC, January 6th,
	 * 1980.
	 * </p>
	 * 
	 * @param calendar The reference <code>Calendar</code> to use when calculating
	 * system time.
	 * 
	 */
	public void setReferenceCalendar(Calendar calendar)
	{
		m_refCalendar = newCalendar(calendar);
	}
	
	/**
	 * Get the system time.
	 * <p>
	 * This is a 32-bit unsigned integer quantity representing the system time,
	 * as the number of GPS seconds since the time represented by the reference
	 * calendar. By default, this is the number of seconds since
	 * 0000 Hours UTC, January 6th, 1980.
	 * The return value may or may not include the correction factor for leap
	 * seconds, depending upon the specified argument.
	 * </p>
	 * 
	 * @param leap If <b>true</b> then the system time will include the correction
	 * factor for leap seconds. If <b>false</b>, then the system time does contain
	 * this correction.
	 * 
	 * @return The system time represented as a 32-bit unsigned integer is
	 * returned.
	 */
	public long getSystemTime(boolean leap)
	{
		int systemTime = 0;
		int currentTimestamp = (int)calculateSeconds(m_calendar);
		int referenceTimestamp = (int)calculateSeconds(m_refCalendar);
		
		systemTime = currentTimestamp - referenceTimestamp;
		
		return systemTime;
	}
	
	/**
	 * The offset is an 8-bit value that serves dual roles. When the return value is zero,
	 * the offset indicates that the system time carries UTC time directly.
	 * When the offset is not equal to zero, it should be interpreted as an 8-bit unsigned
	 * vinteger that defins the current offset in whold seconds between GPS and UTC time
	 * standards.
	 * <p>
	 * To conver GPS time to UTC, the offset is subtracted from GPS time. Whenever the
	 * International Bureau of Weights and Measuers decides that the current offset is
	 * too far in error, an additional leap second may be added (or subtracted), and
	 * the GPS offset will reflect the change.
	 * </p>
	 * 
	 * @return Either zero, or an offset between GPS and UTC time standards.
	 * 
	 * @see systemTime()
	 */
	public short getGPSOffset()
	{
		return m_GPSOffset;
	}

    // Calculate the number of seconds since the epoch.
    private long calculateSeconds(Calendar calendar)
    {
    	long value = calendar.getTimeInMillis();
    	value = (int)(value / 1000);
    	
    	return value;
    }
    
	/**
	 * Convert time in ms into user-friendly string.
	 * 
	 * @param ms The timestamp to convert to the <b>String</b> format.
	 * 
	 * @return A <code>String</code> is returned formatted in a human readable
	 * way.
	 */
	public static String getTimeString(long ms)
	{
	   String str = new String();

	   int days = 0;
	   int hours = 0;
	   int minutes = 0;
	   int seconds = 0;

	   long s = (int)(ms / 1000);

	   if ( s == 0 )
	   {
		  str = "0 seconds";
		  return str;
	   }

	   if ( s >= 60*60*24 )
	   {
		  days = (int)(s / (60*60*24));
		  s -= days*60*60*24;
	   }

	   if ( s >= 60*60 )
	   {
		  hours = (int)(s / (60*60));
		  s -= hours*60*60;
	   }

	   if ( s >= 60 )
	   {
		  minutes = (int)(s / 60);
		  s -= minutes*60;
	   }

	   seconds = (int)s;

	   // Now build the string.

	   if ( days > 1 )
	   {
		  str += days + " days ";
	   }
	   else if ( days > 0 )
	   {
		  str += days + " day ";
	   }

	   if ( hours > 1 )
	   {
		  str += hours + " hours ";
	   }
	   else if ( hours > 0 )
	   {
		  str += hours + " hour ";
	   }

	   if ( minutes > 1 )
	   {
		  str += minutes + " minutes ";
	   }
	   else if ( minutes > 0 )
	   {
		  str += minutes + " minute ";
	   }

	   if ( seconds > 1 )
	   {
		  str += seconds + " seconds ";
	   }
	   else if ( seconds > 0 )
	   {
		  str += seconds + " second ";
	   }

	   // Remove the trailing space.
	   if ( str.length() > 0 )
		  str = str.substring(0,str.length()-1);

	   return str;
	}

	/**
	 * Convert time in ms into user-friendly string.
	 * 
	 * @param ms The timestamp to convert to the <b>String</b> format.
	 * 
	 * @return A <code>String</code> is returned formatted in a human readable
	 * way. This format includes millisecond output.
	 */
	public static String getTimeWithMsString(long ms)
	{
	   String str = getTimeString(ms);
	   int milliseconds = (int)(((float)ms) % 1000);

	   if ( milliseconds == 1 )
	   {
		  str += " " + milliseconds + " millisecond";
	   }
	   else
	   {
		  str += " " + milliseconds + " milliseconds";
	   }

	   return str;
	}

}
