import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChannelService } from '../services/channel.service';
import { Channel } from '../shared/models/channel';

@Component({
  selector: 'app-channel',
  templateUrl: './channel.component.html',
  styleUrls: ['./channel.component.css']
})
export class ChannelComponent implements OnInit {
  public channel: Channel = { id: null };

  constructor(
    private route: ActivatedRoute,
    private channelService: ChannelService
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.channelService.fetch(params['channelId']).subscribe(data => {
        this.channel = data.data;
      });
    });
  }

}
