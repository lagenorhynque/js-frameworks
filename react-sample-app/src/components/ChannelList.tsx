import * as React from 'react';
import { Link, NavLink } from 'react-router-dom';
import Drawer from 'material-ui/Drawer';
import { List, ListItem } from 'material-ui/List';
import ActionHome from 'material-ui/svg-icons/action/home';
import ActionList from 'material-ui/svg-icons/action/list';
import { fetchChannels, Channel } from '../client';

interface ChannelListState {
  channels: Channel[];
}

export class ChannelList extends React.Component<{}, ChannelListState> {
  constructor(props: any) {
    super(props);
    this.state = {
      channels: []
    };
  }

  public render() {
    return (
      <Drawer open={true}>
        <List>
          <ListItem
            containerElement={<Link to={'/'} />}
            rightIcon={<ActionHome />}
            primaryText='Home'>
          </ListItem>
          <ListItem
            rightIcon={<ActionList />}
            primaryText='Channels'
            disabled={true}
            open={true}
            nestedItems={this.state.channels.map(channel =>
              <ListItem
                key={channel.id}
                primaryText={channel.name}
                containerElement={<Link to={{ pathname: `/channels/${channel.id}` }} />}>
              </ListItem>
            )}>
          </ListItem>
        </List>
      </Drawer>
    );
  }

  public componentDidMount() {
    this.fetchChannels();
  }

  private fetchChannels() {
    fetchChannels()
      .then(response => {
        this.setState({ channels: response.data.data });
      })
      .catch(err => {
        console.log(err);
      });
  }
}
