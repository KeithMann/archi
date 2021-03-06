package com.archimatetool.editor.diagram.policies.snaptogrid;

/**
 * snap-to-grid patch  by Jean-Baptiste Sarrodie (aka Jaiguru)
 * 
 * This Class has been extended to allow change on
 * protected method updateSourceRequest
 */

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.gef.tools;

import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.tools.ConnectionBendpointTracker;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
// snap-to-grid patch

/**
 * A tracker for creating new bendpoints or dragging existing ones. The
 * Connection bendpoint tracker is returned by connection bendpoint handles.
 * This tracker will send a {@link BendpointRequest} to the connection editpart
 * which originated the tracker. The bendpoint request may be either a request
 * to move an existing bendpoint, or a request to create a new bendpoint.
 * <P>
 * A ConnectionBendpointTracker operates on a single connection editpart.
 * 
 * @author hudsonr
 */
public class ExtendedConnectionBendpointTracker extends ConnectionBendpointTracker {

    /**
     * Key modifier for ignoring snap while dragging. It's CTRL on Mac, and ALT
     * on all other platforms.
     */
    static final int MODIFIER_NO_SNAPPING;

    static {
        if (Platform.OS_MACOSX.equals(Platform.getOS())) {
            MODIFIER_NO_SNAPPING = SWT.CTRL;
        } else {
            MODIFIER_NO_SNAPPING = SWT.ALT;
        }
    }

	/**
	 * Null constructor.
	 */
	protected ExtendedConnectionBendpointTracker() {
	}

	/**
	 * Constructs a tracker for the given connection and index.
	 * 
	 * @param editpart
	 *            the connection
	 * @param i
	 *            the index of the bendpoint
	 */
	public ExtendedConnectionBendpointTracker(ConnectionEditPart editpart, int i) {
	    super(editpart, i);
	}


	/**
	 * @see org.eclipse.gef.tools.SimpleDragTracker#updateSourceRequest()
	 */
	@Override
    protected void updateSourceRequest() {
		BendpointRequest request = (BendpointRequest) getSourceRequest();
		
        // snap-to-grid patch
		Point p = getLocation();
		
		// Check prefs and whether user is holding down modifier key
        boolean s2g = Preferences.STORE.getBoolean(IPreferenceConstants.GRID_SNAP);
        if(s2g && !getCurrentInput().isModKeyDown(MODIFIER_NO_SNAPPING)) {
            int gs = Preferences.STORE.getInt(IPreferenceConstants.GRID_SIZE);
        	p.setX((p.x/gs) * gs);
            p.setY((p.y/gs) * gs);
        }
        
        request.setLocation(p);
	}

}
