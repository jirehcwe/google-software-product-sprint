package com.google.sps.data;

public final class FeedPost {

  private String imageUrl;
  private String commentText;
  private long timeStamp;

  public FeedPost(String inputComment, long inputTimeStamp)
  {
    commentText = inputComment;
    timeStamp = inputTimeStamp;
  }*Feed

  public FeedPost(String inputComment, String imageLink, long inputTimeStamp)
  {
    commentText = inputComment;
    timeStamp = inputTimeStamp;
    imageUrl = imageLink;
  }
  
}
