package net.accessiblility.app.store.model;

import net.accessiblility.app.store.model.base.BaseComment;

import java.sql.Timestamp;


public class Comment extends BaseComment {

	public Comment() {
		// TODO Auto-generated constructor stub
		super();
	}

	public Comment(int id, String content, Timestamp contentTime, User user, int thumbsUp, int score, Version version) {
		super(id, content, contentTime, user, thumbsUp, score, version);
		// TODO Auto-generated constructor stub
	}
}
