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

function createPostInDom(postJson) {
  postFormat = `<div id='post-container'>
                  <div id='post-header'>
                    <p id='dogname-display'>${postJson.dogName}</p>
                    <p id='date-display'>${new Date(postJson.timeStamp).toUTCString()}</p>
                  </div>
                  <div id='post-content'>
                    <img src=${postJson.imageUrl} id='image-display'>
                  </div>
                  <div id='post-footer'>
                  <p id='comment-display'>${postJson.commentText}</p>
                  </div>
                </div>
                `

  return postFormat;
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
    postSection.innerHTML += createPostInDom(post);
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