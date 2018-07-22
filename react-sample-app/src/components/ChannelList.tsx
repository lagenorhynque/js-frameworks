import * as React from 'react';
import { Link, NavLink } from 'react-router-dom';
import Drawer from 'material-ui/Drawer';
import { List, ListItem } from 'material-ui/List';
import ActionHome from 'material-ui/svg-icons/action/home';
import ActionList from 'material-ui/svg-icons/action/list';

const channels = [{ id: 1, name: 'general' }, { id: 2, name: 'random' }];

export const ChannelList = () => {
  return (
    <Drawer open={true}>
      <List>
        <ListItem
          containerElement={<Link to={'/'} />}
          rightIcon={<ActionHome />}
          primaryText="Home">
        </ListItem>
        <ListItem
          rightIcon={<ActionList />}
          primaryText="Channels"
          disabled={true}
          open={true}
          nestedItems={channels.map(channel =>
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
