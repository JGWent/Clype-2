/**
*
* The Class that will take care of messages for the Clype Application
* Has 2 Constructors
* Has 1 private properties
* Has 0 default vars
* Has 4 methods
* @author John Graham
*
*
*/
package data;
public class MessageClypeData extends ClypeData{
	private String message;
	
	/**
	 * Constructor for MessageClypeData 
	 * Accepts a user name, message, and type
	 * calls ClypeData Constructor using super for user name and type.
	 * Sets message to entered message.
	*/
	public MessageClypeData(String userName, String message, int type) {
		super(userName,type);
		this.message = message;
	}
	
	/**
	 * Constructor for MessageClypeData 
	 * Accepts a user name, message, key, and type
	 * calls ClypeData Constructor using super for user name and type.
	 * Sets message to encrypted entered message.
	*/
	public MessageClypeData(String userName, String message, String	key, int type) {
		super(userName,type);
		this.message = this.encrypt(message, key);
	}
	
	/**
	 * getData()
	 * Returns file contents
	*/
	public MessageClypeData() {
		super(3);
		this.message = "";
	}
	
	/**
	 * getData()
	 * Returns message
	*/
	public String getData() {
		return this.message;
	}
	
	/**
	 * getData()
	 * Accepts Key
	 * Returns decrypted message
	*/
	public String getData(String key) {
		return this.decrypt(this.message, key);
	}

	/**
	 * hashCode()
	 * Returns the hash code
	*/
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + this.message.hashCode();
		
		return result;
	}
	
	/**
	 * toString()
	 * Prints all items of a object to the command window
	*/
	@Override
	public String toString() {
		return "The Message is: " + this.message + 
				",\n and the user is: " + this.getUserName() +
				",\n and the current type is: " + this.getType() +
				",\n and the date is: " + this.getDate();
	}
	
	/**
	 * equals()
	 * Compares two objects to see if they are the same.
	*/
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MessageClypeData)) {
			return false;
		}
		
		MessageClypeData otherMessage = (MessageClypeData)other;
		return this.message == otherMessage.message;
	}
}
