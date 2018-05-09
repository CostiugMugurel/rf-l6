package rf6;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils 
{
	private static final String inputFileValuesSeparator = ",";
	private static final String outputFileValuesSeparator = ",";
	
	
	
	
	protected static String calculateKNN(String[][] learningSet, double gradeUnknown, int k)
	{
			Neighbor[] neighbors = new Neighbor[learningSet.length];
			
			for(int i=0;i<learningSet.length;i++)
			{
				neighbors[i] = new Neighbor();
				neighbors[i].grade = Double.valueOf(learningSet[i][0]);
				neighbors[i].family = learningSet[i][1];
				neighbors[i].distance = Math.abs(neighbors[i].grade - gradeUnknown);
			}
			
			Arrays.sort(neighbors);
			
			ArrayList<Integer> frequencyNumber = new ArrayList<Integer>();
			ArrayList<String> frequencyName = new ArrayList<String>();
			frequencyNumber.add(1);
			frequencyName.add(neighbors[0].family);
			System.out.println("0 : " + neighbors[0].grade + " & " + neighbors[0].family);
			
			for(int i=1;i<k;i++)
			{
				System.out.println(i + " : " + neighbors[i].grade + " & " + neighbors[i].family);

				int pos = frequencyName.indexOf(neighbors[i].family);
				if(pos == -1)
				{
					frequencyNumber.add(1);
					frequencyName.add(neighbors[i].family);
				}
				else
				{
					int value = frequencyNumber.get(pos);
					frequencyNumber.set(pos, value+1);
				}
			}
			
			int index = 0;
			int max = Integer.MIN_VALUE;
			
			for(int i=0;i<frequencyNumber.size();i++)
			{
				if(frequencyNumber.get(i) > max)
				{
					max = frequencyNumber.get(i);
					index = i;
				}
			}
			
			return frequencyName.get(index);
	}
	
	
	
	protected static String[][] readLearningSetFromFile(String fileName) throws USVInputFileCustomException
	{
		//Start with an ArrayList<ArrayList<Double>>
		List<ArrayList<String>> learningSet = new ArrayList<ArrayList<String>>();
		// read file into stream, try-with-resources
		try  {
			Stream<String> stream = Files.lines(Paths.get(fileName));
			learningSet = stream.map(FileUtils::convertLineToLearningSetRow).collect(Collectors.toList());
		} 
		catch (FileNotFoundException fnfe)
		{
			throw new USVInputFileCustomException(" We cannot find the scepicified file on USV computer");
		}	
		catch (IOException ioe) {
			throw new USVInputFileCustomException(" We encountered some errors while trying to read the specified file: " + ioe.getMessage());
		}
		catch (Exception e) {
			throw new USVInputFileCustomException(" Other errors: " + e.getMessage());
		}	
		//  convert ArrayList<ArrayList<Double>> to double[][] for performance
		return convertToBiDimensionalArray(learningSet);
	}
	
	private static String[][] convertToBiDimensionalArray(List<ArrayList<String>> learningSet) {
		
		String[][] learningSetArray = new String[learningSet.size()][];
		
		for (int n = 0; n < learningSet.size(); n++) {
			ArrayList<String> rowListEntry = learningSet.get(n);
			
			// get each row double values
			String[] rowArray = new String[learningSet.get(n).size()];
			
			for (int p = 0; p < learningSet.get(n).size(); p++) 
			{				
				rowArray[p] = rowListEntry.get(p);
			}
			learningSetArray[n] = rowArray;
			
		}
		return learningSetArray;
	}
	
	private static ArrayList<String> convertLineToLearningSetRow(String line)
	{
		ArrayList<String> learningSetRow = new ArrayList<String>();
		String[] stringValues = line.split(inputFileValuesSeparator);
		//we need to convert from string to double
		for (int p = 0; p < stringValues.length; p++)
		{
			learningSetRow.add(stringValues[p]);
		}
		return learningSetRow;
	}
	
	protected static void writeLearningSetToFile(String fileName, double[][] normalizedSet)
	{
		// first create the byte array to be written
		StringBuilder stringBuilder = new StringBuilder();
		for(int n = 0; n < normalizedSet.length; n++) //for each row
		{
			//for each column
			 for(int p = 0; p < normalizedSet[n].length; p++) 
			 {
				//append to the output string
				 stringBuilder.append(normalizedSet[n][p]+"");
				 //if this is not the last row element
			      if(p < normalizedSet[n].length - 1)
			      {
			    	  //then add separator
			    	  stringBuilder.append(outputFileValuesSeparator);
			      }
			 }
			//append new line at the end of the row
			 stringBuilder.append("\n"); 
		}
		try {
			Files.write(Paths.get(fileName), stringBuilder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
