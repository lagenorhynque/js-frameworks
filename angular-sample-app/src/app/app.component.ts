import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Angular sample app';
  channels = [{ id: 1, name: 'general' }, { id: 2, name: 'random' }];
}
