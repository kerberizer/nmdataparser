package net.enanomapper.parser;



public class ExcelDataLocation 
{
	public enum IterationAccess {
		ROW_SINGLE, ROW_MULTI_FIXED, ROW_MULTI_DYNAMIC, UNDEFINED;
		
		public static IterationAccess fromString(String s)
		{	 
			try
			{
				IterationAccess access = IterationAccess.valueOf(s) ;
				return (access);
			}
			catch (Exception e)
			{
				return IterationAccess.UNDEFINED;
			}
		}
	}
	
	public enum DataType {
		CELL, ROW, COLUMN, BLOCK, UNDEFINED;
		
		public static DataType fromString(String s)
		{
			try
			{
				DataType type  = DataType.valueOf(s) ;
				return (type);
			}
			catch (Exception e)
			{
				return DataType.UNDEFINED;
			}
		}
	}
	
	public enum Recognition {
		BY_INDEX, BY_NAME, BY_INDEX_AND_NAME, UNDEFINED;
		
		public static Recognition fromString(String s)
		{
			try
			{
				Recognition rec  = Recognition.valueOf(s) ;
				return (rec);
			}
			catch (Exception e)
			{
				return Recognition.UNDEFINED;
			}
		}
	}
	
	public String error = null;
	
	public String name = "";
	
	public DataType dataType = DataType.CELL;
	public boolean FlagDataType = false;
	
	public IterationAccess dataAccess = IterationAccess.ROW_SINGLE;	
	public boolean FlagDataAccess = false;
	
	public boolean allowEmpty = true;
	public boolean FlagAllowEmpty = false;
	
	public int sheetIndex = 0;
	public boolean FlagSheetIndex = false;
	
	public String sheetName = null;
	public boolean FlagSheetName = false;
	
	public int columnIndex = 0;
	public boolean FlagColumnIndex = false;
	
	public String columnName = null;
	public boolean FlagColumnName = false;
	
	public int rowIndex = 0;
	public boolean FlagRowIndex = false;
	
	public String rowName = null;
	public boolean FlagRowName = false;
	
	
	public String toJSONKeyWord(String offset)
	{
		int nFields = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(offset + name + ":\n");
		sb.append(offset + "{\n");
		
		if (FlagDataType)
		{
			sb.append(offset + "\t\"DATA_TYPE\" : \"" + dataType.toString() + "\"");
			nFields++;
		}
		
		if (FlagDataAccess)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"DATA_ACCESS\" : \"" + dataAccess.toString() + "\"");
			nFields++;
		}
		
		if (FlagAllowEmpty)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"ALLOW_EMPTY\" : " + allowEmpty);
			nFields++;
		}
		
		if (FlagSheetIndex)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"SHEET_INDEX\" : " + sheetIndex);
			nFields++;
		}
		
		if (FlagSheetName)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"SHEET_NAME\" : " + sheetName);
			nFields++;
		}
		
		if (FlagColumnIndex)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"COLUMN_INDEX\" : " + columnIndex);
			nFields++;
		}
		
		if (FlagColumnName)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"COLUMN_NAME\" : " + columnName);
			nFields++;
		}
		
		if (FlagRowIndex)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"ROW_INDEX\" : " + rowIndex);
			nFields++;
		}
		
		if (FlagRowName)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"ROW_NAME\" : " + rowName);
			nFields++;
		}
		
		if (nFields > 0)
			sb.append("\n");
		
		sb.append(offset + "}");
		
		return sb.toString();
	}
	
}