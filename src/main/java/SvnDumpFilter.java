import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import model.ReadLineData;
import modifier.INodeModifier;
import modifier.PathRevisionAndMD5;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.ISVNCanceller;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.internal.util.SVNPathUtil;
import org.tmatesoft.svn.core.internal.wc.ISVNLoadHandler;
import org.tmatesoft.svn.core.internal.wc.SVNAdminHelper;
import org.tmatesoft.svn.core.internal.wc.SVNDumpStreamParser;
import org.tmatesoft.svn.core.internal.wc.SVNErrorManager;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.admin.SVNAdminEvent;
import org.tmatesoft.svn.core.wc.admin.SVNAdminEventAction;
import org.tmatesoft.svn.core.wc.admin.SVNAdminEventAdapter;
import org.tmatesoft.svn.util.SVNLogType;

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

	private static final int COPY_BUFFER_SIZE = 2048;

	private static final Logger log = LoggerFactory
			.getLogger(SvnDumpFilter.class);

	// should be 8MB
	private static final int READ_BUF_SIZE = 8192 * 1024;

	private static long currentRevision = -1;

	private SVNDumpStreamParser parser;

	private ISVNCanceller canceller;

	/**
	 * 
	 */
	public SvnDumpFilter() {

		parser = new SVNDumpStreamParser(canceller = new ISVNCanceller() {

			public void checkCancelled() throws SVNCancelException {

			}
		});

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
		
		char[] buffer = new char[COPY_BUFFER_SIZE];

		long totalPages = contentLength / COPY_BUFFER_SIZE;

		if (totalPages > 0 )
			exitOnError("Implement multiple page stream copying");

		int offset = 0;

		while (offset < contentLength) {

			int totalRead = reader.read(buffer, offset,
					(int) contentLength);

			writer.write(buffer, offset, (int) contentLength);

			if (writer.checkError()) {
				exitOnError(String
						.format("copy data issue. read (%d bytes) failed to write",
								totalRead));
			}

			offset += totalRead;
		}
		
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

	/*
	 * Copied from SvnKit SVNAdminClient
	 */
	private void writeDumpData(OutputStream out, String data)
			throws SVNException {
		try {
			out.write(data.getBytes("UTF-8"));
		} catch (IOException ioe) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR,
					ioe.getLocalizedMessage());
			SVNErrorManager.error(err, ioe, SVNLogType.FSFS);
		}
	}

	public void doFilter(InputStream dumpStream, OutputStream resultDumpStream,
			boolean exclude, boolean renumberRevisions,
			boolean dropEmptyRevisions, boolean preserveRevisionProperties,
			Collection prefixes, boolean skipMissingMergeSources)
			throws SVNException {
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

		writeDumpData(resultDumpStream, String.format("%s: %d\n\n",
				SVNAdminHelper.DUMPFILE_MAGIC_HEADER,
				SVNAdminHelper.DUMPFILE_FORMAT_VERSION));

		SVNAdminEventAdapter myEventHandler;
		CustomDumpFilter handler = new CustomDumpFilter(
				resultDumpStream,
				myEventHandler = new SVNAdminEventAdapter(),
				new INodeModifier() {

					public PathRevisionAndMD5 getCopyPathAndRevision(
							long revision, String nodePath) {

						if (revision == 4
								&& nodePath
										.equals("/external-change-of-copy.c")) {

							PathRevisionAndMD5 copyDetails = new PathRevisionAndMD5();

							copyDetails.setRevision(1);
							copyDetails.setPath("test.c");
							copyDetails
									.setMd5("bfce707a5dde4c3e27f511bdbd8d6503");

							return copyDetails;
						} else
							return null;
					}
				}, exclude, renumberRevisions, dropEmptyRevisions,
				preserveRevisionProperties, prefixes, skipMissingMergeSources);

		SVNDumpStreamParser parser = new SVNDumpStreamParser(canceller);

		parser.parseDumpStream(dumpStream, handler, decoder);

		if (myEventHandler != null) {
			if (handler.getDroppedRevisionsCount() > 0) {
				String message = MessageFormat.format(
						"Dropped {0} revision(s).", new Object[] { String
								.valueOf(handler.getDroppedRevisionsCount()) });
				SVNAdminEvent event = new SVNAdminEvent(
						SVNAdminEventAction.DUMP_FILTER_TOTAL_REVISIONS_DROPPED,
						message);
				event.setDroppedRevisionsCount(handler
						.getDroppedRevisionsCount());
				myEventHandler
						.handleAdminEvent(event, ISVNEventHandler.UNKNOWN);
			}

			if (renumberRevisions) {
				Map renumberHistory = handler.getRenumberHistory();
				Long[] reNumberedRevisions = (Long[]) renumberHistory.keySet()
						.toArray(new Long[renumberHistory.size()]);
				Arrays.sort(reNumberedRevisions);
				for (int i = reNumberedRevisions.length; i > 0; i--) {
					Long revision = reNumberedRevisions[i - 1];
					CustomDumpFilter.RevisionItem revItem = (CustomDumpFilter.RevisionItem) renumberHistory
							.get(revision);
					if (revItem.wasDropped()) {
						String message = MessageFormat.format(
								"{0} => (dropped)",
								new Object[] { revision.toString() });
						SVNAdminEvent event = new SVNAdminEvent(
								revision.longValue(),
								SVNAdminEventAction.DUMP_FILTER_DROPPED_RENUMBERED_REVISION,
								message);
						myEventHandler.handleAdminEvent(event,
								ISVNEventHandler.UNKNOWN);
					} else {
						String message = MessageFormat
								.format("{0} => {1}",
										new Object[] {
												revision.toString(),
												String.valueOf(revItem
														.getRevision()) });
						SVNAdminEvent event = new SVNAdminEvent(
								revItem.getRevision(),
								revision.longValue(),
								SVNAdminEventAction.DUMP_FILTER_RENUMBERED_REVISION,
								message);
						myEventHandler.handleAdminEvent(event,
								ISVNEventHandler.UNKNOWN);
					}
				}
			}

			Map droppedNodes = handler.getDroppedNodes();
			if (!droppedNodes.isEmpty()) {
				String message = MessageFormat.format("Dropped {0} node(s)",
						new Object[] { String.valueOf(droppedNodes.size()) });
				SVNAdminEvent event = new SVNAdminEvent(
						SVNAdminEventAction.DUMP_FILTER_TOTAL_NODES_DROPPED,
						message);
				event.setDroppedNodesCount(droppedNodes.size());
				myEventHandler
						.handleAdminEvent(event, ISVNEventHandler.UNKNOWN);
				String[] paths = (String[]) droppedNodes.keySet().toArray(
						new String[droppedNodes.size()]);
				Arrays.sort(paths, SVNPathUtil.PATH_COMPARATOR);
				for (int i = 0; i < paths.length; i++) {
					String path = paths[i];
					message = "'" + path + "'";
					event = new SVNAdminEvent(
							SVNAdminEventAction.DUMP_FILTER_DROPPED_NODE, path,
							message);
					myEventHandler.handleAdminEvent(event,
							ISVNEventHandler.UNKNOWN);
				}
			}
		}
	}

	public void applyDumpFileFilter(String sourceDumpFile, String targetDumpFile)
			throws FileNotFoundException, SVNException {

		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

		parser.parseDumpStream(new BufferedInputStream(new FileInputStream(
				sourceDumpFile), READ_BUF_SIZE), new ISVNLoadHandler() {

			public void setRevisionProperty(String propertyName,
					SVNPropertyValue propertyValue) throws SVNException {
				log.info("debug");
			}

			public void setNodeProperty(String propertyName,
					SVNPropertyValue propertyValue) throws SVNException {
				log.info("debug");

			}

			public void setFullText() throws SVNException {
				log.info("debug");

			}

			public void removeNodeProperties() throws SVNException {
				log.info("debug");

			}

			public void parseUUID(String uuid) throws SVNException {
				log.info("debug");

			}

			public void parseTextBlock(InputStream dumpStream,
					long contentLength, boolean isDelta) throws SVNException {
				log.info("debug");

			}

			public void openRevision(Map headers) throws SVNException {
				log.info("debug");

			}

			public void openNode(Map headers) throws SVNException {
				log.info("debug");

			}

			public void deleteNodeProperty(String propertyName)
					throws SVNException {
				log.info("debug");

			}

			public void closeRevision() throws SVNException {
				log.info("debug");

			}

			public void closeNode() throws SVNException {
				log.info("debug");

			}

			public void applyTextDelta() throws SVNException {
				log.info("debug");

			}
		}, decoder);

	}

}
