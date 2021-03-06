import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class GvGReducer extends Reducer<Text, Text, Text, Text>
{
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
	{
		/*
			0,             1,	     2, 3, 4, 5, 6, 7, 8
			BatsmanCluster,BowlerCluster,0s,1s,2s,3s,4s,6s,Dismissal

			10,10,1,2,3,4,5,6,7
		*/
		//long[] stats = new long[9];
		double[] probs = new double[7];
		int count = 0;
		//(V Kohli,P Kumar	{'2,3,4,5,6,7,8,9,10,11','2,3,4,5,6,7,8,9,10,11',...}
		for(Text value : values)
		{
			String line = value.toString();
			String[] vals = line.split(",");
			for(int i = 0; i < 7; i++)
			{
				probs[i] += Double.parseDouble(vals[i]);
			}
			count += 1;
		}
		for(int i = 0; i < 7; i++)
		{
			probs[i] = probs[i] / count;	
		}
		
		
		context.write(key, new Text(probs[0] + "," + probs[1] + "," + probs[2] + "," + probs[3] + "," + probs[4] + "," + probs[5] + "," + probs[6]));
	}
}
