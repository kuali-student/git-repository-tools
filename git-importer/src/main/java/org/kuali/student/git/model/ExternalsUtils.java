/**
 * 
 */
package org.kuali.student.git.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kuali.student.svn.model.ExternalModuleInfo;

/**
 * @author Kuali Student Team
 *
 */
public class ExternalsUtils {

	/**
	 * 
	 */
	public ExternalsUtils() {
		// TODO Auto-generated constructor stub
	}

	private static int indexOf (List<GitBranchData>list, String targetBranchPath) {
		for (int i = 0; i < list.size(); i++) {

			GitBranchData data = list.get(i);
			
			if (data.getBranchPath().equals(targetBranchPath))
				return i;
		}
		
		return -1;
	}
	
	
	/**
	 * Reorder the unordered list so that branches that have externals dependencies will be placed after them in the list.
	 * 
	 * This is needed so that when we write the externals data they refer to the latest commit id's in the external branches.
	 * 
	 * @param unorderedList
	 * @return
	 */
	public static List<GitBranchData> computeExternalsAwareOrdering(Collection<GitBranchData>unorderedList) {
		
		List<GitBranchData> results = new ArrayList<> (unorderedList.size());
		
		results.addAll(unorderedList);
		
		for (GitBranchData data : unorderedList) {
			
			List<ExternalModuleInfo> externals = data.getExternals();
			
			if (externals.size() > 0) {
				
				for (ExternalModuleInfo external : externals) {
					
					int indexOfDependentBranch = indexOf(results, data.getBranchPath());						
					
					int indexOfCurrentExternal = indexOf (results, external.getBranchPath());
					
					if (indexOfDependentBranch == -1 || indexOfCurrentExternal == -1)
						continue; // skip over any case where the external isn't represented in the current branches to be committed.
					
					if (indexOfDependentBranch < indexOfCurrentExternal) {
						// remove the dependent branch and insert it after the current external.
						
						if ((indexOfCurrentExternal + 1) == results.size()) {
							// adding at the end of the list
							GitBranchData obj = results.remove(indexOfDependentBranch);
							
							results.add(obj);
						}
						else {
							
							/*
							 * Add after the external.
							 */
							GitBranchData obj = results.get(indexOfDependentBranch);
							
							results.add(indexOfCurrentExternal+1, obj);
							
							// remove after the add so as to not mess up the indexing.
							results.remove(indexOfDependentBranch);
						}
					}
				}
			}
			
		}
		
		
		return results;
	}
}
