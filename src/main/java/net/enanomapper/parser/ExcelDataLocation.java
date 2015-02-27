package net.enanomapper.parser;

import net.enanomapper.parser.ParserConstants.DataType;
import net.enanomapper.parser.ParserConstants.IterationAccess;
import net.enanomapper.parser.ParserConstants.Recognition;

import org.apache.poi.ss.usermodel.Cell;
 


public class ExcelDataLocation 
{
	private boolean FlagExtractValueQualifier = false; 
	private boolean FlagExtractAsRichValue = false;
	private int parallelSheetIndex = -1;  //This is not the sheetIndex. This is the index within array GenericExcelParser.parallelSheets[]
	
	
	private Object absoluteLocationValue = null;
	private Object jsonValue = null;
	private String jsonRepositoryKey = null;
	private String variableKey = null;
	
	public boolean sourceCombination = false;
	public boolean FlagSourceCombination = false;
	
	public int nErrors = 0;
	public String sectionName = "";
	
	public DataType dataType = DataType.CELL;
	public boolean FlagDataType = false;
	
	public Recognition recognition = Recognition.BY_INDEX;
	public boolean FlagRecognition = false;
	
	//public CellType cellType = CellType.STRING;
	//public boolean FlagCellType = false;
	
	public IterationAccess iteration = IterationAccess.ROW_SINGLE;	
	public boolean FlagIteration = false;
	
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
	
	public int columnIndices[] = null; //Used only in mode IterationAccess.COMBINATION
	
	public int rowIndices[] = null; //Used only in mode IterationAccess.COMBINATION
	
	public String variableKeys[] = null; //Used only in mode IterationAccess.COMBINATION
	
	
	public String toJSONKeyWord(String offset)
	{
		int nFields = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(offset + "\"" + sectionName + "\":\n");
		sb.append(offset + "{\n");
		
		if (FlagSourceCombination)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"SOURCE_COMBINATION\" : " + sourceCombination);
			nFields++;
		}
		
		if (FlagDataType)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"DATA_TYPE\" : \"" + dataType.toString() + "\"");
			nFields++;
		}
		
		if (FlagRecognition)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"RECOGNITION\" : \"" + recognition.toString() + "\"");
			nFields++;
		}
		
		if (FlagIteration)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"ITERATION\" : \"" + iteration.toString() + "\"");
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
				
			sb.append(offset + "\t\"SHEET_INDEX\" : " + (sheetIndex + 1)); //0 --> 1-based
			nFields++;
		}
		
		if (FlagSheetName)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"SHEET_NAME\" : \"" + sheetName+"\"");
			nFields++;
		}
		
		if (FlagColumnIndex)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"COLUMN_INDEX\" : " + (columnIndex + 1) ); //0 --> 1-based
			nFields++;
		}
		
		if (columnIndices != null)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"COLUMN_INDICES\" : [" );
			for (int i = 0; i < columnIndices.length; i++)
			{	
				sb.append(columnIndices[i]+1);
				if (i < (columnIndices.length -1))
					sb.append(", ");
			}	
			sb.append("]");
			nFields++;
		}
		
		if (FlagColumnName)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"COLUMN_NAME\" : \"" + columnName + "\"");
			nFields++;
		}
		
		if (FlagRowIndex)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"ROW_INDEX\" : " + (rowIndex + 1) ); //0 --> 1-based
			nFields++;
		}
		
		if (rowIndices != null)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"ROW_INDICES\" : [" );
			for (int i = 0; i < rowIndices.length; i++)
			{	
				sb.append(rowIndices[i]+1);
				if (i < (rowIndices.length -1))
					sb.append(", ");
			}	
			sb.append("]");
			nFields++;
		}
		
		if (FlagRowName)
		{
			if (nFields > 0)
				sb.append(",\n");
				
			sb.append(offset + "\t\"ROW_NAME\" : \"" + rowName + "\"");
			nFields++;
		}
		
		if (variableKeys != null)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"VARIABLE_KEYS\" : [" );
			for (int i = 0; i < variableKeys.length; i++)
			{	
				sb.append("\"" + variableKeys[i] + "\"");
				if (i < (variableKeys.length -1))
					sb.append(", ");
			}	
			sb.append("]");
			nFields++;
		}
		
		if (nFields > 0)
			sb.append("\n");
		
		sb.append(offset + "}");
		
		
		
		return sb.toString();
	}

	public Object getAbsoluteLocationValue() {
		return absoluteLocationValue;
	}

	public void setAbsoluteLocationValue(Object absoluteLocationValue) {
		this.absoluteLocationValue = absoluteLocationValue;
	}

	public Object getJsonValue() {
		return jsonValue;
	}

	public void setJsonValue(Object jsonValue) {
		this.jsonValue = jsonValue;
	}

	public String getJsonRepositoryKey() {
		return jsonRepositoryKey;
	}

	public void setJsonRepositoryKey(String jsonRepositoryKey) {
		this.jsonRepositoryKey = jsonRepositoryKey;
	}
	
	public String getVariableKey() {
		return variableKey;
	}

	public void setVariableKey(String variableKey) {
		this.variableKey = variableKey;
	}

	public boolean isFlagExtractValueQualifier() {
		return FlagExtractValueQualifier;
	}

	public void setFlagExtractValueQualifier(boolean flagExtractValueQualifier) {
		FlagExtractValueQualifier = flagExtractValueQualifier;
	}

	public boolean isFlagExtractAsRichValue() {
		return FlagExtractAsRichValue;
	}

	public void setFlagExtractAsRichValue(boolean flagExtractAsRichValue) {
		FlagExtractAsRichValue = flagExtractAsRichValue;
	}

	public int getParallelSheetIndex() {
		return parallelSheetIndex;
	}

	public void setParallelSheetIndex(int parallelSheetIndex) {
		this.parallelSheetIndex = parallelSheetIndex;
	}
	
	public boolean isFromParallelSheet(){
		return (parallelSheetIndex >= 0);
	}	
}
