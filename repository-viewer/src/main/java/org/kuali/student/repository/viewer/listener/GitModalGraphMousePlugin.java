/*
 *  Copyright 2014 The Kuali Foundation Licensed under the
 *	Educational Community License, Version 2.0 (the "License"); you may
 *	not use this file except in compliance with the License. You may
 *	obtain a copy of the License at
 *
 *	http://www.osedu.org/licenses/ECL-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the License for the specific language governing
 *	permissions and limitations under the License.
 */
package org.kuali.student.repository.viewer.listener;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;

/**
 * @author ocleirig
 *
 */
public class GitModalGraphMousePlugin extends DefaultModalGraphMouse<RevCommit, String> {

	/**
	 * 
	 */
	public GitModalGraphMousePlugin() {
		super();
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse#loadPlugins()
	 */
	@Override
	protected void loadPlugins() {
		super.loadPlugins();
		
		pickingPlugin = new PickingGraphMousePlugin<RevCommit, String>() {

			/* (non-Javadoc)
			 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseDragged(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseDragged(MouseEvent e) {
				/*
				 * Intentionally override this method so that there is no dragging effect when picking.
				 */
			}

			/* (non-Javadoc)
			 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#pickContainedVertices(edu.uci.ics.jung.visualization.VisualizationViewer, java.awt.geom.Point2D, java.awt.geom.Point2D, boolean)
			 */
			@Override
			protected void pickContainedVertices(
					VisualizationViewer<RevCommit, String> visualizer, Point2D down,
					Point2D out, boolean clear) {
				
				/*
				 * Override to only select on point down which is where the initial click is from.
				 * 
				 * This is to make selection singular only.
				 */
				
				Layout<RevCommit, String> layout = visualizer.getGraphLayout();
				
		        PickedState<RevCommit> pickedVertexState = visualizer.getPickedVertexState();
		        
		         
		        if(pickedVertexState != null) {
		            if(clear) {
		            	pickedVertexState.clear();
		            }
		            
		           GraphElementAccessor<RevCommit, String> pickSupport = visualizer.getPickSupport();

		            RevCommit picked = pickSupport.getVertex(layout, down.getX(), down.getY());
		            
		            if (picked != null)
		            	pickedVertexState.pick(picked, true);
		            }
			}

		};
	}

	
	
	

}
