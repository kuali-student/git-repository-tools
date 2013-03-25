/**
 * 
 */
package org.kuali.student.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author Kuali Student Team
 * 
 * The code in this class was copied from org.tmatesoft.svn.core.internal.wc.SVNUtil
 * 
 */
public final class IOUtils {

	/**
	 * 
	 */
	private IOUtils() {
		// TODO Auto-generated constructor stub
	}

	private static CharsetDecoder decoder = Charset.forName("UTF-8")
			.newDecoder();

	public static String readLine(InputStream in, String charsetName)
			throws IOException {

		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		int r = -1;

		while ((r = in.read()) != '\n') {

			if (r == -1) {
				// String out = decode(decoder, byteBuffer.toByteArray());

				return null;

			}

			byteBuffer.write(r);

		}

		String out = decode(decoder, byteBuffer.toByteArray());

		return out;

	}

	private static String decode(CharsetDecoder decoder, byte[] in) {

		ByteBuffer inBuf = ByteBuffer.wrap(in);

		CharBuffer outBuf = CharBuffer.allocate(inBuf.capacity()
				* Math.round(decoder.maxCharsPerByte() + 0.5f));

		decoder.decode(inBuf, outBuf, true);

		decoder.flush(outBuf);

		decoder.reset();

		return outBuf.flip().toString();

	}

}
