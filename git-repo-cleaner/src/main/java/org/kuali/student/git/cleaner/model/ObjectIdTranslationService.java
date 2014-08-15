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

import java.util.List;

import org.eclipse.jgit.lib.ObjectId;

/**
 * 
 * Delegates to an ordered list of translation data sources.
 * 
 * The idea is that if multiple translations have occurred in some order.
 * 
 * looking up each translation in turn will result in the final or current object id which can then be used.
 * 
 * @author ocleirig
 *
 */
public class ObjectIdTranslationService implements ObjectIdTranslation {

	private final List<ObjectIdTranslation>dataSources;
	
	/**
	 * @param dataSources2 
	 * 
	 */
	public ObjectIdTranslationService(List<ObjectIdTranslation> dataSources) {
		this.dataSources = dataSources;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.git.cleaner.model.ObjectIdTranslation#translateObjectId(org.eclipse.jgit.lib.ObjectId)
	 */
	@Override
	public ObjectId translateObjectId(ObjectId originalId) {
		
		ObjectId currentTranslation = originalId;
		
		for (ObjectIdTranslation delegate : dataSources) {
		
			currentTranslation = delegate.translateObjectId(currentTranslation);
		}
		
		return currentTranslation;
	}

}
