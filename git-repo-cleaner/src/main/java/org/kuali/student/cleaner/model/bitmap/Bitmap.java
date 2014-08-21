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
package org.kuali.student.cleaner.model.bitmap;

import org.eclipse.jgit.lib.ObjectId;

import com.googlecode.javaewah.EWAHCompressedBitmap;

/**
 * @author ocleirig
 *
 */
public class Bitmap {

	private EWAHCompressedBitmap bitmap = null;
	
	private RevCommitBitMapIndex indexer = null;

	/**
	 * @param bitmap
	 * @param indexer
	 */
	public Bitmap(RevCommitBitMapIndex indexer) {
		this(indexer, new EWAHCompressedBitmap());
	}

	private Bitmap (RevCommitBitMapIndex indexer, EWAHCompressedBitmap bitmap) {
		super();
		this.indexer = indexer;
		this.bitmap = bitmap;
		
	}
	
	public void set(Integer index) {
		
		EWAHCompressedBitmap currentBitmap = new EWAHCompressedBitmap();
		
		currentBitmap.set(index);
		
		this.bitmap = this.bitmap.or(currentBitmap);
		
	}

	public Bitmap or(Bitmap currentBitmap) {
		
		EWAHCompressedBitmap orBitmap = this.bitmap.or(currentBitmap.bitmap);
		
		return new Bitmap(indexer, orBitmap);
		
	}

	public Bitmap xor(Bitmap targetBitmap) {
		
		EWAHCompressedBitmap xorBitmap = this.bitmap.xor(targetBitmap.bitmap);
		
		return new Bitmap(indexer, xorBitmap);
	}

	public boolean containsObjectId(ObjectId parentCommitId) {
		
		Integer index = indexer.getBitmapElementIndex(parentCommitId);
		
		return this.bitmap.get(index);
		
	}

	public void set(ObjectId currentCommitId) {
		
		Integer index = indexer.getBitmapElementIndex(currentCommitId);
		
		this.set(index);
		
	}
	
	

}
