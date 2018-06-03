//用户信息
package Common;

public class User implements java.io.Serializable{

	    public String userid;//登录名
		public String passwd;//密码
		public String online;
		public User() {
			userid=null;
			passwd=null;
			online="no";
		}
		public String getUserID() {
			return userid;
		}
		public void setUserID(String userid) {
			this.userid=userid;
		}
		public String getPasswd() {
			return passwd;
		}
		public void setPasswd(String passwd) {
			this.passwd=passwd;
		}
		public String getonline() {
			return online;
		}
		public void setonline(String online) {
			this.online=online;
		}
}
