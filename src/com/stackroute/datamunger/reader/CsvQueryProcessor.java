package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {
	   File data;
       BufferedReader br;
	   String fileName;

	/*
	 * Parameterized constructor to initialize filename. As you are trying to
	 * perform file reading, hence you need to be ready to handle the IO Exceptions.
	 */
	
	public CsvQueryProcessor(String fileName) throws FileNotFoundException {
		this.fileName=fileName;
		data =new File(fileName);
		FileReader fr=new FileReader(data);
		br=new BufferedReader(fr);
	}

	/*
	 * Implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 */

	@Override
	public Header getHeader() throws IOException {
		Header hd=new Header();
		FileReader fr=new FileReader(data);
		br=new BufferedReader(fr);
		String query=br.readLine();
		
		String[] res=query.trim().split(",");
		hd.setHeaders(res);
		return hd;		
	}

	/**
	 * This method will be used in the upcoming assignments
	 */
	@Override
	public void getDataRow() {

	}

	/*
	 * Implementation of getColumnType() method. To find out the data types, we will
	 * read the first line from the file and extract the field values from it. In
	 * the previous assignment, we have tried to convert a specific field value to
	 * Integer or Double. However, in this assignment, we are going to use Regular
	 * Expression to find the appropriate data type of a field. Integers: should
	 * contain only digits without decimal point Double: should contain digits as
	 * well as decimal point Date: Dates can be written in many formats in the CSV
	 * file. However, in this assignment,we will test for the following date
	 * formats('dd/mm/yyyy',
	 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm
	 * -dd')
	 */
	
	@Override
	public DataTypeDefinitions getColumnType() throws IOException {

		DataTypeDefinitions dt=new DataTypeDefinitions();
		
		Pattern ddmonyy = Pattern.compile("[0-9]{2}-[a-zA-Z]{3}-[0-9]{2}");
		Pattern ddmonyyyy = Pattern.compile("[0-9]{2}-[a-zA-Z]{3}-[0-9]{4}");
		Pattern ddmonthyy = Pattern.compile("[0-9]{2}-[a-zA-Z]{3,}-[0-9]{2}");
		Pattern ddmonthyyyy = Pattern.compile("[0-9]{2}-[a-zA-Z]{3,}-[0-9]{4}");
		Pattern ddmmyyyyslash = Pattern.compile("([0-2][0-9]|[3][01])/([0][0-9]|[1][0-2])/([0-9]{4})");
		Pattern mmddyyyyslash = Pattern.compile("([0][0-9]|[1][0-2])/([0-2][0-9]|[3][01])/([0-9]{4})");
		Pattern yyyymmdd = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		Pattern integer = Pattern.compile("[0-9]+");
		String col=br.readLine();
		String[] out2=col.trim().split(",");
		String res = "";
		for(int i=0;i<out2.length;i++)
		{
			if(ddmonyy.matcher(out2[i]).matches()) {
				res = res.concat("java.util.Date ");
			}
			else if(ddmonyyyy.matcher(out2[i]).matches()) {
				res = res.concat("java.util.Date ");
			}
			else if(ddmonthyy.matcher(out2[i]).matches()) {
				res = res.concat("java.util.Date ");
			}
			else if(ddmonthyyyy.matcher(out2[i]).matches()) {
				res = res.concat("java.util.Date ");
			}
			else if(ddmmyyyyslash.matcher(out2[i]).matches()) {
				res = res.concat("java.util.Date ");
			}
			else if(mmddyyyyslash.matcher(out2[i]).matches()) {
				res = res.concat("java.util.Date ");
			}
			else if(yyyymmdd.matcher(out2[i]).matches()) {
				res = res.concat("java.util.Date ");
			}
			else
			{
			 try
			{
			Integer.parseInt(out2[i]);
			res=res.concat("java.lang.Integer ");
			}
			catch(Exception e)
			{
			try
			{
			Double.parseDouble(out2[i]);
			res=res.concat("java.lang.Double ");
			}
			catch(Exception f)
			{
			res=res.concat("java.lang.String ");
			}
			}
			}
		}						
		   res=res.concat("java.lang.Object");
		   dt.setDataTypes(res.split(" "));
		  return dt;	
	}
}
