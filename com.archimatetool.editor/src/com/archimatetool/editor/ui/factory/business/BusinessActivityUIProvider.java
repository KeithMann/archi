/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.business;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.business.BusinessActivityEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Business Activity UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class BusinessActivityUIProvider extends AbstractBusinessUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getBusinessActivity();
    }
    
    @Override
    public EditPart createEditPart() {
        return new BusinessActivityEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.BusinessActivityUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.BusinessActivityUIProvider_1;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchimateImages.ICON_BUSINESS_ACTIVITY_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchimateImages.ICON_BUSINESS_ACTIVITY_16);
    }
}
