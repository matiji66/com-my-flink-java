package org.com.pateo.flink.streaming;


import java.io.IOException;

import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 
 * This is an example how to write streams into HBase. In this example the
 * stream will be written into a local Hbase but it is possible to adapt this
 * example for an HBase running in a cloud. You need a running local HBase with a
 * table "flinkExample" and a column "entry". If your HBase configuration does
 * not fit the hbase-site.xml in the resource folder then you gave to delete temporary this
 * hbase-site.xml to execute the example properly.
 * 
 */
public class HBaseWriteStreamExample {

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment
				.getExecutionEnvironment();

		// data stream with random numbers
		DataStream<String> dataStream = env.addSource(new SourceFunction<String>() {
			private static final long serialVersionUID = 1L;

			private volatile boolean isRunning = true;

			@Override
			public void run(SourceContext<String> out) throws Exception {
				while (isRunning) {
					out.collect(String.valueOf(Math.floor(Math.random() * 100)));
				}

			}

			@Override
			public void cancel() {
				isRunning = false;
			}
		});
		dataStream.writeUsingOutputFormat(new HBaseOutputFormat());

		env.execute();
	}

	/**
	 * 
	 * This class implements an OutputFormat for HBase
	 *
	 */
	private static class HBaseOutputFormat implements OutputFormat<String> {

		private org.apache.hadoop.conf.Configuration conf = null;
		private HTable table = null;
		private String taskNumber = null;
		private int rowNumber = 0;

		private static final long serialVersionUID = 1L;

		@Override
		public void configure(Configuration parameters) {
			conf = HBaseConfiguration.create();
		}

		@Override
		public void open(int taskNumber, int numTasks) throws IOException {
			table = new HTable(conf, "flinkExample");
			this.taskNumber = String.valueOf(taskNumber);
		}

		@Override
		public void writeRecord(String record) throws IOException {
			Put put = new Put(Bytes.toBytes(taskNumber + rowNumber));
			put.add(Bytes.toBytes("entry"), Bytes.toBytes("entry"),
					Bytes.toBytes(rowNumber));
			rowNumber++;
			table.put(put);
		}

		@Override
		public void close() throws IOException {
			table.flushCommits();
			table.close();
		}

	}
}
