package org.overture.ide.ui.editor.core;


import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

public class VdmSourceViewer extends SourceViewer{

	private VdmEditor editor = null;
	
	public VdmSourceViewer(Composite parent, IVerticalRuler ruler,
			IOverviewRuler overviewRuler, boolean overviewRulerVisible,
			int styles, VdmEditor vdmEditor) {
		super(parent,ruler,overviewRuler,overviewRulerVisible,styles);
		this.editor = vdmEditor;
	}

	
}
