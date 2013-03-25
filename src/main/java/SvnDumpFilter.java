import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import model.ReadLineData;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */

/**
 * Svn Dump Filter is a way to change the contents of a revision as it is
 * loaded.
 * 
 * @author ocleirig
 * 
 */
public class SvnDumpFilter {

	private static final Logger log = LoggerFactory
			.getLogger(SvnDumpFilter.class);

	private static long currentRevision = -1;


	/**
	 * 
	 */
	public SvnDumpFilter() {

		

	}

	private static void exitOnError(String message) {
		log.error(message);
		System.exit(-1);
	}

	private static String extractStringKey(ReadLineData lineData) {
		String[] parts = splitLine(lineData.getLine());

		return parts[0];
	}

	private static String extractStringValue(ReadLineData lineData) {
		String[] parts = splitLine(lineData.getLine());

		return parts[1];
	}

	private static long extractLongValue(ReadLineData lineData) {

		return Long.valueOf(extractStringValue(lineData));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			log.error("USAGE: <source dump file> <target dump file>");
			System.exit(-1);
		}

		String sourceDumpFile = args[0];
		String targetDumpFile = args[1];

		SvnDumpFilter sdf = new SvnDumpFilter();

		boolean exclude = true;
		boolean renumberRevisions = false;
		boolean dropEmptyRevisions = false;
		boolean preserveRevisionProperties = false;
		Collection prefixes = new HashSet<Object>();
		boolean skipMissingMergeSources = false;

		try {

			FileInputStream fileInputStream = new FileInputStream(
					sourceDumpFile);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fileInputStream, "UTF-8"));

			PrintWriter writer = new PrintWriter(new File(targetDumpFile),
					"UTF-8");

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
					
					 
				}
				else if (isNodeStart(lineData)) {
					
					processNode(lineData, reader, writer);
					
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

	private static boolean isNodeStart(ReadLineData lineData) {
		if (lineData.startsWith("Node-path"))
			return true;
		else
			return false;
	}

	private static boolean isRevisionStart(ReadLineData lineData) {
		if (lineData.startsWith("Revision-number"))
			return true;
		else
			return false;
	}

	private static void processNode(ReadLineData lineData, BufferedReader reader,
			PrintWriter writer) throws IOException {
		
		String path = extractStringValue(lineData);
		
		lineData.println(writer);
		
		// this map will store the properties in the same order as we found them.
		Map<String, String>nodeProperties = new LinkedHashMap<String, String>();
		
		while (true) {
			// consume properties until wee see the content or someother node/revision
			
			lineData = readTilNonEmptyLine(reader);
			
			if (isRevisionStart(lineData)) {
				// write the current node
				writeNode(currentRevision, path, nodeProperties, writer);
				processRevision(lineData, reader, writer);
				return;
			}
			else if (isNodeStart(lineData)) {
				// write the current node
				writeNode(currentRevision, path, nodeProperties, writer);
				processNode(lineData, reader, writer);
				return;
			}
			else {
				String parts[] = splitLine(lineData.getLine());
				
				nodeProperties.put(parts[0], parts[1]);
				
				if (nodeProperties.containsKey("Content-length")) {
					
					long contentLength = extractLongValue(lineData);
					
					// write the properties and copy the content (this includes both the svn properties and text content)
					writeNode(currentRevision, path, nodeProperties, writer);
					transferStreamContent(reader, writer, contentLength);
					return;
					
				}
				
				
			}
			
		}
		
	}

	private static void writeNode(long currentRevision, String path, Map<String, String> nodeProperties,
			PrintWriter writer) {
		
		/*
		 * Externalize this as part of the refactor.
		 */
		if (SvnDumpFilter.currentRevision == 4) {
			if (path.equals("external-change-of-copy.c")) {
				
				nodeProperties.put("Node-copyfrom-rev", "1");
				nodeProperties.put("Node-copyfrom-path", "test.c");
				nodeProperties.put("Text-copy-source-md5", "bfce707a5dde4c3e27f511bdbd8d6503");

			}
		}
		
		
		
		String contentLengthValue = nodeProperties.remove("Content-length");
		
		for (Map.Entry<String, String> entry : nodeProperties.entrySet()) {
			
			writer.print(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
		}
		
		// ensure that we print out the content-length property last
		if (contentLengthValue != null)
			writer.print(String.format("Content-length: %s\n", contentLengthValue));
		
		
		
		
	}

	private static void processRevision(ReadLineData lineData, BufferedReader reader,
			PrintWriter writer) throws IOException {
		
		currentRevision = extractLongValue(lineData);
		
		lineData.println(writer);

		ReadLineData expectingPropContentLength = readTilNonEmptyLine(reader);

		if (!expectingPropContentLength
				.startsWith("Prop-content-length")) {
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

		transferStreamContent(reader, writer, contentLength);
		
	}

	private static void transferStreamContent(BufferedReader reader,
			PrintWriter writer, long contentLength) throws IOException {
		
		long copied = IOUtils.copyLarge(reader, writer, 0, contentLength);
//		
		if (copied != contentLength)
			exitOnError(String.format("Transferred (%s) instead of Expected (%s)", String.valueOf (copied), String.valueOf (contentLength)));
		
	}

	private static ReadLineData readTilNonEmptyLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();

		int skippedLines = 0;
		while (line != null) {

		if (line.trim().length() == 0) {
			skippedLines++;
			line = reader.readLine();
			continue; // skip over emtpy lines
		}
		else
			return new ReadLineData(line, skippedLines);
		}
		
		if (skippedLines == 0)
			return null;
		else
			return new ReadLineData(null, skippedLines);
	}

	private static String[] splitLine(String line) {

		String parts[] = line.split(":");

		if (parts.length != 2) {
			log.error("Format Error: key and value are required: " + line);
			System.exit(-1);
		}

		return new String[] { parts[0], parts[1].trim() };

	}

	

}
