import { Component, OnInit } from '@angular/core';
import { ChannelService } from './services/channel.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Angular sample app';
  channels = [];

  constructor(
    private channelService: ChannelService
  ) { }

  ngOnInit() {
    this.channelService.fetchList().subscribe(data => {
      this.channels = data.data;
    });
  }
}
