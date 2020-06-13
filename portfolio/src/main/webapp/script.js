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
const MEME_ID = 'Meme';

function addRandomGreeting() 
{
  const greetings =
      ['Hello world!', 'This is my first Javascript function.', 'Surprise meme!'];

  // Choose a random greeting
  const greetingNum = Math.floor(Math.random() * greetings.length);

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greetings[greetingNum];

  // Show random meme if the 2nd index is hit.
  if (greetingNum == 2)
  {
    showMeme();
  }
  else
  {
    removeMeme();
  }
}

function showMeme() 
{
    //Prevents extra memes being created.
    if (document.getElementById(MEME_ID) !== null)
    {
        return;
    }

    var img = document.createElement('img');
    img.src = '/images/honest code.jpg';
    img.style = 'text-align = center';
    img.alt = 'Honest code.';
    img.id = MEME_ID;

    var content = document.getElementById('content');
    content.appendChild(img);
    
    window.scrollTo(0,document.body.scrollHeight);
}

function removeMeme()
{
    var image = document.getElementById(MEME_ID);
    if (image !== null)
    {
        image.parentNode.removeChild(image);
    }
}

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

function getServerData()
{
    fetch('/data')
    .then(response => response.json())
    .then((stats) => {

    const dataContainerElement = document.getElementById('data-container');
    dataContainerElement.innerHTML = '';
    dataContainerElement.appendChild(
        createListElement('Index 0: ' + stats[0]));
    dataContainerElement.appendChild(
        createListElement('Index 1: ' + stats[1]));
    dataContainerElement.appendChild(
        createListElement('Index 2: ' + stats[2]));
        
    });


}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}