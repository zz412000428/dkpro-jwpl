/*******************************************************************************
 * Copyright 2017
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.consumer.dump.codec;

import java.io.UnsupportedEncodingException;

import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.ConfigurationException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.DecodingException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.EncodingException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.LoggingException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.SQLConsumerException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.logging.Logger;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.Task;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.Diff;

/**
 * This class encodes the diffs while collecting statistical information.
 *
 *
 *
 */
public class TimedSQLEncoder
	extends SQLEncoder
{

	/**
	 * Temporary variable - used for storing the encoded size
	 */
	private long encodedSize;

	/**
	 * Temporary variable - used for storing the encoded sql size
	 */
	private long encodedSQLSize;

	/**
	 * (Constructor) Creates a new TimedSQLEncoder object.
	 *
	 * @param logger
	 *            Reference to the logger
	 *
	 * @throws ConfigurationException
	 *             if an error occurred while accessing the configuration
	 * @throws LoggingException
	 *             if an error occurred while accessing the logger
	 */
	public TimedSQLEncoder(final Logger logger)
		throws ConfigurationException, LoggingException
	{
		super(logger);
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Initializes the encoding information.
	 */
	public void init()
	{
		this.encodedSize = 0;
		this.encodedSQLSize = 0;
	}

	/**
	 * Returns the encoded size.
	 *
	 * @return encoded size
	 */
	public long getEncodedSize()
	{
		return encodedSize;
	}

	/**
	 * Returns the encoded sql size.
	 *
	 * @return encoded sql size
	 */
	public long getEncodedSQLSize()
	{
		return encodedSQLSize;
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Encodes the diff.
	 *
	 * @param task
	 *            Reference to the DiffTask
	 * @param diff
	 *            Diff to encode
	 * @return Base 64 encoded Diff
	 *
	 * @throws ConfigurationException
	 *             if an error occurred while accessing the configuration
	 * @throws UnsupportedEncodingException
	 *             if the character encoding is unsupported
	 * @throws DecodingException
	 *             if the decoding failed
	 * @throws EncodingException
	 *             if the encoding failed
	 * @throws SQLConsumerException
	 *             if an error occurred while encoding the diff
	 */
	protected String encodeDiff(final Task<Diff> task, final Diff diff)
		throws ConfigurationException, UnsupportedEncodingException,
		DecodingException, EncodingException, SQLConsumerException
	{

		String encoding = super.encodeDiff(task, diff);

		this.encodedSize += encoding.length();

		return encoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.tud.ukp.kulessa.delta.consumers.sql.codec.SQLEncodrInterface#binaryTask
	 * (de.tudarmstadt.ukp.kulessa.delta.data.tasks.Task)
	 */
	protected byte[] binaryDiff(final Task<Diff> task, final Diff diff)
		throws ConfigurationException, UnsupportedEncodingException,
		DecodingException, EncodingException, SQLConsumerException
	{

		byte[] encoding = super.binaryDiff(task, diff);

		this.encodedSize += encoding.length;

		return encoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.tudarmstadt.ukp.kulessa.delta.consumers.sql.codec.SQLEncodrInterface
	 * #encodeTask(de.tudarmstadt.ukp.kulessa.delta.data.tasks.Task)
	 */
	public SQLEncoding[] encodeTask(final Task<Diff> task)
		throws ConfigurationException, UnsupportedEncodingException,
		DecodingException, EncodingException, SQLConsumerException
	{

		SQLEncoding[] encoding = super.encodeTask(task);

		for (SQLEncoding sql : encoding) {
			this.encodedSQLSize += sql.byteSize();
		}

		return encoding;
	}
}
