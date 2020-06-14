package com.google.sps.data;

public final class Comment {

  private String commentText = "Placeholder Comment";
  private long timeStamp = 0;

  public Comment(String inputComment, long inputTimeStamp)
  {
    commentText = inputComment;
    timeStamp = inputTimeStamp;
  }
  
}
