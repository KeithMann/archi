/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import com.archimatetool.editor.diagram.figures.AbstractTextFlowFigure;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Business Function Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessFunctionFigure
extends AbstractTextFlowFigure {

    public BusinessFunctionFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rounded Rectangle Figure Delegate to Draw
        RoundedRectangleFigureDelegate figureDelegate = new RoundedRectangleFigureDelegate(this);
        figureDelegate.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_FUNCTION_16));
        setFigureDelegate(figureDelegate);
    }
}
