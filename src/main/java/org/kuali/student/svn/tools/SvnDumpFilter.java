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
package org.kuali.student.svn.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import modifier.PathRevisionAndMD5AndSHA1;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Svn Dump Filter is a way to change the contents of a revision as it is
 * loaded.
 * 
 * @author Kuali Student Team
 * 
 */
public class SvnDumpFilter {

	private static final String UTF_8 = "UTF-8";

	private static final Logger log = LoggerFactory
			.getLogger(SvnDumpFilter.class);

	private long currentRevision = -1;

	@Autowired
	private INodeFilter nodeFilter;

	private FileInputStream fileInputStream;

	private FileOutputStream fileOutputStream;

	/**
	 * 
	 */
	public SvnDumpFilter() {

	}

	public void applyFilter(String sourceDumpFile, String targetDumpFile) throws FileNotFoundException,
			UnsupportedEncodingException {


		fileInputStream = new FileInputStream(sourceDumpFile);

		fileOutputStream = new FileOutputStream(targetDumpFile);
		

		try {

			boolean foundFormat = false;

			while (true) {

				ReadLineData lineData = readTilNonEmptyLine();

				if (lineData == null) {
					break;
				}
				else if (lineData.getLine() == null) {

					lineData.println(fileOutputStream);

					break;
				}

				if (lineData.startsWith("SVN-fs-dump-format-version")) {

					long dumpFormatVersion = extractLongValue(lineData);

					if (dumpFormatVersion > 3) {
						exitOnError("Filter only works on version 3 and below dump streams");
					}

					foundFormat = true;

					lineData.println(fileOutputStream);

				} else if (lineData.startsWith("UUID")) {
					lineData.println(fileOutputStream);
				} else if (isRevisionStart(lineData)) {

					processRevision(lineData);

				} else if (isNodeStart(lineData)) {

					processNode(lineData, nodeFilter);

				}

			}

			try {
				fileInputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				log.warn("Problem closing streams");
			}

		} catch (FileNotFoundException e) {
			log.error("file not found", e);
		} catch (UnsupportedEncodingException e) {
			log.error("unsupported encoding exception", e);
		} catch (IOException e) {
			log.error("io exception", e);
		}
	}

	private void exitOnError(String message) {
		log.error(message);
		System.exit(-1);
	}

	private String extractStringKey(ReadLineData lineData) {
		String[] parts = splitLine(lineData.getLine());

		return parts[0];
	}

	private String extractStringValue(ReadLineData lineData) {
		String[] parts = splitLine(lineData.getLine());

		return parts[1];
	}

	private long extractLongValue(ReadLineData lineData) {

		return Long.valueOf(extractStringValue(lineData));
	}

	private boolean isNodeStart(ReadLineData lineData) {
		if (lineData.startsWith("Node-path"))
			return true;
		else
			return false;
	}

	private boolean isRevisionStart(ReadLineData lineData) {
		if (lineData.startsWith("Revision-number"))
			return true;
		else
			return false;
	}

	private void processNode(ReadLineData lineData, INodeFilter nodeFilter) throws IOException {

		String path = extractStringValue(lineData);

		lineData.println(fileOutputStream);

		// this map will store the properties in the same order as we found
		// them.
		Map<String, String> nodeProperties = new LinkedHashMap<String, String>();

		while (true) {
			// consume properties until wee see the content or someother
			// node/revision

			lineData = readTilNonEmptyLine();

			if (isRevisionStart(lineData)) {
				// write the current node
				writeNode(currentRevision, path, nodeProperties, nodeFilter);
				if (nodeProperties.containsKey("Content-length")) {
					log.info("");
				}
				processRevision(lineData);
				return;
			} else if (isNodeStart(lineData)) {
				// write the current node
				writeNode(currentRevision, path, nodeProperties, nodeFilter);
				if (nodeProperties.containsKey("Content-length")) {
					log.info("");
				}
				processNode(lineData, nodeFilter);
				return;
			} else {
				String parts[] = splitLine(lineData.getLine());

				nodeProperties.put(parts[0], parts[1]);

				if (nodeProperties.containsKey("Content-length")) {

					long contentLength = extractLongValue(lineData);

					// write the properties and copy the content (this includes
					// both the svn properties and text content)
					writeNode(currentRevision, path, nodeProperties,
							nodeFilter);
					transferStreamContent(contentLength);
					return;

				}

			}

		}

	}

