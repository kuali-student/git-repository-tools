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
package org.kuali.student.svn.tools.dump;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.iq80.snappy.SnappyInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 *
 */
public class ReadSnappyDumpMain {

	private static final Logger log = LoggerFactory.getLogger(ReadSnappyDumpMain.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("USAGE: <snappy archive>");
			System.exit(-1);
		}
		
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new SnappyInputStream(new FileInputStream(args[0]))));
			
			while (true) {
				
				String line = reader.readLine();
				
				if (line == null)
					break;
				
				System.out.println(line);
			}
			
			reader.close();
			
		} catch (Exception e) {
			log.error("failed reading snappy archive : " + args[0], e);
		} 
	}

}
