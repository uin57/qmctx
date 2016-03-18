
package ket.moi;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class POIExcelReader implements ExcelReader
{
	public POIExcelReader(String fileName)
	{
		workbook = getWorkbook(fileName);
	}

	@Override
	public ExcelSheet getSheet(int i) throws ReaderException
	{
		if( workbook == null )
			throw new ReaderException("excel file not found");
		if( i < 0 || i >= workbook.getNumberOfSheets() )
			throw new ReaderException("bad sheet index " + i);
		return new POIExcelSheet(workbook.getSheetAt(i));
	}
	
	private Workbook workbook = null;
	
	public static Workbook getWorkbook(String fileName)
	{
		try
		{
			if( fileName == null )
				return null;
			if( fileName.endsWith(".xls") || fileName.endsWith(".XLS") )
				return new HSSFWorkbook(new FileInputStream(fileName));
			if( fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX") )
				return new XSSFWorkbook(new FileInputStream(fileName));
	    }
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args)
	{
		try
		{
			String s00 = Factory.newExcelReader("e:\\workbook.xls").getSheet(0).getStringValue(0, 0);
			System.out.println(s00);
		}
		catch(ReaderException ex)
		{
			ex.printStackTrace();
		}
	}
	/*
	public static void main(String[] args)
	{
		System.out.println("ket.poi.Reader");
		Workbook wb = getWorkbook("e:\\workbook.xls");
		
		if( wb == null )
			return;
	 
	    Sheet sheet = wb.getSheetAt(0);
	    Row row = sheet.getRow(0);
	    Cell cell = row.getCell(0);
	    String msg = cell.getStringCellValue();
	    System.out.println(msg);

	}
	
	public static void method2() throws Exception {
		 
	    InputStream is = new FileInputStream("e:\\workbook.xls");
	    HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(is));
	 
	    ExcelExtractor extractor = new ExcelExtractor(wb);
	    extractor.setIncludeSheetNames(false);
	    extractor.setFormulasNotResults(false);
	    extractor.setIncludeCellComments(true);
	 
	    String text = extractor.getText();
	    System.out.println(text);
	}
	 
	public static void method3() throws Exception {
	    HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream("e:\\workbook.xls"));
	    HSSFSheet sheet = wb.getSheetAt(0);
	 
	    for (Iterator<Row> iter = (Iterator<Row>) sheet.rowIterator(); iter.hasNext();) {
	      Row row = iter.next();
	      for (Iterator<Cell> iter2 = (Iterator<Cell>) row.cellIterator(); iter2.hasNext();) {
	        Cell cell = iter2.next();
	        String content = cell.getStringCellValue();// 除非是sring类型，否则这样迭代读取会有错误
	        System.out.println(content);
	      }
	    }
	}
	*/
}
