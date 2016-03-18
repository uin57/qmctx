
package ket.moi;

public class Factory
{
	public static ExcelReader newExcelReader(String fileName)
	{
		return new POIExcelReader(fileName);
	}
}
