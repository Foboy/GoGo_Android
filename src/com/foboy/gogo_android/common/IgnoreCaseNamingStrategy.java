package com.foboy.gogo_android.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

@SuppressWarnings("serial")
public class IgnoreCaseNamingStrategy extends PropertyNamingStrategy {
	  @Override
	  public String nameForField(MapperConfig<?> config,
	   AnnotatedField field, String defaultName) {
		 String name = field.getName();
	     return convert(defaultName);
	   
	  }
	  @Override
	  public String nameForGetterMethod(MapperConfig<?> config,
	   AnnotatedMethod method, String defaultName) {
		  String name = method.getName();
	     return convert(defaultName);
	  }
	  
	  @Override
	  public String nameForSetterMethod(MapperConfig<?> config,
	    AnnotatedMethod method, String defaultName) {
		  String name = method.getName();
	   String a = convert(defaultName); 
	   return a;
	  }
	  
	  public String convert(String defaultName )
	  {
	   char[] arr = defaultName.toCharArray();
	   StringBuilder sb = new StringBuilder();
	   if(arr.length !=0)
	   {
	        for(int i = 0; i < arr.length; i++)
	        {
	            char ch = arr[i];
	             
	            if(Character.isUpperCase(ch))
	            {
	                sb.append(Character.toLowerCase(ch));
	            }
	            else
	            {
	                sb.append(ch);
	            }
	        }
	   }
	   return sb.toString();
	  }
}
