import * as React from 'react';
import { fetchChannelDetail, Channel, fetchMessages, Message } from '../client';
import Subheader from 'material-ui/Subheader';
import {List, ListItem} from 'material-ui/List';
import Avatar from 'material-ui/Avatar';

interface MessageFeedProps {
  channelId: number;
  shouldReload: boolean;
  setShouldReload: (shouldReload: boolean) => void;
}

interface MessageFeedState {
  channel: Channel;
  messages: Message[];
}

export class MessageFeed extends React.Component<MessageFeedProps, MessageFeedState> {
  constructor(props: MessageFeedProps) {
    super(props);
    this.state = {
      channel: { name: '' },
      messages: []
    };
  }

  public render() {
    return (
      <List>
      <Subheader>{this.state.channel.name}</Subheader>
      {this.state.messages.slice().reverse().map(message =>
        <ListItem
        key={message.id}
        leftAvatar={<Avatar />}
        primaryText={message.user_name}
        secondaryText={message.body}
        />
      )}
      </List>
    );
  }

  public componentDidMount() {
    this.fetchChannelDetail(this.props.channelId);
    this.fetchMessages(this.props.channelId);
  }

  public componentDidUpdate(prevProps: MessageFeedProps) {
    if (prevProps.channelId !== this.props.channelId
        || (!prevProps.shouldReload && this.props.shouldReload)) {
      this.fetchChannelDetail(this.props.channelId);
      this.fetchMessages(this.props.channelId);
    }
  }

  private fetchChannelDetail(channelId: number) {
    fetchChannelDetail(channelId)
      .then(response => {
        this.setState({ channel: response.data.data });
      })
      .catch(err => {
        console.log(err);
      });
  }

  private fetchMessages(channelId: number) {
    this.props.setShouldReload(false);
    fetchMessages(channelId)
        .then(response => {
          this.setState({ messages: response.data.data });
        })
        .catch(err => {
        console.log(err);
      });
  }
}
