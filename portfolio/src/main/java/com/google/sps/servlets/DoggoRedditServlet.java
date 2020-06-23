// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.sps.data.FeedPost;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.PrintWriter;

@WebServlet("/doggo-servlet")
public final class DoggoRedditServlet extends HttpServlet {

  static final String COMMENT_TEXT = "commentText";
  static final String TIMESTAMP = "timestamp";
  static final String IMAGE_URL = "imageUrl";
  static final String FEED_POST = "FeedPost";
  static final String DOG_NAME = "dogName";
  static
  Gson gson = new Gson();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Entity postEntity = new Entity(FEED_POST);
    
    String dogName = request.getParameter(DOG_NAME);
    String comment = request.getParameter(COMMENT_TEXT);
    String uploadURL = getUploadedFileUrlFromBlobstore(request, "image");
    long timestamp = System.currentTimeMillis();
    FeedPost newPost = new FeedPost(dogName, comment, uploadURL, timestamp);
    postEntity.setProperty(FEED_POST, gson.toJson(newPost));

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(postEntity);

    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    Query query = new Query(FEED_POST);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<FeedPost> posts = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      
      String postJson = (String) entity.getProperty(FEED_POST);
      FeedPost post = gson.fromJson(postJson, FeedPost.class);
      posts.add(post);
      System.out.println(postJson);
      System.out.println(post);
    }
    
    Collections.sort(posts, Collections.reverseOrder());

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(posts));
  }

  private String getUploadedFileUrlFromBlobstore(HttpServletRequest request, String formInputElementName) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get(formInputElementName);

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // We could check the validity of the file here, e.g. to make sure it's an image file
    // https://stackoverflow.com/q/10779564/873165

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    String url = imagesService.getServingUrl(options);

    System.out.println(url);
    // GCS's localhost preview is not actually on localhost,
    // so make the URL relative to the current domain.
    if(url.startsWith("http://localhost:8080/")){
      url = url.replace("http://localhost:8080/", "/");
    }
    return url;
  }

}
