/*
 *  SWTCalendar.java  - A calendar component for SWT
 *  Mark Bryan Yu
 *  swtcalendar.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.wizzer.mle.studio.framework.ui.calendar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import java.util.Calendar;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class SWTCalendar extends Composite {
    private List listeners;

    private SWTYearChooser yearChooser;
    private SWTMonthChooser monthChooser;
    private SWTDayChooser dayChooser;

    public SWTCalendar(Composite parent) {
        super(parent, SWT.NONE);

        listeners = new ArrayList();

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginHeight = 2;
        gridLayout.marginWidth = 2;
        gridLayout.horizontalSpacing = 2;
        gridLayout.verticalSpacing = 2;
        setLayout(gridLayout);

        monthChooser = new SWTMonthChooser(this);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.BEGINNING;
        monthChooser.setLayoutData(data);

        yearChooser = new SWTYearChooser(this);
        data = new GridData();
        data.horizontalAlignment = GridData.END;
        yearChooser.setLayoutData(data);

        dayChooser = new SWTDayChooser(this, this);
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = 2;
        data.grabExcessHorizontalSpace = true;
        dayChooser.setLayoutData(data);
        monthChooser.setDayChooser(dayChooser);
        yearChooser.setDayChooser(dayChooser);
    }

    private void setCalendar(Calendar c, boolean update) {
        if (update) {
            yearChooser.setYear(c.get(Calendar.YEAR));
            monthChooser.setMonth(c.get(Calendar.MONTH));
            dayChooser.setDay(c.get(Calendar.DATE));
        }
    }

    public void setCalendar(Calendar c) {
        setCalendar(c, true);
    }

    public void notifyListeners(Calendar calendar) {
        Iterator i = listeners.iterator();
        while(i.hasNext()) {
            ISWTCalendarListener l = (ISWTCalendarListener)i.next();
            l.dateChanged(dayChooser.getCalendar());
        }
    }

    public Calendar getCalendar() {
        return dayChooser.getCalendar();
    }

    public void addCalendarListeners(ISWTCalendarListener listeners) {
        this.listeners.add(listeners);
    }

    public void removeCalendarListener(ISWTCalendarListener listeners) {
        this.listeners.remove(listeners);
    }
}
