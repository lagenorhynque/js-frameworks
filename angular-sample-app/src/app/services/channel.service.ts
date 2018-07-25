import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Channel } from '../shared/models/channel';

@Injectable({
  providedIn: 'root'
})
export class ChannelService {
  private API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  fetch(channelId: number): Observable<{data: Channel}> {
    return this.http.get<{data: Channel}>(`${this.API}/channels/${channelId}`);
  }

  fetchList(): Observable<{data: Channel[]}> {
    return this.http.get<{data: Channel[]}>(`${this.API}/channels`);
  }
}
