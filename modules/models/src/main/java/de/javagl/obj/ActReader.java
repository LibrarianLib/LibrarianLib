/*
 * www.javagl.de - Obj
 *
 * Copyright (c) 2008-2015 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package de.javagl.obj;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ActReader
{
    public static Act read(InputStream inputStream) throws IOException
    {
        return read(inputStream, new Act());
    }

    public static Act read(InputStream inputStream, Act output) throws IOException
    {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        return readImpl(reader, output);
    }

    public static Act read(Reader reader) throws IOException
    {
        return read(reader, new Act());
    }

    public static Act read(Reader reader, Act output) throws IOException
    {
        if (reader instanceof BufferedReader)
        {
            return readImpl((BufferedReader)reader, output);
        }
        return readImpl(new BufferedReader(reader), output);
    }

    private static Act readImpl(BufferedReader reader, Act output) throws IOException
    {
        int lineNum = 0;
        while(true)
        {
            lineNum++;
            String line = reader.readLine();
            if(line == null)
            {
                break;
            }

            line = line.trim();

            //System.out.println("read line: "+line);

            // Combine lines that have been broken
            boolean finished = false;
            while(line.endsWith("\\"))
            {
                line = line.substring(0, line.length() - 2);
                lineNum++;
                String nextLine = reader.readLine();
                if (nextLine == null)
                {
                    finished = true;
                    break;
                }
                line += " " + nextLine;
            }
            if (finished)
            {
                break;
            }

            StringTokenizer st = new StringTokenizer(line);
            if(!st.hasMoreTokens())
            {
                continue;
            }

            String identifier = st.nextToken().toLowerCase();

            switch (identifier) {
                case "o":
                    output.beginObject(line.substring(line.indexOf('o') + 1).trim());
                    break;

                case "a":
                    output.beginAction(line.substring(line.indexOf('a') + 1).trim());
                    break;

                case "c":
                    output.beginChannel(line.substring(line.indexOf('c') + 1).trim());
                    break;

                case "k":
                    output.addSample(parseFloat(st.nextToken()), parseFloat(st.nextToken()));
                    break;
            }
        }
        return output;
    }

    /**
     * Parse a float from the given string, wrapping number format
     * exceptions into an IOException
     *
     * @param s The string
     * @return The float
     * @throws IOException If the string does not contain a valid float value
     */
    private static float parseFloat(String s) throws IOException
    {
        try
        {
            return Float.parseFloat(s);
        }
        catch (NumberFormatException e)
        {
            throw new IOException(e);
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ActReader()
    {
        // Private constructor to prevent instantiation
    }

}
