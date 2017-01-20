package net.accessiblility.app.store.model.base;

import net.accessiblility.app.store.model.Comment;
import net.accessiblility.app.store.model.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public abstract class BaseVersion implements Serializable {

	public BaseVersion() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer versionCode;
	private String versionName ;
	private Integer gradle;
	private Integer androidAppSecId;
	private String size;
	private Integer packageId;
	private Set<Comment> comment = new HashSet<Comment>();
	private User users;
	
	public Set<Comment> getComment() {
		return comment;
	}
	public void setComment(Set<Comment> comment) {
		this.comment = comment;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	public Integer getGradle() {
		return gradle;
	}
	public void setGradle(Integer gradle) {
		this.gradle = gradle;
	}
/*	public AndroidAppSec getandroidAppSec() {
		return androidAppSec;
	}
	public void setAndroidAppSec(AndroidAppSec androidAppSec) {
		this.androidAppSec = androidAppSec;
	}*/
	public User getUsers() {
		return users;
	}
	public void setUsers(User users) {
		this.users = users;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		result = prime * result + ((gradle == null) ? 0 : gradle.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((versionCode == null) ? 0 : versionCode.hashCode());
		result = prime * result + ((versionName == null) ? 0 : versionName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseVersion other = (BaseVersion) obj;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		if (gradle == null) {
			if (other.gradle != null)
				return false;
		} else if (!gradle.equals(other.gradle))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (versionCode == null) {
			if (other.versionCode != null)
				return false;
		} else if (!versionCode.equals(other.versionCode))
			return false;
		if (versionName == null) {
			if (other.versionName != null)
				return false;
		} else if (!versionName.equals(other.versionName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BaseVersion [id=" + id + ", versionCode=" + versionCode + ", versionName=" + versionName
				+ ", userId="  + ", gradle=" + gradle + ", androidAppSec="
				+ ", user=" + users + "]";
	}
	public BaseVersion(Integer id, Integer versionCode, String versionName, Integer gradle) {
		super();
		this.id = id;
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.gradle = gradle;
	}

	public Integer getAndroidAppSecId() {
		return androidAppSecId;
	}

	public void setAndroidAppSecId(Integer androidAppSecId) {
		this.androidAppSecId = androidAppSecId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
