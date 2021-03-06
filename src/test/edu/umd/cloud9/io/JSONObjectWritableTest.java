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

package edu.umd.cloud9.io;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import junit.framework.JUnit4TestAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class JSONObjectWritableTest {

	@Test
	public void testSerialize1() throws Exception {
		JSONObjectWritable obj1 = new JSONObjectWritable();
		obj1.put("JSON", "Hello, World!");

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(bytesOut);

		obj1.write(dataOut);

		JSONObjectWritable obj2 = new JSONObjectWritable();
		obj2.readFields(new DataInputStream(new ByteArrayInputStream(bytesOut.toByteArray())));

		String s = "{\"JSON\":\"Hello, World!\"}";

		assertEquals(obj2.toString(), s);
	}
	
	@Test
	public void testSerialize2() throws Exception {
		JSONObjectWritable obj1 = new JSONObjectWritable();
		obj1.put("JSON", "'tis");

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(bytesOut);

		obj1.write(dataOut);

		JSONObjectWritable obj2 = new JSONObjectWritable();
		obj2.readFields(new DataInputStream(new ByteArrayInputStream(bytesOut.toByteArray())));

		String s = "{\"JSON\":\"'tis\"}";

		assertEquals(obj2.toString(), s);
	}

	@Test
	public void testSerialize() throws Exception {
		JSONObjectWritable obj = new JSONObjectWritable();

		obj.put("firstName", "John");
		obj.put("lastName", "Smith");

		JSONObject address = new JSONObject();
		address.put("streetAddress", "21 2nd Street");
		address.put("city", "New York");
		address.put("state", "NY");
		address.put("postalCode", 10021);

		JSONArray phoneNumbers = new JSONArray();
		phoneNumbers.put("212 555-1234");
		phoneNumbers.put("646 555-4567");

		obj.put("address", address);
		obj.put("phoneNumbers", phoneNumbers);

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(bytesOut);

		obj.write(dataOut);

		JSONObjectWritable obj2 = new JSONObjectWritable();
		obj2.readFields(new DataInputStream(new ByteArrayInputStream(bytesOut.toByteArray())));

		String s = "{\"lastName\":\"Smith\",\"address\":{\"streetAddress\":\"21 2nd Street\",\"postalCode\":10021,\"state\":\"NY\",\"city\":\"New York\"},\"phoneNumbers\":[\"212 555-1234\",\"646 555-4567\"],\"firstName\":\"John\"}";

		assertEquals(obj2.toString(), s);

		assertEquals(obj2.getString("firstName"), "John");
		assertEquals(obj2.getString("lastName"), "Smith");
		assertEquals(obj2.getJSONObject("address").getString("city"), "New York");
		assertEquals(obj2.getJSONArray("phoneNumbers").length(), 2);
		assertEquals(obj2.getJSONArray("phoneNumbers").getString(0), "212 555-1234");
	}

	@Test
	public void testRewrite() throws Exception {
		String s1 = "{\"JSON\":\"Hello, World!\"}";
		String s2 = "{\"JSON\":\"Hello!\"}";

		JSONObjectWritable obj = new JSONObjectWritable();
		obj.readJSONObject(s1);
		
		assertEquals(obj.toString(), s1);

		obj.readJSONObject(s2);
		assertEquals(obj.toString(), s2);

	}
	
	@Test
	public void testOverwrite() throws Exception {
		JSONObjectWritable obj = new JSONObjectWritable();
		obj.put("field", "longer string");
		assertEquals(obj.toString(), "{\"field\":\"longer string\"}");
		
		obj.put("field", "a");
		assertEquals(obj.toString(), "{\"field\":\"a\"}");
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(JSONObjectWritableTest.class);
	}

}
