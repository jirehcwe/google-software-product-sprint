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

function addRandomGreeting() 
{
  const greetings =
      ['Hello world!', 'This is my first Javascript function.', 'Surprise meme!'];

  // Increment greeting number
  const greetingNum = Math.floor(Math.random() * greetings.length);
  const greeting = greetings[greetingNum];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
  if (greetingNum == 2)
  {
    show_meme();
  }
  else
  {
    remove_meme();
  }
}

function show_meme() 
{
    //Prevents extra memes being created.
    if (document.getElementById('Meme') !== null)
    {
        return;
    }

    var img = document.createElement('img');
    img.src = '/images/honest code.jpg';
    img.style = 'width:50%;height:50%;text-align = center';
    img.alt = 'Honest code.';
    img.id = 'Meme'

    document.body.appendChild(img);
}

function remove_meme()
{
    var image = document.getElementById('Meme');
    if (image !== null)
    {
        image.parentNode.removeChild(image);
    }
}
