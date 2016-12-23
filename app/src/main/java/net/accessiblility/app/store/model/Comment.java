package net.accessiblility.app.store.model;

import net.accessiblility.app.store.model.base.BaseComment;


public class Comment extends BaseComment {

	public Comment() {
		// TODO Auto-generated constructor stub
		super();
	}

	public Comment(int id, String content, String contentTime, User user, int thumbsUp, int score) {
		super(id, content, contentTime, user, thumbsUp, score);
		// TODO Auto-generated constructor stub
	}
}
