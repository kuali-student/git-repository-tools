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
package org.kuali.student.git.model.tree;


import java.util.Comparator;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;

/*
 * Git Tree's require that the tree's and blobs are sorted alphabetically.
 * 
 * http://git.661346.n2.nabble.com/In-tree-object-Must-the-td7446900.html
 */
public class JGitTreeData {
	
	public static Comparator<JGitTreeData> GIT_SORT_ORDERING = new Comparator<JGitTreeData>() {

		// copied from JGit version 3 DirCache
		int cmp(final byte[] aPath, final int aLen, final byte[] bPath,
				final int bLen) {
			for (int cPos = 0; cPos < aLen && cPos < bLen; cPos++) {
				final int cmp = (aPath[cPos] & 0xff) - (bPath[cPos] & 0xff);
				if (cmp != 0)
					return cmp;
			}
			return aLen - bLen;
		}
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(JGitTreeData o1, JGitTreeData o2) {
			
			byte[] byteName1 = o1.getByteName();
			byte[] byteName2 = o2.getByteName();
			
			return cmp(byteName1, byteName1.length, byteName2, byteName2.length);
			
		}

	};
	
	private byte[] byteName;
	
	private String name;
	private FileMode fileMode;
	private ObjectId objectId;
	/**
	 * @param name
	 * @param fileMode
	 * @param objectId
	 */
	public JGitTreeData(String name, FileMode fileMode, ObjectId objectId) {
		super();
		this.name = name;
		this.byteName = getByteName(name, fileMode);
		this.fileMode = fileMode;
		this.objectId = objectId;
	}
	private byte[] getByteName(String name, FileMode fileMode) {
		
		StringBuilder nameBuilder = new StringBuilder(name);
		
		if (fileMode == FileMode.TREE)
			nameBuilder.append("/");
		
		return Constants.encode(nameBuilder.toString());
		
		
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the fileMode
	 */
	public FileMode getFileMode() {
		return fileMode;
	}
	/**
	 * @return the objectId
	 */
	public ObjectId getObjectId() {
		return objectId;
	}
	
	
	/**
	 * @return the byteName
	 */
	public byte[] getByteName() {
		return byteName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JGitTreeData [name=");
		builder.append(name);
		builder.append(", fileMode=");
		builder.append(fileMode);
		builder.append(", objectId=");
		builder.append(objectId);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}