/*
 * Copyright 2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.git.model.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.kuali.student.git.model.GitTreeData;
import org.kuali.student.git.model.GitTreeData.GitTreeDataVisitor;

/**
 * @author Kuali Student Team
 *
 */
public final class GitTreeDataUtils {

	/**
	 * 
	 */
	private GitTreeDataUtils() {
	}

	private static class BlobCountingVisitor implements GitTreeDataVisitor {

		private AtomicInteger counter = new AtomicInteger(0);
		
		/* (non-Javadoc)
		 * @see org.kuali.student.git.model.GitTreeData.GitTreeDataVisitor#visitBlob(java.lang.String, java.lang.String)
		 */
		@Override
		public void visitBlob(String name, String objectId) {
			counter.addAndGet(1);
		}

		/**
		 * @return the counter
		 */
		public AtomicInteger getCounter() {
			return counter;
		}
		
		
		
	}
	
	public static int countBlobs(GitTreeData existingTreeData) {
		
		BlobCountingVisitor visitor;
		
		existingTreeData.visit(visitor = new BlobCountingVisitor());
		
		return visitor.getCounter().intValue();
	}

}
