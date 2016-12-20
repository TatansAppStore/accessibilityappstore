package net.accessiblility.app.store.model;

import net.accessiblility.app.store.model.base.BaseVersion;

public class Version extends BaseVersion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Version() {
		super();
	}

	public Version(Integer id, Integer versionCode, String versionName, Integer gradle) {
		super(id, versionCode, versionName, gradle);
	}

}
