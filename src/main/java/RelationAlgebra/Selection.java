package RelationAlgebra;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Selection {
	public static class SelectionMap extends Mapper<LongWritable, Text, RelationA, NullWritable>{
		private int id;
		private String value;
		@Override
		protected void setup(Context context) throws IOException,InterruptedException{
			id = context.getConfiguration().getInt("col", 0);
			value = context.getConfiguration().get("value");
		}

		public void map(LongWritable offSet, Text line, Context context)throws
		IOException, InterruptedException{
			RelationA record = new RelationA(line.toString());
			if(record.isCondition(id, value))
				context.write(record, NullWritable.get());
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
		Configuration conf = new Configuration();

		conf.setInt("col", Integer.parseInt(args[2]));
		conf.set("value", args[3]);
		Job selectionJob = Job.getInstance(conf, "selectionJob");

		selectionJob.setJarByClass(Selection.class);
		selectionJob.setMapperClass(SelectionMap.class);
		selectionJob.setMapOutputKeyClass(RelationA.class);
		selectionJob.setMapOutputValueClass(NullWritable.class);

		selectionJob.setNumReduceTasks(0);

		//selectionJob.setInputFormatClass(WholeFileInputFormat.class);
		selectionJob.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(selectionJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(selectionJob, new Path(args[1]));
		selectionJob.waitForCompletion(true);
		System.out.println("finished!");
	}
}
