package com.google.sps.data;

import com.google.sps.data.Comment;
import java.util.ArrayList;

public final class CommentSection {

  private ArrayList<Comment> commentHistory = new ArrayList<Comment>();

  public void AddComment(String commentText)
  {
    Comment newComment = new Comment(commentText);
    commentHistory.add(newComment);
  }


}
