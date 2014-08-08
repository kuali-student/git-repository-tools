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

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

/**
 * @author ocleirig
 *
 */
public class ViewerMain {

	private static final Logger log = LoggerFactory.getLogger(ViewerMain.class);
	
	/**
	 * 
	 */
	public ViewerMain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1 && args.length != 2 && args.length != 3) {
			System.err.println("USAGE: <git meta directory> [--simplify] [--report]");
			System.exit(-1);
		}
		
		String gitDirectory = args[0];
		
		File gitDir = new File (gitDirectory);
		
		if (!gitDir.exists()) {
			System.err.println(gitDirectory + " does not exist");
			System.exit(-1);
		}
		
		boolean simplify = false;
		boolean report = false;
		
		if (args.length >= 2) {
			if (args[1].toLowerCase().equals("--simplify"))
				simplify = true;
			else if (args[1].toLowerCase().equals("--report"))
				report = true;
		}
		
		if (args.length == 3) {
			if (args[2].toLowerCase().equals("--simplify"))
				simplify = true;
			else if (args[2].toLowerCase().equals("--report"))
				report = true;
		}
			
		
		try {
			
			Repository repo = GitRepositoryUtils.buildFileRepository(gitDir, false, true);
			
			Graph<RevCommit, String> graph = new DirectedSparseMultigraph<RevCommit, String>();
		
			RevWalk rw = new RevWalk (repo);
		
			
			Map<String, Ref> branchHeads = repo.getRefDatabase().getRefs(Constants.R_HEADS);
			
			Map<RevCommit, String>branchHeadCommitToBranchNameMap = new HashMap<RevCommit, String>();
			
			for (Map.Entry<String, Ref>entry : branchHeads.entrySet()) {
				
				RevCommit branchHeadCommit = rw.parseCommit(entry.getValue().getObjectId());
				
				rw.markStart(branchHeadCommit);
				
				branchHeadCommitToBranchNameMap.put(branchHeadCommit, entry.getValue().getName());
				
				
			}
			
			// tags
			Map<String, Ref> tags = repo.getRefDatabase().getRefs(Constants.R_TAGS);
			
			
			for (Map.Entry<String, Ref>entry : tags.entrySet()) {
				
				RevTag tag = rw.parseTag(entry.getValue().getObjectId());
				
				RevCommit branchHeadCommit = rw.parseCommit(tag.getObject().getId());
				
				rw.markStart(branchHeadCommit);
				
				branchHeadCommitToBranchNameMap.put(branchHeadCommit, entry.getValue().getName());
				
				
			}

			rw.sort(RevSort.TOPO);
			rw.sort(RevSort.REVERSE, true);
			
			RevCommit currentCommit = null;
			
			Set<RevCommit>commits = new HashSet<RevCommit>();
			
			while ((currentCommit = rw.next()) != null) {
			
				commits.add(currentCommit);
				
			}
			
			for(RevCommit commit :  commits) {
				
				if (simplify) {
					/* 
					 * The only vertexes will be the parentless commits,  multi-parent commits and branch head commits.
					 * 
					 * For each commit we need to walk the available parents backwards so that we find the candidate vertex to associate with.
					 * 
					 */
					
					if (RevCommitVertexUtils.isSimplifiedVertex (branchHeadCommitToBranchNameMap, commit)) {
						
						for (RevCommit parentCommit : commit.getParents()) {
							
							// find the parent commit that is a simplified vertex
							
							RevCommit currentVertex = RevCommitVertexUtils.findSimplifiedVertex(branchHeadCommitToBranchNameMap, parentCommit);
							
							graph.addEdge(commit.getId().name() + " to " + currentVertex.getId().name(), commit, currentVertex);
							
						}
						
					}
					
					
				}
				else {
					/*
					 * For non-simplified mode we an edge between each commit and its parent commit.
					 */
					for (RevCommit parentCommit : commit.getParents()) {
						graph.addEdge(commit.getId().name() + " to " + parentCommit.getId().name(), commit, parentCommit);
					}
				}
				
			}
			
			if (report) {
				
				PrintWriter reporter = new PrintWriter (new File("vertex-report.txt"));
				
				List<RevCommitVertexCount>vertexes = new ArrayList<RevCommitVertexCount>();
				
				for (RevCommit vertex : graph.getVertices()) {
				
					Collection<String> inEdges = graph.getInEdges(vertex);
					
					int size = inEdges.size();
					
					if (size > 1)
						vertexes.add(new RevCommitVertexCount(vertex, size));
					
				}
				
				Collections.sort(vertexes, new Comparator<RevCommitVertexCount>() {

					/* (non-Javadoc)
					 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
					 */
					@Override
					public int compare(RevCommitVertexCount o1,
							RevCommitVertexCount o2) {
						// descending
						return new Integer (o2.getCount()).compareTo(new Integer (o1.getCount()));
					}
					
				});
				
				reporter.println("Object Id:child count");
				
				for (RevCommitVertexCount vertexCount : vertexes) {
					reporter.println(vertexCount.getVertex().getId().name() + ":" + vertexCount.getCount());
				}
				
				
				reporter.close();
				
			}
			new GitGraphFrame(gitDir, graph, branchHeadCommitToBranchNameMap, simplify);
			
		} catch (Exception e) {
			log.error("viewing failed", e);
		}
		
	}

	

}
