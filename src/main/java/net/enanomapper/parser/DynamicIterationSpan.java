package net.enanomapper.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonNode;

import ambit2.base.data.SubstanceRecord;
import net.enanomapper.parser.ParserConstants.DynamicIteration;
import net.enanomapper.parser.ParserConstants.ElementDataType;
import net.enanomapper.parser.excel.ExcelUtils;
import net.enanomapper.parser.excel.ExcelUtils.IndexInterval;
import net.enanomapper.parser.json.JsonUtilities;



/**
 * 
 * @author nick
 * This class defines information for dynamic extraction of information during iteration
 * This info is typically used by the iteration modes: ROW_MULTI_*, COLUMN_MULTI_*
 * 
 * 
 */
public class DynamicIterationSpan 
{	
	
	public static class RowObject{
		public ArrayList<Object> elementObjects = null;
		public Object rowObject = null;
	}
	
	
	public boolean isPrimarySheet = false;
	public DynamicIteration dynamicIteration = DynamicIteration.NEXT_NOT_EMPTY;
	
	
	
	public boolean handleByRows = true;    //The flag is related to the iteration mode and it determines whether basic data elements are rows or columns
	public boolean FlagHandleByRows = false;
	
	public ElementDataType cumulativeObjectType = null; //This is what type of object is formed by the cumulative effect of all of rows/columns
	public ElementDataType rowType = null;  //This is the default row level grouping 
	//public DataElementType columnType = null;  //This is the default column level grouping
	public ArrayList<DynamicElement> elements = null;  
	public ArrayList<DynamicGrouping> groupLevels = null;
	
	
	//Error handling
	public boolean FlagStoreErrors = true;
	Logger logger = null;
	public String masterErrorString = ""; //This is used for error messaging 
	public ArrayList<String> errors = new ArrayList<String>(); 
	
