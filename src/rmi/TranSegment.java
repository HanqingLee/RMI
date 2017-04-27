package rmi;

import java.io.Serializable;

//This class is a package of invoking parameters, and the transmission of this object implemented the Java serialization methods.
public class TranSegment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4781376039900026160L;
	private int Obj_Key;
	private String method;
	private Object[] args;

	public TranSegment(int Obj_Key, String method, Object[] args) {
		this.Obj_Key = Obj_Key;
		this.method = method;
		this.args = args;
	}

	public int getObj_Key() {
		return Obj_Key;
	}

	public void setObj_Key(int obj_Key) {
		Obj_Key = obj_Key;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}
