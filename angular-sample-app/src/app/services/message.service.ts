import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Message } from '../shared/models/message';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private API = 'http://localhost:8080/api';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  private subject = new Subject();

  constructor(private http: HttpClient) { }

  fetch(channelId: number): Observable<{data: Message[]}> {
    return this.http.get<{data: Message[]}>(`${this.API}/channels/${channelId}/messages`);
  }

  post(channelId: number, body: string, userId: number): Observable<{data: Message}> {
    return this.http.post<{data: Message}>(`${this.API}/channels/${channelId}/messages`,
                                           { 'body': body, 'user_id': userId},
                                           this.httpOptions);
  }

  get waiting() {
    return this.subject.asObservable();
  }

  notify() {
    this.subject.next();
  }
}