	private void writeNode(long currentRevision, String path,
			Map<String, String> nodeProperties, INodeFilter nodeFilter) {

		String action = nodeProperties.get("Node-action");

		if (action != null && action.equals("add")) {

			PathRevisionAndMD5AndSHA1 joinHistoryData = nodeFilter.getCopyFromData(
					currentRevision, path);

			/*
			 * For now we will only try to join paths on add.
			 */
			if (joinHistoryData != null) {

				String original = nodeProperties.put("Node-copyfrom-rev",
						String.valueOf(joinHistoryData.getRevision()));
				
				if (original != null)
					log.warn("Overriting existing Node-copyfrom-rev: " + original);
				
				original = nodeProperties.put("Node-copyfrom-path",
						joinHistoryData.getPath());
				
				if (original != null)
					log.warn("Overriting existing Node-copyfrom-path: " + original);
				
				original = nodeProperties.put("Text-copy-source-md5",
						joinHistoryData.getMd5());
				
				if (original != null)
					log.warn("Overriting existing Text-copy-source-md5: " + original);
				
				original = nodeProperties.put("Text-copy-source-sha1",
						joinHistoryData.getSha1());
				
				if (original != null)
					log.warn("Overriting existing Text-copy-source-sha1: " + original);


			}
		}

		String contentLengthValue = nodeProperties.remove("Content-length");

		PrintWriter pw = new PrintWriter(fileOutputStream);
		
		for (Map.Entry<String, String> entry : nodeProperties.entrySet()) {

			pw.print(String.format("%s: %s\n", entry.getKey(),
					entry.getValue()));
		}

		// ensure that we print out the content-length property last
		if (contentLengthValue != null)
			pw.print(String.format("Content-length: %s\n",
					contentLengthValue));
		
		pw.flush();

	}

	private void processRevision(ReadLineData lineData) throws IOException {

		currentRevision = extractLongValue(lineData);

		lineData.println(fileOutputStream);

		ReadLineData expectingPropContentLength = readTilNonEmptyLine();

		if (!expectingPropContentLength.startsWith("Prop-content-length")) {
			exitOnError("Expected Prop-content-length: but found: "
					+ expectingPropContentLength);
		}

		long propContentLength = extractLongValue(expectingPropContentLength);

		expectingPropContentLength.println(fileOutputStream);

		ReadLineData expectingContentLength = readTilNonEmptyLine();

		if (!expectingContentLength.startsWith("Content-length:")) {
			exitOnError("Expected Content-length: but found: "
					+ expectingPropContentLength);
		}

		long contentLength = extractLongValue(expectingContentLength);

		expectingContentLength.println(fileOutputStream);

		// we need to stream this
		// divide total bytes by buffer size and then repeat until
		// all is copied

		transferStreamContent(contentLength);

	}

	private void transferStreamContent(long contentLength) throws IOException {

		contentLength+= 1;
		
		long copied = IOUtils.copyLarge(fileInputStream, fileOutputStream, 0, contentLength);
		
		if (copied != contentLength)
			exitOnError(String.format(
					"Transferred (%s) instead of Expected (%s)",
					String.valueOf(copied), String.valueOf(contentLength)));
		

	}

	private ReadLineData readTilNonEmptyLine()
			throws IOException {
		String line = org.kuali.student.common.io.IOUtils.readLine(fileInputStream, UTF_8);

		int skippedLines = 0;
		while (line != null) {

			if (line.trim().length() == 0) {
				skippedLines++;
				line = org.kuali.student.common.io.IOUtils.readLine(fileInputStream, UTF_8);
				continue; // skip over emtpy lines
			} else
				return new ReadLineData(line, skippedLines);
		}

		if (skippedLines == 0)
			return null;
		else
			return new ReadLineData(null, skippedLines);
	}

	private String[] splitLine(String line) {

		String parts[] = line.split(":");

		if (parts.length != 2) {
			log.error("Format Error: key and value are required: " + line);
			System.exit(-1);
		}

		return new String[] { parts[0], parts[1].trim() };

	}

}
