package proguide.walleton.util;

import java.util.ArrayList;
import java.util.Map;

public class AESManager
{
	static AESManager	instance	= null;

	public static void dismiss( )
	{

	}

	public static Map<String, Object> encryptParams( Map<String, Object> p_parameters )
	{
		return p_parameters;
	}

	public static AESManager getInstance( )
	{
		if ( null != instance )
			return instance;
		else
			return new AESManager( null );
	}
	
	public static void setInstance( AESManager p_instance )
	{
		instance = p_instance;
	}
	
	public AESManager( ArrayList<String> p_param )
	{
		
	}
	
	
	public ArrayList<String> getSerializated()
	{
		return null;
	}
	
	public static String decrypt( String p_param )
	{
		return p_param;
	}
}
