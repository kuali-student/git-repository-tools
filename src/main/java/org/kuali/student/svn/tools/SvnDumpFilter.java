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
import java.io.BufferedWriter;
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

import modifier.PathRevisionAndMD5;

import org.apache.commons.io.IOUtils;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 */

/**
 * Svn Dump Filter is a way to change the contents of a revision as it is
 * loaded.
 * 
 * @author Kuali Student Team
 * 
 */
public class SvnDumpFilter {

	private static final Logger log = LoggerFactory
			.getLogger(SvnDumpFilter.class);

	private long currentRevision = -1;

	private FileInputStream fileInputStream;

	private BufferedReader reader;

	private PrintWriter writer;
	
	@Autowired
	private INodeFilter nodeFilter;

	private FileOutputStream fileOutputStream;

	/**
	 * 
	 */
	public SvnDumpFilter() {

	}

	public void applyFilter(String sourceDumpFile, String targetDumpFile) throws FileNotFoundException,
			UnsupportedEncodingException {

		fileInputStream = new FileInputStream(sourceDumpFile);

		reader = new BufferedReader(new InputStreamReader(fileInputStream,
				"UTF-8"));

		fileOutputStream = new FileOutputStream (targetDumpFile, false);
		
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF-8")));

		try {

			boolean foundFormat = false;

			while (true) {

				ReadLineData lineData = readTilNonEmptyLine(reader);

				if (lineData.getLine() == null) {

					lineData.println(writer);

					break;
				}

				if (lineData.startsWith("SVN-fs-dump-format-version")) {

					long dumpFormatVersion = extractLongValue(lineData);

					if (dumpFormatVersion > 3) {
						exitOnError("Filter only works on version 3 and below dump streams");
					}

					foundFormat = true;

					lineData.println(writer);

				} else if (lineData.startsWith("UUID")) {
					lineData.println(writer);
				} else if (isRevisionStart(lineData)) {

					processRevision(lineData, reader, writer);

				} else if (isNodeStart(lineData)) {

					processNode(lineData, nodeFilter, reader, writer);

				}

			}

			try {
				reader.close();
				writer.close();
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

	private void processNode(ReadLineData lineData, INodeFilter nodeFilter,
			BufferedReader reader, PrintWriter writer) throws IOException {

		String path = extractStringValue(lineData);

		lineData.println(writer);

		// this map will store the properties in the same order as we found
		// them.
		Map<String, String> nodeProperties = new LinkedHashMap<String, String>();

		while (true) {
			// consume properties until wee see the content or someother
			// node/revision

			lineData = readTilNonEmptyLine(reader);

			if (isRevisionStart(lineData)) {
				// write the current node
				writeNode(currentRevision, path, nodeProperties, nodeFilter,
						writer);
				processRevision(lineData, reader, writer);
				return;
			} else if (isNodeStart(lineData)) {
				// write the current node
				writeNode(currentRevision, path, nodeProperties, nodeFilter,
						writer);
				processNode(lineData, nodeFilter, reader, writer);
				return;
			} else {
				String parts[] = splitLine(lineData.getLine());

				nodeProperties.put(parts[0], parts[1]);

				if (nodeProperties.containsKey("Content-length")) {

					long contentLength = extractLongValue(lineData);

					// write the properties and copy the content (this includes
					// both the svn properties and text content)
					writeNode(currentRevision, path, nodeProperties,
							nodeFilter, writer);
					transferStreamContent(contentLength);
					return;

				}

			}

		}

	}

	private void writeNode(long currentRevision, String path,
			Map<String, String> nodeProperties, INodeFilter nodeFilter,
			PrintWriter writer) {

		String md5 = nodeProperties.get("Text-content-md5");

		if (md5 != null)
			nodeFilter.storeMD5(currentRevision, path, md5);

		String action = nodeProperties.get("Node-action");

		if (action != null && action.equals("add")) {

			PathRevisionAndMD5 joinHistoryData = nodeFilter.getCopyFromData(
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

			}
		}

		String contentLengthValue = nodeProperties.remove("Content-length");

		for (Map.Entry<String, String> entry : nodeProperties.entrySet()) {

			writer.print(String.format("%s: %s\n", entry.getKey(),
					entry.getValue()));
		}

		// ensure that we print out the content-length property last
		if (contentLengthValue != null)
			writer.print(String.format("Content-length: %s\n",
					contentLengthValue));

	}

	private void processRevision(ReadLineData lineData, BufferedReader reader,
			PrintWriter writer) throws IOException {

		currentRevision = extractLongValue(lineData);

		lineData.println(writer);

		ReadLineData expectingPropContentLength = readTilNonEmptyLine(reader);

		if (!expectingPropContentLength.startsWith("Prop-content-length")) {
			exitOnError("Expected Prop-content-length: but found: "
					+ expectingPropContentLength);
		}

		long propContentLength = extractLongValue(expectingPropContentLength);

		expectingPropContentLength.println(writer);

		ReadLineData expectingContentLength = readTilNonEmptyLine(reader);

		if (!expectingContentLength.startsWith("Content-length:")) {
			exitOnError("Expected Content-length: but found: "
					+ expectingPropContentLength);
		}

		long contentLength = extractLongValue(expectingContentLength);

		expectingContentLength.println(writer);

		// we need to stream this
		// divide total bytes by buffer size and then repeat until
		// all is copied

		transferStreamContent(contentLength);

	}

	private void transferStreamContent(long contentLength) throws IOException {

		long copied = IOUtils.copyLarge(fileInputStream, fileOutputStream, 0, contentLength);
		//
		if (copied != contentLength)
			exitOnError(String.format(
					"Transferred (%s) instead of Expected (%s)",
					String.valueOf(copied), String.valueOf(contentLength)));

	}

	private ReadLineData readTilNonEmptyLine(BufferedReader reader)
			throws IOException {
		String line = reader.readLine();

		int skippedLines = 0;
		while (line != null) {

			if (line.trim().length() == 0) {
				skippedLines++;
				line = reader.readLine();
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