	//Work variables
	private Row firstRow = null;
	private Row firstGroupRow = null;
	
	
	public static DynamicIterationSpan extractDynamicIterationSpan(JsonNode node, ExcelParserConfigurator conf, String masterSection)
	{
		DynamicIterationSpan dis = new DynamicIterationSpan(); 
		JsonUtilities jsonUtils = new JsonUtilities();
		
		//HANDLE_BY_ROWS
		if(!node.path("HANDLE_BY_ROWS").isMissingNode())
		{
			Boolean b =  jsonUtils.extractBooleanKeyword(node, "HANDLE_BY_ROWS", true);
			if (b == null)
				conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
						+ "\"HANDLE_BY_ROWS\": " + jsonUtils.getError());
			else
			{	
				dis.handleByRows = b;
				dis.FlagHandleByRows = true;
			}	
		}
		
		
		//CUMULATIVE_OBJECT_TYPE
		if(node.path("CUMULATIVE_OBJECT_TYPE").isMissingNode())
		{
			conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
					+ "\"CUMULATIVE_OBJECT_TYPE\": is missing!");
		}
		else
		{
			String keyword =  jsonUtils.extractStringKeyword(node, "CUMULATIVE_OBJECT_TYPE", false);
			if (keyword == null)
				conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
						+ "\"CUMULATIVE_OBJECT_TYPE\": " + jsonUtils.getError());
			else
			{	
				dis.cumulativeObjectType = ElementDataType.fromString(keyword);
				if (dis.cumulativeObjectType == ElementDataType.UNDEFINED)
					conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
							+ "\"CUMULATIVE_OBJECT_TYPE\" is incorrect or UNDEFINED!  -->"  + keyword);
			}	
		}
		
		//ROW_TYPE
		if(node.path("ROW_TYPE").isMissingNode())
		{
			//Not treated as an error.
		}
		else
		{
			String keyword =  jsonUtils.extractStringKeyword(node, "ROW_TYPE", false);
			if (keyword == null)
				conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
						+ "\"ROW_TYPE\": " + jsonUtils.getError());
			else
			{	
				dis.rowType = ElementDataType.fromString(keyword);
				if (dis.rowType == ElementDataType.UNDEFINED)
					conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
							+ "\"ROW_TYPE\" is incorrect or UNDEFINED! --> " + keyword);
			}	
		}
		
		
		if(!node.path("ELEMENTS").isMissingNode())
		{
			JsonNode elNode = node.path("ELEMENTS");
			if (elNode.isArray())
			{
				dis.elements = new ArrayList<DynamicElement>();
				for (int i = 0; i < elNode.size(); i++)
				{
					DynamicElement el = DynamicElement.extractDynamicElement(elNode.get(i), conf, masterSection, i);
					if (el != null)
						dis.elements.add(el);
				}	
			}
			else
				conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
						+ "\"ELEMENTS\" is not an array!");
		}
		
		if(!node.path("GROUP_LEVELS").isMissingNode())
		{
			JsonNode elNode = node.path("GROUP_LEVELS");
			if (elNode.isArray())
			{
				dis.groupLevels = new ArrayList<DynamicGrouping>();
				for (int i = 0; i < elNode.size(); i++)
				{
					DynamicGrouping grp = DynamicGrouping.extractDynamicGrouping(elNode.get(i), conf, masterSection, i);
					if (grp != null)
						dis.groupLevels.add(grp);
				}	
			}
			else
				conf.configErrors.add("In JSON Section \"" + masterSection + "\" subsection \"DYNAMIC_ITERATION_SPAN\" keyword "
						+ "\"GROUP_LEVELS\" is not an array!");
		}
		
		return dis;
	}
	
	
	public String toJSONKeyWord(String offset)
	{
		int nFields = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(offset + "\"DYNAMIC_ITERATION_SPAN\":\n");
		sb.append(offset + "{\n");
		
		
		if (FlagHandleByRows)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"HANDLE_BY_ROWS\" : " + handleByRows + "");
			nFields++;
		}
		
		if (cumulativeObjectType != null)
		{
			if (nFields > 0)
				sb.append(",\n");
			sb.append(offset + "\t\"CUMULATIVE_OBJECT_TYPE\" : \"" + cumulativeObjectType.toString() + "\"");
			nFields++;
		}
		
		if (elements != null)
		{
			if (nFields > 0)
				sb.append(",\n\n");
			
			sb.append(offset + "\t\"ELEMENTS\":\n");
			sb.append(offset + "\t[\n");
			for (int i = 0; i < elements.size(); i++)
			{	
				sb.append(elements.get(i).toJSONKeyWord(offset + "\t\t"));			
				if (i < elements.size()-1) 
					sb.append(",\n");
				sb.append("\n");
			}
			sb.append(offset+"\t]"); 
		}
		
		if (groupLevels != null)
		{
			if (nFields > 0)
				sb.append(",\n\n");
			
			sb.append(offset + "\t\"GROUP_LEVELS\":\n");
			sb.append(offset + "\t[\n");
			for (int i = 0; i < groupLevels.size(); i++)
			{	
				sb.append(groupLevels.get(i).toJSONKeyWord(offset + "\t\t"));			
				if (i < groupLevels.size()-1) 
					sb.append(",\n");
				sb.append("\n");
			}
			sb.append(offset+"\t],\n\n"); 
		}
		
		
		if (nFields > 0)
			sb.append("\n");
		
		sb.append(offset + "}");
		
		return sb.toString();
	}
	
	
	public boolean checkConsistency()
	{	
		if (rowType != null)
			if (!rowType.isElementOf(cumulativeObjectType))
				addError(masterErrorString + " ROW_TYPE "  + rowType.toString() + 
						" is inconsistent with CULULATIVE_OBJECT_TYPE " + cumulativeObjectType.toString());
		
		checkElementConsistency();
		checkGroupLevelConsistency();
		
		return true;
	}
	
	protected boolean checkElementConsistency()
	{
		if (elements == null)
			return true;
		
		if (elements.isEmpty())
			return true;
		
		if (rowType != null)
			for (int i = 0; i < elements.size(); i++)
			{
				if (!elements.get(i).dataType.isElementOf(rowType))
					addError(masterErrorString + " ELEMENTS[" + (i+1) + "] type " + elements.get(i).dataType.toString() + 
							" is inconsistent with ROW_TYPE " + rowType.toString());
			}
		
		return true;
	}
		
	protected boolean checkGroupLevelConsistency()
	{	
		if (groupLevels == null)
			return true;
		
		if (groupLevels.isEmpty())
			return true;
		
		
		if (!groupLevels.get(0).groupCumulativeType.isElementOf(cumulativeObjectType))
			addError(masterErrorString + " GROUP_LEVELS[1].groupCumulativeType " +  groupLevels.get(0).groupCumulativeType.toString() + 
					" is not an element of cumulativeObjectType " + cumulativeObjectType.toString());

		for (int i = 0; i < groupLevels.size(); i++)
		{
			if (!groupLevels.get(i).checkConsistency())
				addError(masterErrorString + " GROUP_LEVELS[" + (i+1) + "] inconsistency error!");
			
			if (i > 0)
				if (!groupLevels.get(i).groupCumulativeType.isElementOf(groupLevels.get(i-1).groupCumulativeType))
					addError(masterErrorString + " GROUP_LEVELS[" + (i+1) + "].groupCumulativeType " + groupLevels.get(i).groupCumulativeType.toString() 
							+ " is not an element of GROUP_LEVELS[" + i + "].groupCumulativeType " + groupLevels.get(i-1).groupCumulativeType.toString());
		}
				
		return true;
	}
	
	
	public DynamicIterationObject getDynamicIterationObjectFromRows(ArrayList<Row> rows)
	{	
		if (rows == null)
			return null;
		else
			if (rows.isEmpty())
				return null;
		
		if (groupLevels == null)
		{	
			firstRow = rows.get(0);
			return handleRows(rows, cumulativeObjectType);
		}	
		else
			return handleGroupsLavels(rows);
	}
	
	
	protected DynamicIterationObject handleGroupsLavels(ArrayList<Row> rows)
	{
		DynamicIterationObject dio = new DynamicIterationObject();
		
		//Currently only one grouping level is handled (element 0)
		firstRow = rows.get(0);
		boolean FlagNextNonEmpty = (dynamicIteration == DynamicIteration.NEXT_NOT_EMPTY);
		TreeMap<Integer, String> groups = ExcelUtils.getRowGroups(rows, groupLevels.get(0).groupingElementIndex,  FlagNextNonEmpty);
		
		Integer prevInt = null;
		for (Entry<Integer, String> entry : groups.entrySet())
		{
			if (prevInt != null)
			{
				ArrayList<Row> grpRows = new ArrayList<Row>();
				for (int i = prevInt; i <= entry.getKey()-1; i++)
					grpRows.add(rows.get(i));
				
				firstGroupRow = grpRows.get(0);
				DynamicIterationObject grpDio = handleRows(grpRows, groupLevels.get(0).groupCumulativeType);
				dio.groupDIOs.add(grpDio);
			}
			prevInt = entry.getKey();
		}
		
		
		ArrayList<Row> grpRows = new ArrayList<Row>();
		for (int i = prevInt; i <= rows.size()-1; i++)
			grpRows.add(rows.get(i));
		
		firstGroupRow = grpRows.get(0);
		DynamicIterationObject grpDio = handleRows(grpRows, groupLevels.get(0).groupCumulativeType);
		dio.groupDIOs.add(grpDio);
		
		return dio;
	}
	
	
	protected DynamicIterationObject handleRows(ArrayList<Row> rows, ElementDataType resultType)
	{
		DynamicIterationObject dio = new DynamicIterationObject ();
		for (int i = 0; i < rows.size(); i ++)
		{
			RowObject obj = getRowObject(rows.get(i), resultType);
			dio.rowObjects.add(obj);
		}
		
		
		return dio;
	}
	
	
	protected RowObject getRowObject(Row row, ElementDataType resultType)
	{	
		RowObject robj = new RowObject();
		robj.elementObjects = getElementObjects(row);
		
		/*
		switch (resultType)
		{
		case SUBSTANCE: {
			SubstanceRecord r = new  SubstanceRecord();
			robj.rowObject = r;
		}
		break;
		
		default:
		} 
		*/
		
		return robj;
	}
	
	
	protected ArrayList<Object> getElementObjects(Row row)
	{
		ArrayList<Object> elementObjects = new ArrayList<Object>();
		if (elements != null)
			for (int i = 0; i < elements.size(); i++)
			{
				Object obj = getElementObject(row, elements.get(i));
				elementObjects.add(obj);
			}
		return elementObjects;
	}
	
	
	protected Object getElementObject(Row row, DynamicElement element)
	{	
		Cell c = null;
		
		switch (element.position)
		{
		case ANY_GROUP_ROW:
		case ANY_ROW: 
			//Information is taken from the row itself
			c = row.getCell(element.index);
			break;
			
		case FIRST_ROW: 
			//Information is taken from the row itself
			c = firstRow.getCell(element.index);
			break;
			
		case FIRST_GROUP_ROW: 
			//Information is taken from the row itself
			c = firstGroupRow.getCell(element.index);
			break;
		}
		
		Object elObj  = ExcelUtils.getObjectFromCell(c);
		return elObj;
	}
	
	
	
	
	public void addError(String errorMsg)
	{
		if (FlagStoreErrors)
			errors.add(errorMsg);
		if (logger != null)
			logger.info(errorMsg);
	}
	
	
	/*
	
	public DynamicIterationObject getDynamicIterationObjectFromRows(Sheet sheet, int startRowIndex, int endRowIndex)
	{
		DynamicIterationObject dio = new DynamicIterationObject ();
		//TODO
		return dio;
	}
	
	
	public Object[] createDataObjectsFromRows(Sheet sheet, int startRowIndex, int endRowIndex)
	{
		if (!handleByRows)
			return null;  //This cannot be done if the basic data element is not a row
		
		switch (cumulativeObjectType)
		{
		case SUBSTANCE_ARRAY:
		{	
			return getSubstanceRecordArray(sheet, startRowIndex, endRowIndex).toArray();
		}	
		case SUBSTANCE:
		{	
			SubstanceRecord r = getSubstanceRecord(sheet, startRowIndex, endRowIndex);
			SubstanceRecord array[] = new SubstanceRecord[] {r};
			return array;
		}
		
		default:
			break;
		}
		
		//TODO
		return null;
	}
	
	
	public ArrayList<SubstanceRecord> getSubstanceRecordArray (Sheet sheet, int startRowIndex, int endRowIndex)
	{
		//TODO
		return null;
	}
	
	public SubstanceRecord getSubstanceRecord (Sheet sheet, int startRowIndex, int endRowIndex)
	{
		//TODO
		return null;
	}
	
	*/
	
}
