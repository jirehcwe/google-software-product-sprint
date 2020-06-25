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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.String;

public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    // Request is too long or too short(more than 1 day/negative duration)
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration() || request.getDuration() <= 0)
    {
      return Arrays.asList();
    }

    // Return full day for trivial empty cases (no event given, no attendees given)
    if (events.isEmpty() || request.getAttendees().isEmpty())
    {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    events = sortEventsByStartTime(events);

    int requiredDuration = (int)request.getDuration(); // Can cast as such because duration does not exceed 2^32.
    Collection<String> requiredAttendees = request.getAttendees();
    
    int currentTime = TimeRange.START_OF_DAY;
    int startTime = TimeRange.START_OF_DAY;
    
    ArrayList<TimeRange> candidateRanges = new ArrayList<TimeRange>();

    Iterator eventsIterator = events.iterator();
    Event currentEvent = null;
    Event previousEvent = null;
    while (startTime != TimeRange.END_OF_DAY) { 

      if (!eventsIterator.hasNext()){
        TimeRange potentialSlot = TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true);
        if (potentialSlot.duration() >= requiredDuration)
        {
          candidateRanges.add(potentialSlot);
        }
        break;
      }

      currentEvent = (Event)eventsIterator.next();

      boolean areAttendeesInCurrentEvent = !Collections.disjoint(currentEvent.getAttendees(), request.getAttendees());

      if (areAttendeesInCurrentEvent)
      {
        currentTime = currentEvent.getWhen().start(); 
      } else 
      {
        continue; // We can take this timeslot as available and hence we look ahead to the next event.
      }

      //        start, current
      //             v
      // Timeline  : |--A-----|-------->
      if (startTime == currentTime)
      {
        startTime = currentEvent.getWhen().end();
        currentTime = currentEvent.getWhen().end();
        continue; //Look ahead to next event
      }

      //           start     current
      //             v          v
      // Timeline  : |----------|-----A-----|-------->
      // Free time in between is potential range

      TimeRange potentialSlot = TimeRange.fromStartEnd(startTime, currentTime, false);
      if (potentialSlot.duration() >= requiredDuration)
      {
        candidateRanges.add(potentialSlot);
      }

      startTime = currentEvent.getWhen().end();
      currentTime = currentEvent.getWhen().end();
    }
    
    return candidateRanges;
  }

  public Collection<Event> sortEventsByStartTime(Collection<Event> events)
  {
    List<Event> list = new ArrayList<Event>(events);

    Collections.sort(list, (a, b) -> TimeRange.ORDER_BY_START.compare(a.getWhen(), b.getWhen()));
    return list;
  }
}
