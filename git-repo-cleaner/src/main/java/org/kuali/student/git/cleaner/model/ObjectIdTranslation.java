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

import org.eclipse.jgit.lib.ObjectId;

/**
 * @author ocleirig
 *
 */
public interface ObjectIdTranslation {

	/**
	 * 
	 * @param originalId
	 * @return the translated id, originalid if there is no translation.
	 */
	public ObjectId translateObjectId (ObjectId originalId);
}
