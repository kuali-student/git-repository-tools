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
package org.kuali.student.cleaner.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.lib.ObjectId;
import org.kuali.student.git.cleaner.model.ObjectIdTranslation;

/**
 * @author ocleirig
 *
 */
public class ObjectIdTranslationMapImpl implements ObjectIdTranslation {

	private Map<ObjectId, ObjectId>translationMap = new HashMap<ObjectId, ObjectId>();
	
	/**
	 * 
	 */
	public ObjectIdTranslationMapImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.model.ObjectIdTranslation#translateObjectId(org.eclipse.jgit.lib.ObjectId)
	 */
	@Override
	public ObjectId translateObjectId(ObjectId originalId) {
		
		ObjectId translatedId = this.translationMap.get(originalId);
		
		if (translatedId == null)
			return originalId;
		else
			return translatedId;
	}
	
	public void addTranslation (ObjectId original, ObjectId translation) {
		this.translationMap.put(original, translation);
	}

}
