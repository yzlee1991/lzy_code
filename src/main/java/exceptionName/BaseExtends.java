package exceptionName;

public class BaseExtends extends Base implements I2{

	/*public static void show(){
		System.out.println("BaseExtends...");
	}*/
	
	public final static String getExtendsName(){
		Base.show();
		return null;
	}
	
	public void super11(){
		System.out.println("BaseExtends..super11..");
	}
	
	public void print(){
		super.super11();
		super11();
	}
}
