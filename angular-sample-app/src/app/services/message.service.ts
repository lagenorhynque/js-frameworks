import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../shared/models/message';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  fetch(channelId: number): Observable<{data: Message[]}> {
    return this.http.get<{data: Message[]}>(`${this.API}/channels/${channelId}/messages`);
  }
}
