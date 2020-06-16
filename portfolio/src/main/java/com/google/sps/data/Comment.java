package com.google.sps.data;

public final class Comment {

  private String commentText;
  private long timeStamp;

  public Comment(String inputComment, long inputTimeStamp)
  {
    commentText = inputComment;
    timeStamp = inputTimeStamp;
  }
  
}
