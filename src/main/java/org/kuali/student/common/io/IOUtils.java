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
 * The code in this class was copied from org.tmatesoft.svn.core.internal.wc.SVNUtil version 1.7.8 and was originally written by: TMate Software Ltd., Peter Skoog
 * 
 * it is subject to the <a href="http://svnkit.com/license.html">svnkit.com license</a>.
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
