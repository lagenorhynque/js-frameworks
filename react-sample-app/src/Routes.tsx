import * as React from 'react';
import { render } from 'react-dom';
import { Switch } from 'react-router';
import { BrowserRouter, Redirect, Route } from 'react-router-dom';
import { ChannelList } from './components';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import Paper from 'material-ui/Paper';

const routes = <BrowserRouter>
  <div id='wrapper'>
    <ChannelList />
    <main style={{ margin: '1rem 0 1rem 16rem' }}>
      <Paper>
        <Switch>
          <Route
            exact={true} path='/channels/:channelId'
            render={props => <h2>{props.match.params.channelId}</h2>} />
          <Route
            exact={true} path='/'
            render={props => <h1>Sample Application</h1> } />
        </Switch>
      </Paper>
    </main>
  </div>
</BrowserRouter>;

const App = () => (
  <MuiThemeProvider>
    {routes}
  </MuiThemeProvider>
);

render(<App />, document.getElementById('app'));
