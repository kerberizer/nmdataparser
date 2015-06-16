package net.enanomapper.parser.json;

import org.codehaus.jackson.JsonNode;

public class JsonUtilities 
{
	private String error = "";
	/**
	 * 
	 * @return
	 */
	public String getError() {
		return error;
	}
	/**
	 * 
	 * @param node
	 * @param keyword
	 * @param isRequired
	 * @return
	 */
	public String extractStringKeyword(JsonNode node, String keyword, boolean isRequired)
	{
		error = "";
		JsonNode keyNode = node.path(keyword);
		if(keyNode.isMissingNode())
		{
			if(isRequired)
			{	
				error = "Keyword " + keyword + " is missing!";
				return null;
			}
			return "";
		}
		
		if (keyNode.isTextual())
		{	
			return keyNode.asText();
		}
		else
		{	
			error = "Keyword " + keyword + " is not of type text!";
			return null;
		}			
	}
	/**
	 * 
	 * @param node
	 * @param keyword
	 * @param isRequired
	 * @return
	 */
	public Double extractDoubleKeyword(JsonNode node, String keyword, boolean isRequired)
	{
		error = "";
		JsonNode keyNode = node.path(keyword);
		if(keyNode.isMissingNode())
		{
			if(isRequired)
			{	
				error = "Keyword " + keyword + " is missing!";
				return null;
			}
			return null;
		}
		
		if (keyNode.isDouble())
		{	
			return keyNode.asDouble();
		}
		else
		{	
			error = "Keyword " + keyword + " is not of type Double!";
			return null;
		}			
	}
	/**
	 * 
	 * @param node
	 * @param keyword
	 * @param isRequired
	 * @return
	 */
	public Integer extractIntKeyword(JsonNode node, String keyword, boolean isRequired)
	{
		error = "";
		JsonNode keyNode = node.path(keyword);
		if(keyNode.isMissingNode())
		{
			if(isRequired)
			{	
				error = "Keyword " + keyword + " is missing!";
				return null;
			}
			return null;
		}
		
		if (keyNode.isInt())
		{	
			return keyNode.asInt();
		}
		else
		{	
			error = "Keyword " + keyword + " is not of type Int!";
			return null;
		}			
	}
	/**
	 * 
	 * @param node
	 * @param keyword
	 * @param isRequired
	 * @return
	 */
	public Boolean extractBooleanKeyword(JsonNode node, String keyword, boolean isRequired)
	{
		error = "";
		JsonNode keyNode = node.path(keyword);
		if(keyNode.isMissingNode())
		{
			if(isRequired)
			{	
				error = "Keyword " + keyword + " is missing!";
				return null;
			}
			return null;
		}
		
		if (keyNode.isBoolean())
		{	
			return keyNode.asBoolean();
		}
		else
		{	
			error = "Keyword " + keyword + " is not of type Boolean!";
			return null;
		}			
	}
	/**
	 * 
	 * @param node
	 * @return
	 */
	public static String getNodeTypeAsString(JsonNode node)
	{
		if (node.isArray())
			return ("Array");
		
		if (node.isTextual())
			return ("Text");
		
		if (node.isObject())
			return ("Object");
		
		if (node.isInt())
			return ("Int");
		
		if (node.isLong())
			return ("Long");
		
		if (node.isDouble())
			return ("Double");
		
		if (node.isBoolean())
			return ("Boolean");
		
		if (node.isBigInteger())
			return ("BigInteger");
		
		
		/*
		if (node.isMissingNode())
			return ("MissingNode");
		*/
		return "0";
	}
	/**
	 * 
	 * @param node
	 * @return
	 */
	public static Object extractObject (JsonNode node)
	{
		if (node.isTextual())
		{
			String s = node.asText();
			if (s != null)
				return s;
		}
		
		if (node.isInt())
		{
			int i = node.asInt();
			return  new Integer(i);
		}
		
		if (node.isDouble())
		{
			double d  = node.asDouble();
			return new Double(d);
		}
		
		//TODO - eventually add array object extraction
		
		return null;
	}
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String objectToJsonField(Object obj)
	{
		if (obj == null)
			return null;
		
		if (obj instanceof String)
			return ("\"" + obj.toString() + "\"");
		
		if (obj instanceof Integer)
			return obj.toString();
		
		if (obj instanceof Double)
			return obj.toString();
		
		if (obj instanceof double[])
		{
			return toJsonField((double[]) obj);
		}
			
		//TODO handle some other cases as arrays etc.
		
		return null;
	}
	/**
	 * 
	 * @param d
	 * @return
	 */
	public static String toJsonField(double d[])
	{
		if (d == null)
			return null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < d.length; i++)
		{
			sb.append(d[i]);
			if (i < (d.length-1))
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}
	
}
