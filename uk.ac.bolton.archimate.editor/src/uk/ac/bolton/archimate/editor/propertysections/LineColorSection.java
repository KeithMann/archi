/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.commands.LineColorCommand;
import uk.ac.bolton.archimate.editor.diagram.editparts.ILinedEditPart;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.ILineObject;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for a Line Color
 * 
 * @author Phillip Beauvoir
 */
public class LineColorSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return (object instanceof ILinedEditPart) && ((ILinedEditPart)object).getModel() instanceof ILineObject;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
            if(isAlive()) {
                RGB rgb = fColorSelector.getColorValue();
                String newColor = ColorFactory.convertRGBToString(rgb);
                if(!newColor.equals(fLineObject.getLineColor())) {
                    getCommandStack().execute(new LineColorCommand(fLineObject, newColor));
                }
            }
        }
    };
    
    /**
     * Listen to default element line colour change in Prefs
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty().startsWith(IPreferenceConstants.DEFAULT_ELEMENT_LINE_COLOR)) {
                refreshControls();
            }
        }
    };

    private ILineObject fLineObject;

    private ColorSelector fColorSelector;
    private Button fDefaultColorButton;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createLabel(parent, Messages.LineColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Composite client = createComposite(parent, 2);

        fColorSelector = new ColorSelector(client);
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        gd.widthHint = ITabbedLayoutConstants.BUTTON_WIDTH;
        fColorSelector.getButton().setLayoutData(gd);
        getWidgetFactory().adapt(fColorSelector.getButton(), true, true);
        fColorSelector.addListener(colorListener);

        fDefaultColorButton = new Button(client, SWT.PUSH);
        fDefaultColorButton.setText(Messages.LineColorSection_1);
        gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.BUTTON_WIDTH;
        fDefaultColorButton.setLayoutData(gd);
        getWidgetFactory().adapt(fDefaultColorButton, true, true); // Need to do it this way for Mac
        fDefaultColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    Color color = ColorFactory.getDefaultLineColor(fLineObject);
                    String rgbValue = ColorFactory.convertColorToString(color);
                    getCommandStack().execute(new LineColorCommand(fLineObject, rgbValue));
                }
            }
        });
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof ILinedEditPart && ((ILinedEditPart)element).getModel() instanceof ILineObject) {
            fLineObject = (ILineObject)((EditPart)element).getModel();
            if(fLineObject == null) {
                throw new RuntimeException("Line Object was null"); //$NON-NLS-1$
            }
        }
        else {
            throw new RuntimeException("Should have been an ILineObject"); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        String colorValue = fLineObject.getLineColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb == null) {
            rgb = ColorFactory.getDefaultLineColor(fLineObject).getRGB();
        }
        
        fColorSelector.setColorValue(rgb);

        boolean enabled = fLineObject instanceof ILockable ? !((ILockable)fLineObject).isLocked() : true;
        fColorSelector.setEnabled(enabled);
        
        boolean isDefaultColor = rgb.equals(ColorFactory.getDefaultLineColor(fLineObject).getRGB());
        fDefaultColorButton.setEnabled(!isDefaultColor && enabled);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fColorSelector != null) {
            fColorSelector.removeListener(colorListener);
        }
        
        Preferences.STORE.removePropertyChangeListener(prefsListener);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fLineObject;
    }
}
