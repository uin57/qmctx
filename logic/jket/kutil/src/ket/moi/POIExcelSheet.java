
package ket.moi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class POIExcelSheet implements ExcelSheet
{
	public POIExcelSheet(Sheet sheet)
	{
		this.sheet = sheet;
	}

	@Override
	public boolean isNotEmpty(int r, int c)
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return ! cell.getStringCellValue().isEmpty();
			case Cell.CELL_TYPE_BOOLEAN:
				return ! new Boolean(cell.getBooleanCellValue()).toString().isEmpty();
			case Cell.CELL_TYPE_NUMERIC:
				return ! new Double(cell.getNumericCellValue()).toString().isEmpty() ;
			default:
				break;
			}
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	@Override
	public String getStringValue(int r, int c) throws ReaderException
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_BOOLEAN:
				return new Boolean(cell.getBooleanCellValue()).toString();
			case Cell.CELL_TYPE_NUMERIC:
				return new Double(cell.getNumericCellValue()).toString();
			default:
				throw new ReaderException("bad cell type" + cell.getCellType());
			}
		}
		catch(Exception ex)
		{
			throw new ReaderException("error value of sheet " + sheet.getSheetName() + ", row=" + (r+1) + ", col=" + (c+1), ex);
		}
	}
	
	@Override
	public byte getByteValue(int r, int c) throws ReaderException
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return Byte.parseByte(cell.getStringCellValue());
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? (byte)1 : 0;
			case Cell.CELL_TYPE_NUMERIC:
				return (byte)cell.getNumericCellValue();
			default:
				throw new ReaderException("bad cell type" + cell.getCellType());
			}
		}
		catch(Exception ex)
		{
			throw new ReaderException("error value of sheet " + sheet.getSheetName() + ", row=" + (r+1) + ", col=" + (c+1), ex);
		}
	}
	
	@Override
	public short getShortValue(int r, int c) throws ReaderException
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return Short.parseShort(cell.getStringCellValue());
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? (short)1 : 0;
			case Cell.CELL_TYPE_NUMERIC:
				return (short)cell.getNumericCellValue();
			default:
				throw new ReaderException("bad cell type" + cell.getCellType());
			}
		}
		catch(Exception ex)
		{
			throw new ReaderException("error value of sheet " + sheet.getSheetName() + ", row=" + (r+1) + ", col=" + (c+1), ex);
		}
	}
	
	@Override
	public int getIntValue(int r, int c) throws ReaderException
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return Integer.parseInt(cell.getStringCellValue());
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? 1 : 0;
			case Cell.CELL_TYPE_NUMERIC:
				return (int)cell.getNumericCellValue();
			default:
				throw new ReaderException("bad cell type" + cell.getCellType());
			}
		}
		catch(Exception ex)
		{
			throw new ReaderException("error value of sheet " + sheet.getSheetName() + ", row=" + (r+1) + ", col=" + (c+1), ex);
		}
	}
	
	@Override
	public long getLongValue(int r, int c) throws ReaderException
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return Long.parseLong(cell.getStringCellValue());
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? 1 : 0;
			case Cell.CELL_TYPE_NUMERIC:
				return (long)cell.getNumericCellValue();
			default:
				throw new ReaderException("bad cell type" + cell.getCellType());
			}
		}
		catch(Exception ex)
		{
			throw new ReaderException("error value of sheet " + sheet.getSheetName() + ", row=" + (r+1) + ", col=" + (c+1), ex);
		}
	}
	
	@Override
	public float getFloatValue(int r, int c) throws ReaderException
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return Float.parseFloat(cell.getStringCellValue());
			case Cell.CELL_TYPE_NUMERIC:
				return (float)cell.getNumericCellValue();
			default:
				throw new ReaderException("bad cell type" + cell.getCellType());
			}
		}
		catch(Exception ex)
		{
			throw new ReaderException("error value of sheet " + sheet.getSheetName() + ", row=" + (r+1) + ", col=" + (c+1), ex);
		}
	}
	
	@Override
	public double getDoubleValue(int r, int c) throws ReaderException
	{
		try
		{
			Cell cell = sheet.getRow(r).getCell(c);
			switch( cell.getCellType() )
			{
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_STRING:
				return Double.parseDouble(cell.getStringCellValue());
			case Cell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			default:
				throw new ReaderException("bad cell type" + cell.getCellType());
			}
		}
		catch(Exception ex)
		{
			throw new ReaderException("error value of sheet " + sheet.getSheetName() + ", row=" + (r+1) + ", col=" + (c+1), ex);
		}
	}
	
	private Sheet sheet;
}
