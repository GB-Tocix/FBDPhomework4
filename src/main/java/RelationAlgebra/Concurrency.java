package RelationAlgebra;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class Concurrency {
	public static class ConcurrencyMap extends Mapper<LongWritable, Text, RelationA, IntWritable>{
		private IntWritable one = new IntWritable(1);
		@Override
		public void map(LongWritable offSet, Text line, Context context)throws 
		IOException, InterruptedException{
			RelationA record = new RelationA(line.toString());
			context.write(record, one);
		}
	}
	public static class ConcurrencyReduce extends Reducer<RelationA, IntWritable, RelationA, NullWritable>{
		@Override
		public void reduce(RelationA key, Iterable<IntWritable> value, Context context) throws 
		IOException,InterruptedException{
			int sum = 0;
			for(IntWritable val : value){
				sum += val.get();
			}
			if(sum >= 1)
				context.write(key, NullWritable.get());
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
		Job ConcurrencyJob = new Job();
		ConcurrencyJob.setJobName("ConcurrencyJob");
		ConcurrencyJob.setJarByClass(Concurrency.class);
	
		ConcurrencyJob.setMapperClass(ConcurrencyMap.class);
		ConcurrencyJob.setMapOutputKeyClass(RelationA.class);
		ConcurrencyJob.setMapOutputValueClass(IntWritable.class);

		ConcurrencyJob.setReducerClass(ConcurrencyReduce.class);
		ConcurrencyJob.setOutputKeyClass(RelationA.class);
		ConcurrencyJob.setOutputValueClass(NullWritable.class);

		//ConcurrencyJob.setInputFormatClass(TextInputFormat.class);
		ConcurrencyJob.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(ConcurrencyJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(ConcurrencyJob, new Path(args[1]));
		
		ConcurrencyJob.waitForCompletion(true);
		System.out.println("finished!");
	}
}
