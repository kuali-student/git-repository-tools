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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import modifier.PathRevisionAndMD5AndSHA1;

import org.apache.commons.io.IOUtils;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


	/**
	 * 
	 */
	public SvnDumpFilter() {

	}

	/**
	 * Parse the dumpfile given.
	 * 
	 * Calls the options interface when
	 * 
	 * @param dumpFile
	 * @param options
	 * @param nodeFilter 
	 * @throws FileNotFoundException
	 */
	public void parseDumpFile(String dumpFile,
			AbstractParseOptions options) throws FileNotFoundException {
		this.parseDumpFile(dumpFile, options, null);
	}
	
	public void parseDumpFile(String dumpFile, IParseOptions options, INodeFilter nodeFilter)
			throws FileNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(dumpFile);

		options.setFileInputStream(fileInputStream);

		try {

			while (true) {

				ReadLineData lineData = org.kuali.student.common.io.IOUtils.readTilNonEmptyLine(fileInputStream, UTF_8);

				if (lineData == null) {
					break;
				} else if (lineData.getLine() == null) {

					options.onStreamEnd(lineData);

					break;
				}

				if (lineData.startsWith("SVN-fs-dump-format-version")) {

					long dumpFormatVersion = extractLongValue(lineData);

					if (dumpFormatVersion > 3) {
						exitOnError("Filter only works on version 3 and below dump streams");
					}

					options.onDumpFormatVersion(lineData);

				} else if (lineData.startsWith("UUID")) {
					options.onUUID(lineData);
				} else if (isRevisionStart(lineData)) {

					processRevision(lineData, options, fileInputStream);

				} else if (isNodeStart(lineData)) {

					processNode(lineData, options, nodeFilter, fileInputStream);

				}

			}

			try {
				fileInputStream.close();
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

	public void applyFilter(String sourceDumpFile, String targetDumpFile, INodeFilter nodeFilter)
			throws FileNotFoundException, UnsupportedEncodingException {

		final FileOutputStream fileOutputStream = new FileOutputStream(
				targetDumpFile);

		parseDumpFile(sourceDumpFile, new AbstractParseOptions() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kuali.student.svn.tools.IParseOptions#onStreamEnd(org.kuali
			 * .student.svn.tools.model.ReadLineData)
			 */
			@Override
			public void onStreamEnd(ReadLineData lineData) {

				lineData.println(fileOutputStream);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kuali.student.svn.tools.IParseOptions#onDumpFormatVersion
			 * (org.kuali.student.svn.tools.model.ReadLineData)
			 */
			@Override
			public void onDumpFormatVersion(ReadLineData lineData) {
				lineData.println(fileOutputStream);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kuali.student.svn.tools.IParseOptions#onUUID(org.kuali.student
			 * .svn.tools.model.ReadLineData)
			 */
			@Override
			public void onUUID(ReadLineData lineData) {
				lineData.println(fileOutputStream);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.kuali.student.svn.tools.IParseOptions#onRevision(long,
			 * org.kuali.student.svn.tools.model.ReadLineData)
			 */
			@Override
			public void onRevision(long currentRevision, ReadLineData lineData) {

				lineData.println(fileOutputStream);

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kuali.student.svn.tools.IParseOptions#onRevisionPropContentLength
			 * (long, org.kuali.student.svn.tools.model.ReadLineData)
			 */
			@Override
			public void onRevisionPropContentLength(long currentRevision,
					long propContentLength, ReadLineData lineData) {

				lineData.println(fileOutputStream);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kuali.student.svn.tools.IParseOptions#onRevisionContentLength
			 * (long, org.kuali.student.svn.tools.model.ReadLineData)
			 */
			@Override
			public void onRevisionContentLength(long currentRevision,
					long contentLength, long propContentLength, ReadLineData lineData) {

				// TODO write out the propcontent length element.
				lineData.println(fileOutputStream);

				try {
					transferStreamContent(inputStream, fileOutputStream,
							contentLength);
				} catch (IOException e) {
					throw new RuntimeException(
							String.format(
									"stream transfer failed for revision(%d) properties.",
									currentRevision), e);
				}

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kuali.student.svn.tools.IParseOptions#onNode(org.kuali.student
			 * .svn.tools.model.ReadLineData, java.lang.String)
			 */
			@Override
			public void onNode(ReadLineData lineData, String path) {
				lineData.println(fileOutputStream);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.kuali.student.svn.tools.IParseOptions#onAfterNode(long,
			 * java.lang.String, java.util.Map,
			 * org.kuali.student.svn.tools.model.INodeFilter)
			 */
			@Override
			public void onAfterNode(long currentRevision, String path,
					Map<String, String> nodeProperties, INodeFilter nodeFilter) {

				writeNode(fileOutputStream, currentRevision, path,
						nodeProperties, nodeFilter);

				// seems to be allowed to have files without content
//				String kind = nodeProperties.get("Node-kind");
//				
//				if (kind.equals("file") && !nodeProperties.containsKey("Content-length")) {
//					throw new RuntimeException(String.format(
//							"Node(%d:%s) missing content-length property",
//							currentRevision, path));
//				}

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kuali.student.svn.tools.IParseOptions#onNodeContentLength
			 * (long, long, java.util.Map,
			 * org.kuali.student.svn.tools.model.INodeFilter)
			 */
			@Override
			public void onNodeContentLength(long currentRevision, String path,
					long contentLength, long propContentLength, Map<String, String> nodeProperties,
					INodeFilter nodeFilter) {

				writeNode(fileOutputStream, currentRevision, path,
						nodeProperties, nodeFilter);

				try {
					transferStreamContent(inputStream, fileOutputStream,
							contentLength);
				} catch (IOException e) {
					throw new RuntimeException(
							String.format(
									"stream transfer failed for node(%d:%s) properties.",
									currentRevision, path), e);
				}
			}

		}, nodeFilter);

		try {
			fileOutputStream.close();
		} catch (IOException e) {
			log.warn("Exception closing stream", e);
		}

	}

	private void exitOnError(String message) {
		log.error(message);
		System.exit(-1);
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

	private void processNode(ReadLineData lineData, IParseOptions options,
			INodeFilter nodeFilter, FileInputStream fileInputStream)
			throws IOException {

		String path = extractStringValue(lineData);

		options.onNode(lineData, path);

		// this map will store the properties in the same order as we found
		// them.
		Map<String, String> nodeProperties = new LinkedHashMap<String, String>();

		while (true) {
			// consume properties until wee see the content or someother
			// node/revision

			lineData =  org.kuali.student.common.io.IOUtils.readTilNonEmptyLine(fileInputStream, UTF_8);
			
			if (lineData == null) {
				log.warn("end of input reached while processing path: " + path);
				return;
			} else if (lineData.getLine() == null) {

				options.onAfterNode(currentRevision, path, nodeProperties, nodeFilter);
				options.onStreamEnd(lineData);

				return;
			}

			if (isRevisionStart(lineData)) {
				// mark the end of the node
				options.onAfterNode(currentRevision, path, nodeProperties,
						nodeFilter);
				processRevision(lineData, options, fileInputStream);
				return;
			} else if (isNodeStart(lineData)) {
				// mark the end of the node
				options.onAfterNode(currentRevision, path, nodeProperties,
						nodeFilter);
				processNode(lineData, options, nodeFilter, fileInputStream);
				return;
			} else {
				String parts[] = splitLine(lineData.getLine());

				nodeProperties.put(parts[0], parts[1]);
				
				if (parts[0].equals("Node-action") && parts[1].equals("delete")) {
					/*
					 * Handle where the last path in the dump file is a deleted path
					 * we need to trigger it to be persisted.
					 */
					options.onAfterNode(currentRevision, path, nodeProperties, nodeFilter);
					return;
				}
				else if (nodeProperties.containsKey("Content-length")) {

					long contentLength = extractLongValue(lineData);
					
					long propContentLength;
					
					try {
						propContentLength = Long.valueOf (nodeProperties.get("Prop-content-length"));
					} catch (NumberFormatException e) {
						propContentLength = -1L;
						// fall through
					}
					
					// read in any of the path properties into the nodeProperties map
					
					
					
					// write the properties and copy the content (this includes
					// both the svn properties and text content)
					options.onNodeContentLength(currentRevision, path,
							contentLength, propContentLength, nodeProperties, nodeFilter);
					return;

				}

			}

		}

	}

	private void writeNode(FileOutputStream fileOutputStream,
			long currentRevision, String path,
			Map<String, String> nodeProperties, INodeFilter nodeFilter) {

		String action = nodeProperties.get("Node-action");

		if (nodeFilter != null && action != null && action.equals("add")) {

			String nodeType = nodeProperties.get("Node-kind");

			PathRevisionAndMD5AndSHA1 joinHistoryData = nodeFilter
					.getCopyFromData(currentRevision, path);

			/*
			 * For now we will only try to join paths on add that are not already joined.
			 */
			if (nodeProperties.get("Node-copyfrom-rev") == null && joinHistoryData != null) {

				String original = nodeProperties.put("Node-copyfrom-rev",
						String.valueOf(joinHistoryData.getRevision()));

				if (original != null)
					log.warn("Overriting existing Node-copyfrom-rev: "
							+ original);

				original = nodeProperties.put("Node-copyfrom-path",
						joinHistoryData.getPath());

				if (original != null)
					log.warn("Overriting existing Node-copyfrom-path: "
							+ original);

				// only write the hashes if the type is file
				if (nodeType.equals("file")) {
					
					original = nodeProperties.put("Text-copy-source-md5",
							joinHistoryData.getMd5());

					if (original != null)
						log.warn("Overriting existing Text-copy-source-md5: "
								+ original);

					String sha1 = joinHistoryData.getSha1();

					if (sha1 != null) {

						original = nodeProperties.put("Text-copy-source-sha1",
								joinHistoryData.getSha1());

						if (original != null)
							log.warn("Overriting existing Text-copy-source-sha1: "
									+ original);

					}

				}

			}
		}

		String contentLengthValue = nodeProperties.remove("Content-length");

		PrintWriter pw = new PrintWriter(fileOutputStream);

		for (Map.Entry<String, String> entry : nodeProperties.entrySet()) {

			pw.print(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
		}

		// ensure that we print out the content-length property last
		if (contentLengthValue != null)
			pw.print(String.format("Content-length: %s\n", contentLengthValue));

		pw.flush();

	}

	private void processRevision(ReadLineData lineData, IParseOptions options,
			FileInputStream fileInputStream) throws IOException {

		currentRevision = extractLongValue(lineData);

		options.onRevision(currentRevision, lineData);

		ReadLineData expectingPropContentLength =  org.kuali.student.common.io.IOUtils.readTilNonEmptyLine(fileInputStream, UTF_8);

		if (!expectingPropContentLength.startsWith("Prop-content-length")) {
			exitOnError("Expected Prop-content-length: but found: "
					+ expectingPropContentLength);
		}

		long propContentLength = extractLongValue(expectingPropContentLength);
		
		ReadLineData expectingContentLength =  org.kuali.student.common.io.IOUtils.readTilNonEmptyLine(fileInputStream, UTF_8);

		if (!expectingContentLength.startsWith("Content-length:")) {
			exitOnError("Expected Content-length: but found: "
					+ expectingPropContentLength);
		}

		
		long contentLength = extractLongValue(expectingContentLength);

		options.onRevisionContentLength(currentRevision, contentLength, propContentLength, expectingContentLength);

	}

	private void transferStreamContent(FileInputStream fileInputStream,
			FileOutputStream fileOutputStream, long contentLength)
			throws IOException {

		contentLength += 1; // I believe this is for the null byte in C. Without
							// this we lose 1 character in the buffer.

		long copied = IOUtils.copyLarge(fileInputStream, fileOutputStream, 0,
				contentLength);

		if (copied != contentLength)
			exitOnError(String.format(
					"Transferred (%s) instead of Expected (%s)",
					String.valueOf(copied), String.valueOf(contentLength)));

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
