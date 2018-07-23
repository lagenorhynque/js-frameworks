import * as React from 'react';
import { match } from 'react-router-dom';
import { MessageFeed, MessageForm } from '../components';

interface ChannelMatch {
  channelId: number;
}

interface ChannelProps {
  match: match<ChannelMatch>;
}

interface ChannelState {
  shouldReload: boolean;
}

export class Channel extends React.Component<ChannelProps, ChannelState> {
  constructor(props: ChannelProps) {
    super(props);
    this.state = {
      shouldReload: false
    };
  }

  public render() {
    const { channelId } = this.props.match.params;
    return (
      [
        <MessageFeed key='message-feed'
                     channelId={channelId}
                     shouldReload={this.state.shouldReload}
                     setShouldReload={this.setShouldReload} />,
        <MessageForm key='message-form'
                     channelId={channelId}
                     setShouldReload={this.setShouldReload} />
      ]
    );
  }

  private setShouldReload = (shouldReload: boolean) => {
    this.setState({ shouldReload });
  }
}
