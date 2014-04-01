/*
 * Copyright 2013 The Kuali Foundation
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
package org.kuali.student.git.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Kuali Student Team
 *
 */
public class GitCopyFileParser {

	/**
	 * 
	 */
	public GitCopyFileParser() {
		
	}
	
	public void parse (String file) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		
		while (true) {
			
			String line = reader.readLine();
			
			if (line == null)
				break;
			
			if (line.trim().length() == 0)
				continue; // skip empty lines
			
			String[] parts = line.split(" ");
			
			
		}
		reader.close();
	}

}
