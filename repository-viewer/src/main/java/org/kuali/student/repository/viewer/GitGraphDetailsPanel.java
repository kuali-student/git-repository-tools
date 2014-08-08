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
package org.kuali.student.repository.viewer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Holds the details of the selected node
 * 
 * @author ocleirig
 *
 */
public class GitGraphDetailsPanel extends JPanel {

	private JLabel objectIdLabel;
	private JPanel branchNamePanel;
	private JLabel branchNameLabel;
	private Map<RevCommit, String> branchHeadCommitToBranchNameMap;
	private JLabel parentIdsLabel;
	private JTextArea commitMessage;
	private JLabel committer;
	private JLabel commitDate;

	/**
	 * @param branchHeadCommitToBranchNameMap 
	 * @param jComboBox 
	 * 
	 */
	public GitGraphDetailsPanel(JComboBox modeComboBox, Map<RevCommit, String> branchHeadCommitToBranchNameMap) {
		super();

		this.branchHeadCommitToBranchNameMap = branchHeadCommitToBranchNameMap;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		addSubPanel(new JLabel("Mouse Click Mode: "), modeComboBox);
		
		addSubPanel(new JLabel("Object id: "), objectIdLabel = new JLabel (""));
		
		addSubPanel(new JLabel("Parent ids: "), parentIdsLabel = new JLabel (""));
		
		addSubPanel(new JLabel("Message: "), commitMessage = new JTextArea (""));
		
		commitMessage.setEditable(false);
		
		addSubPanel(new JLabel("Committer: "), committer = new JLabel (""));
		
		addSubPanel(new JLabel("Commit Date: "), commitDate = new JLabel (""));
		
		branchNamePanel = addSubPanel(new JLabel("Branch Name: "), branchNameLabel = new JLabel (""));
		
//		modeComboBox.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//			}
//		});
		
		
	}
	
	private JPanel addSubPanel (JLabel label, JComponent component) {
		
		JPanel subPanel = new JPanel();	
		
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
		
		subPanel.add(label);
		
		subPanel.add (component);
		
		add(subPanel);
		
		return subPanel;
	}

	public void setSelectedCommit(RevCommit commit) {
		
		objectIdLabel.setText(commit.getId().name().substring(0, 8));
		
		this.commitMessage.setText(commit.getFullMessage());
		this.committer.setText(commit.getCommitterIdent().getName());
		
		this.commitDate.setText(DateFormatUtils.ISO_DATE_FORMAT.format(commit.getCommitterIdent().getWhen()));
		
		List<RevCommit>parentCommitIds = Arrays.asList(commit.getParents());
		Collection<String>parentIds = CollectionUtils.transformedCollection(parentCommitIds, new Transformer<RevCommit, String>() {

			/* (non-Javadoc)
			 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
			 */
			@Override
			public String transform(RevCommit input) {
				return input.getId().name().substring(0, 8);
			}
			
		});
		
		this.parentIdsLabel.setText(StringUtils.join(parentIds, ", "));
		
		String branchName = branchHeadCommitToBranchNameMap.get(commit);
		
		if (branchName == null)
			branchNamePanel.setVisible(false);
		else {
			branchNamePanel.setVisible(true);
			branchNameLabel.setText(branchName);
		}
		
		
	}

	

}
