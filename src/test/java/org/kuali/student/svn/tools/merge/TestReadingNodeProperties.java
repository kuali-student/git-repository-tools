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
package org.kuali.student.svn.tools.merge;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kuali.student.common.io.exceptions.InvalidKeyLineException;

/**
 * @author Kuali Student Team
 * 
 * Test how to read in the revision properties from the svn dump stream.
 *
 */
public class TestReadingNodeProperties {

	/**
	 * 
	 */
	public TestReadingNodeProperties() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testReadingRevisionProperties() throws InvalidKeyLineException, IOException {
		

		Map<String, String> nodeProperties = new HashMap<>();
		
		FileInputStream inputStream = new FileInputStream("src/test/resources/revision-properties.dat");
		
			
		while (org.kuali.student.common.io.IOUtils.readKeyAndValuePair (inputStream, nodeProperties));
		
		Assert.assertTrue(nodeProperties.containsKey("svn:author"));
		Assert.assertTrue(nodeProperties.containsKey("svn:date"));
		Assert.assertTrue(nodeProperties.containsKey("svn:log"));
		
		
		Assert.assertEquals("hjohnson", nodeProperties.get("svn:author"));
		Assert.assertEquals("2009-02-26T22:30:46.373926Z", nodeProperties.get("svn:date"));
		Assert.assertEquals("Fix firefox popup frame size issue", nodeProperties.get("svn:log"));
		
	}
	
	@Test
	public void testReadingComplexRevisionProperties() throws IOException, InvalidKeyLineException {
		

		Map<String, String> nodeProperties = new HashMap<>();
		
		FileInputStream inputStream = new FileInputStream("src/test/resources/revision-properties-no-log.dat");
		
		String contentLengthLine = org.kuali.student.common.io.IOUtils.readLine(inputStream, "UTF8");
		
		Assert.assertEquals("Content-length: 102", contentLengthLine);
		
		String skippedLine = org.kuali.student.common.io.IOUtils.readLine(inputStream, "UTF8");
			
		while (org.kuali.student.common.io.IOUtils.readKeyAndValuePair (inputStream, nodeProperties));
		
		Assert.assertTrue(nodeProperties.containsKey("svn:author"));
		Assert.assertTrue(nodeProperties.containsKey("svn:date"));
		Assert.assertTrue(nodeProperties.containsKey("svn:log"));
		
		
		Assert.assertEquals("hjohnson", nodeProperties.get("svn:author"));
		Assert.assertEquals("2009-02-26T22:30:46.373926Z", nodeProperties.get("svn:date"));
		Assert.assertEquals("", nodeProperties.get("svn:log"));
		
	}
}

	
