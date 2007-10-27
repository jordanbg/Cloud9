package edu.umd.cloud9.tuple;

import java.io.IOException;

public class PackRecordsReadDemo {
	public static final Schema RECORD_SCHEMA = new Schema();
	static {
		RECORD_SCHEMA.addField("text", String.class, "");
	}

	public static Tuple tuple = RECORD_SCHEMA.instantiate();

	public static void main(String[] args) throws IOException {
		String file = "../umd-hadoop-dist/sample-input/bible+shakes.nopunc.packed";
		
		LocalTupleRecordReader reader = new LocalTupleRecordReader(file);
		while ( reader.read(tuple) ) {
			System.out.println(tuple.get(0));
		}
		reader.close();
		
		System.out.println("Wrote " + reader.getRecordCount() + " records.");
	}

}