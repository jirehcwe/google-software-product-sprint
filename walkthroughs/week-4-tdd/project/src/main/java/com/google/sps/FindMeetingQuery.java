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

import com.google.sps.MeetingRequest;
import com.google.sps.TimeRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    Collection<TimeRange> result;

    //Request is too long or too short(more than 1 day/negative duration)
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration() || request.getDuration() <= 0)
    {
      return Arrays.asList();
    }

    //Return full day for trivial empty cases (no event given, no attendees given)
    if (events.isEmpty() || request.getAttendees().isEmpty())
    {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    

    for (Event event: events)
    {
    
    }
    
    throw new UnsupportedOperationException("Not all cases were caught!");
  }

  class ByStartTimeComparator implements Comparator<Event> 
  { 
    // Used for sorting in ascending order of 
    // roll name
    @Override
    public int compare(Event a, Event b) 
    { 
      return TimeRange.ORDER_BY_START.compare(a.getWhen(), b.getWhen());
    } 
  }

  public Collection<Event> sortEventsByStartTime(Collection<Event> events)
  {
    List<Event> list = new ArrayList<Event>(events);

    Collections.sort(list, new ByStartTimeComparator());
    return list;
  }
}
