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
package org.kuali.student.git.cleaner.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.lib.ObjectId;

/**
 * @author ocleirig
 *
 */
public class ObjectTranslationDataSource implements ObjectIdTranslation {

	private String dataSource;
	
	private Map<ObjectId, ObjectId>objectTranslationMap = new HashMap<ObjectId, ObjectId>();
	
	/**
	 * 
	 */
	public ObjectTranslationDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void storeObjectTranslation(String originalObjectId, String newObjectId) {
		
		ObjectId original = ObjectId.fromString(originalObjectId);
		
		ObjectId translated = ObjectId.fromString(newObjectId);
		
		this.objectTranslationMap.put(original, translated);
	}

	@Override
	public ObjectId translateObjectId(ObjectId originalId) {
		
		ObjectId translation = this.objectTranslationMap.get(originalId);
		
		if (translation == null)
			return originalId;
		else
			return translation;
	}
}
