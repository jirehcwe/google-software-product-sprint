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

package com.google.sps;

import com.google.sps.Event;
import com.google.sps.MeetingRequest;
import com.google.sps.TimeRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream; 
import java.io.*;

public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    // Request is too long or too short(more than 1 day/negative duration)
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration() || request.getDuration() <= 0)
    {
      return Arrays.asList();
    }

    Collection<String> requiredAttendees = request.getAttendees();

    List<Event> relevantEvents = events
                                  .stream()
                                  .filter(event -> !Collections.disjoint(event.getAttendees(), requiredAttendees))
                                  .sorted((a, b) -> TimeRange.ORDER_BY_START.compare(a.getWhen(), b.getWhen()))
                                  .collect(Collectors.toList());
    


    // Return full day for trivial empty cases (no event given, no attendees given)
    if (relevantEvents.isEmpty() || request.getAttendees().isEmpty())
    {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    int requiredDuration = (int)request.getDuration(); // Can cast as such because duration does not exceed 2^32.
    
    ArrayList<TimeRange> candidateRanges = new ArrayList<TimeRange>();

    Iterator eventsIterator = relevantEvents.iterator();
    Event currentEvent = (Event)eventsIterator.next();
    Event previousEvent = currentEvent;
    int currentTime = TimeRange.START_OF_DAY;
    int startTime = TimeRange.START_OF_DAY;
    
    while (true) 
    {
      currentTime = currentEvent.getWhen().start(); // Available time is only up to when event starts, since there are clashing attendees.

      TimeRange potentialSlot = TimeRange.fromStartEnd(startTime, currentTime, false);

      if (potentialSlot.duration() >= requiredDuration)
      {
        candidateRanges.add(potentialSlot);
      }

      //Update start time
      startTime = Math.max(currentEvent.getWhen().end(), previousEvent.getWhen().end());

      // Check for next event
      if (eventsIterator.hasNext())
      {
        previousEvent = currentEvent;
        currentEvent = (Event)eventsIterator.next();
        continue;
      }

      // Terminate loop
      potentialSlot = TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true);
      if (potentialSlot.duration() >= requiredDuration)
      {
        candidateRanges.add(potentialSlot);
      }
    
      break;
      
    }

    return candidateRanges;
  }

}
