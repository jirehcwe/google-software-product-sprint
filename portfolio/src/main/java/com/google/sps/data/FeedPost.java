package com.google.sps.data;

public final class FeedPost implements Comparable<FeedPost>{

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

  public long GetTimeStamp() {
    return timeStamp;
  }


  @Override
    public int compareTo(FeedPost anotherPost) {
      return (int)(this.timeStamp - anotherPost.GetTimeStamp());
    }
  
}
