/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hwpf.usermodel;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndian;

/**
 * This data structure is used by a paragraph to determine how it should drop
 * its first letter. I think its the visual effect that will show a giant first
 * letter to a paragraph. I've seen this used in the first paragraph of a
 * book
 *
 * @author Ryan Ackley
 */
public class DropCapSpecifier
{
  private short _info;
    private static BitField _type = BitFieldFactory.getInstance(0x07);
    private static BitField _lines = BitFieldFactory.getInstance(0xf8);

  public DropCapSpecifier(byte[] buf, int offset)
  {
    this(LittleEndian.getShort(buf, offset));
  }

  public DropCapSpecifier(short info)
  {
    _info = info;
  }

  public short toShort()
  {
    return _info;
  }
}
