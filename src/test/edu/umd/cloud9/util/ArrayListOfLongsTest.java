/*
 * Cloud9: A MapReduce Library for Hadoop
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package edu.umd.cloud9.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class ArrayListOfLongsTest {

	@Test
	public void testBasic1() {
		int size = 100000;
		Random r = new Random();
		long[] ints = new long[size];

		ArrayListOfLongs list = new ArrayListOfLongs();
		for (int i = 0; i < size; i++) {
			long k = r.nextLong();
			list.add(k);
			ints[i] = k;
		}

		for (int i = 0; i < size; i++) {
			long v = list.get(i);

			assertEquals(ints[i], v);
		}
	}

	@Test
	public void testRemove() {
		ArrayListOfLongs list = new ArrayListOfLongs();
		for ( int i=0; i<10; i++) {
			list.add(i);
		}

		list.remove(list.indexOf(5));
		assertEquals(9, list.size());
		assertEquals(0, list.get(0));
		assertEquals(1, list.get(1));
		assertEquals(2, list.get(2));
		assertEquals(3, list.get(3));
		assertEquals(4, list.get(4));
		assertEquals(6, list.get(5));
		assertEquals(7, list.get(6));
		assertEquals(8, list.get(7));
		assertEquals(9, list.get(8));

		list.remove(list.indexOf(9));
		assertEquals(8, list.size());
		assertEquals(0, list.get(0));
		assertEquals(1, list.get(1));
		assertEquals(2, list.get(2));
		assertEquals(3, list.get(3));
		assertEquals(4, list.get(4));
		assertEquals(6, list.get(5));
		assertEquals(7, list.get(6));
		assertEquals(8, list.get(7));
	}

	@Test
	public void testUpdate() {
		int size = 100000;
		Random r = new Random();
		long[] longs = new long[size];

		ArrayListOfLongs list = new ArrayListOfLongs();
		for (int i = 0; i < size; i++) {
			long k = r.nextLong();
			list.add(k);
			longs[i] = k;
		}

		assertEquals(size, list.size());

		for (int i = 0; i < size; i++) {
			list.set(i, longs[i] + 1);
		}

		assertEquals(size, list.size());

		for (int i = 0; i < size; i++) {
			long v = list.get(i);

			assertEquals(longs[i] + 1, v);
		}
	}

	@Test
	public void testTrim1() {
		int size = 89;
		Random r = new Random();
		long[] longs = new long[size];

		ArrayListOfLongs list = new ArrayListOfLongs();
		for (int i = 0; i < size; i++) {
			long k = r.nextLong();
			list.add(k);
			longs[i] = k;
		}

		for (int i = 0; i < size; i++) {
			long v = list.get(i);

			assertEquals(longs[i], v);
		}

		long[] rawArray = list.getArray();
		int lenBefore = rawArray.length;

		list.trimToSize();
		long[] rawArrayAfter = list.getArray();
		int lenAfter = rawArrayAfter.length;

		assertEquals(89, lenAfter);
		assertTrue(lenBefore > lenAfter);
	}

	@Test
	public void testClone() {
		int size = 100000;
		Random r = new Random();
		long[] longs = new long[size];

		ArrayListOfLongs list1 = new ArrayListOfLongs();
		for (int i = 0; i < size; i++) {
			long k = r.nextLong();
			list1.add(k);
			longs[i] = k;
		}

		ArrayListOfLongs list2 = list1.clone();

		assertEquals(size, list1.size());
		assertEquals(size, list2.size());

		for (int i = 0; i < size; i++) {
			list2.set(i, longs[i] + 1);
		}

		// values in old list should not have changed
		assertEquals(size, list1.size());
		for (int i = 0; i < size; i++) {
			assertEquals(longs[i], list1.get(i));
		}

		// however, values in new list should have changed
		assertEquals(size, list1.size());
		for (int i = 0; i < size; i++) {
			assertEquals(longs[i] + 1, list2.get(i));
		}
	}

	@Test
	public void testToString() {
		int size = 10;
		Random r = new Random();

		ArrayListOfLongs list = new ArrayListOfLongs();
		for (int i = 0; i < size; i++) {
			list.add(r.nextLong());
		}

		String out = list.toString();
		for (int i = 0; i < size; i++) {
			long v = list.get(i);

			// Make sure the first 10 elements are printed out.
			assertTrue(out.indexOf(new Long(v).toString()) != -1);
		}

		for (int i = 0; i < size; i++) {
			list.add(r.nextInt(100000));
		}

		out = list.toString();
		for (int i = size; i < size+size; i++) {
			long v = list.get(i);

			// Make sure these elements are not printed out.
			assertTrue(out.indexOf(new Long(v).toString()) == -1);
		}

		assertTrue(out.indexOf(size + " more") != -1);
	}

	@Test
	public void testIterable() {
		int size = 1000;
		Random r = new Random();
		long[] longs = new long[size];

		ArrayListOfLongs list = new ArrayListOfLongs();
		for (int i = 0; i < size; i++) {
			long k = r.nextLong();
			list.add(k);
			longs[i] = k;
		}

		int i=0;
		for ( Long v : list) {
			assertEquals(longs[i++], (long) v);
		}

	}

	@Test
	public void testSetSize() {
		ArrayListOfLongs list = new ArrayListOfLongs();

		list.add(5L);
		assertEquals(1, list.size);
		assertEquals(5L, list.get(0));

		list.setSize(5);
		assertEquals(5L, list.size);
		assertEquals(0, list.get(1));
		assertEquals(0, list.get(2));
		assertEquals(0, list.get(3));
		assertEquals(0, list.get(4));

		list.add(12L);
		assertEquals(6, list.size);
		assertEquals(12L, list.get(5));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ArrayListOfLongsTest.class);
	}
}