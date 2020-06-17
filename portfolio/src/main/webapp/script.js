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

function getRandomGame()
{
    console.log('Fetching a random game.');

    addGameNameToDOM("Loading...");
    
    fetch('/random-game')
    .then(response => response.text())
    .then(gameName => addGameNameToDOM(gameName));

    setTimeout(() => {  addGameNameToDOM(""); }, 3000);
}

function addGameNameToDOM(gameName)
{
    document.getElementById('game-container').innerText = gameName;
}

function displayPosts()
{  
  fetch('/doggo-servlet')
  .then(response => response.json())
  .then(postJson => {
    if(postJson.length != 0)
    {
      populatePostsSection(postJson);
    }
    else
    {
      const postsSection = document.getElementById('posts-section');
      postsSection.innerText = "No posts so far.";
    }
   
    
  });

}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function createPostElement(postJson) {
  const post = document.createElement("div");

  const postHeader = document.createElement("div");
  post.appendChild(postHeader);
  const usernameDisplay = document.createElement("p");
  const timestampDisplay = document.createElement("p");
  postHeader.appendChild(usernameDisplay);
  postHeader.appendChild(timestampDisplay);

  usernameDisplay.innerText = "user";
  const timestamp = new Date(postJson.timeStamp);
  timestampDisplay.innerText = timestamp.toUTCString();
  const postContent = document.createElement("div");
  post.appendChild(postContent);
  const imageDisplay = document.createElement("img");
  postContent.appendChild(imageDisplay);

  imageDisplay.src = postJson.imageUrl;

  const postFooter = document.createElement("div");
  post.appendChild(postFooter);
  const commentDisplay = document.createElement("p");
  postFooter.appendChild(commentDisplay);

  commentDisplay.innerText = postJson.commentText;

  return post;
}

function populateCommentSection(commentJson)
{
  const commentsSection = document.getElementById('comments-section');
  commentsSection.innerHTML = '';

  commentJson.forEach((comment) => {
    commentsSection.appendChild(createListElement(comment.commentText));
  });
}

function populatePostsSection(postJson)
{
  const postSection = document.getElementById('posts-section');
  postSection.innerHTML = '';

  postJson.forEach((post) => {
    postSection.appendChild(createPostElement(post));
  });
}

function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-image-upload')
      .then((response) => response.text())
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('my-form');
        messageForm.action = imageUploadUrl;
      });
}