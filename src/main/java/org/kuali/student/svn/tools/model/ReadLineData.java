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
package org.kuali.student.svn.tools.model;

import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 
 * @author Kuali Student Team
 *
 */
public class ReadLineData {
	
	private int skippedLines = 0;
	
	private String line;

	public ReadLineData(String line, int skippedLines) {
		super();
		this.line = line;
		this.skippedLines = skippedLines;
	}

	/**
	 * @return the skippedLines
	 */
	public int getSkippedLines() {
		return skippedLines;
	}

	/**
	 * @param skippedLines the skippedLines to set
	 */
	public void setSkippedLines(int skippedLines) {
		this.skippedLines = skippedLines;
	}

	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	public boolean startsWith(String prefix) {
		return this.line.startsWith(prefix);
	}

	public void println(FileOutputStream fileOutputStream) {
		
		PrintWriter pw = new PrintWriter(fileOutputStream);
		
		for (int i = 0; i < skippedLines; i++) {
			pw.print("\n"); // unix style end of lines
		}
		
		if (line != null) {
			pw.print(line);
			pw.print("\n"); // unix style end of lines
		}
		
		pw.flush();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReadLineData [skippedLines=");
		builder.append(skippedLines);
		builder.append(", line=");
		builder.append(line);
		builder.append("]");
		return builder.toString();
	}

	
	
	

}
