package com.google.sps.data;

public final class FeedPost {

  private String dogName;
  private String imageUrl;
  private String commentText;
  private long timeStamp;

  public FeedPost(String inputDogName, String inputComment, String imageLink, long inputTimeStamp)
  {
    dogName = inputDogName;
    commentText = inputComment;
    timeStamp = inputTimeStamp;
    imageUrl = imageLink;
  }
  
}
