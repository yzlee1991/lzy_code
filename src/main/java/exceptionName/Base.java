package exceptionName;

public class Base implements I{

	public static void show(){
		System.out.println("Base...");
		getExtendsName();
	}
	
	public static String getExtendsName(){
		String s="";
		try{
			throw new Exception("");
		}catch(Exception e){
			for(StackTraceElement ee:e.getStackTrace() ){
				System.out.println(ee);
			}
			if ((e != null) && (e.getStackTrace().length >= 3)) {
				System.out.println(e.getStackTrace()[2].getClassName());
			}
		}
		return null;
		
	}
	
	
	public void super11(){
		System.out.println("base..super11..");
	}
}
