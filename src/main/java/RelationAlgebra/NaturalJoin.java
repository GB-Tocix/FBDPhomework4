package RelationAlgebra;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class NaturalJoin {
    public static class NaturalJoinMap extends Mapper<Text, BytesWritable, Text, Text> {
        private int col;
        private String relationNameA;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            relationNameA = context.getConfiguration().get("relationNameA");
            col = context.getConfiguration().getInt("col", 0);
        }

        @Override
        public void map(Text relationName, BytesWritable content, Context context) throws
                IOException, InterruptedException {
            String[] records = new String(content.getBytes(), "UTF-8").split("\\n");
            if (relationName.toString().equalsIgnoreCase(relationNameA)) {
                for (int i = 0; i < records.length; i++) {
                    RelationA record = new RelationA(records[i]);
                    context.write(new Text(record.getCol(col)),
                            new Text(relationName.toString() + " " + record.getValueExcept(col)));
                }
            } else {
                for (int i = 0; i < records.length; i++) {
                    RelationB record = new RelationB(records[i]);
                    context.write(new Text(record.getCol(col)),
                            new Text(relationName.toString() + " " + record.getValueExcept(col)));
                }
            }
        }
    }

    public static class NaturalJoinReduce extends Reducer<Text, Text, Text, NullWritable> {
        private String relationNameA;

        protected void setup(Context context) throws IOException, InterruptedException {
            relationNameA = context.getConfiguration().get("relationNameA");
        }

        public void reduce(Text key, Iterable<Text> value, Context context) throws
                IOException, InterruptedException {
            ArrayList<Text> setR = new ArrayList<Text>();
            ArrayList<Text> setS = new ArrayList<Text>();
            for (Text val : value) {
                String[] recordInfo = val.toString().split(" ");
                if (recordInfo[0].equalsIgnoreCase(relationNameA))
                    setR.add(new Text(recordInfo[1]));
                else
                    setS.add(new Text(recordInfo[1]));
            }
            for (int i = 0; i < setR.size(); i++) {
                for (int j = 0; j < setS.size(); j++) {
                    String[] Ri_split = setR.get(i).toString().split(",");
                    String[] Si_split = setS.get(j).toString().split(",");
                    Text t = new Text(key.toString() + "," + Ri_split[0] + "," + Ri_split[1] + "," +
							Si_split[0] + "," + Ri_split[2] + "," + Si_split[1]);
                    context.write(t, NullWritable.get());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Job naturalJoinJob = new Job();
        naturalJoinJob.setJobName("naturalJoinJob");
        naturalJoinJob.setJarByClass(NaturalJoin.class);
        naturalJoinJob.getConfiguration().setInt("col", Integer.parseInt(args[2]));
        naturalJoinJob.getConfiguration().set("relationNameA", args[3]);

        naturalJoinJob.setMapperClass(NaturalJoinMap.class);
        naturalJoinJob.setMapOutputKeyClass(Text.class);
        naturalJoinJob.setMapOutputValueClass(Text.class);

        naturalJoinJob.setReducerClass(NaturalJoinReduce.class);
        naturalJoinJob.setOutputKeyClass(Text.class);
        naturalJoinJob.setOutputValueClass(NullWritable.class);

        naturalJoinJob.setInputFormatClass(WholeFileInputFormat.class);
        naturalJoinJob.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.addInputPath(naturalJoinJob, new Path(args[0]));
        FileOutputFormat.setOutputPath(naturalJoinJob, new Path(args[1]));

        naturalJoinJob.waitForCompletion(true);
        System.out.println("finished!");
    }
}
