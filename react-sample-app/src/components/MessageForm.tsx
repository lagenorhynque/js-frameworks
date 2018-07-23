import * as React from 'react';
import { postMessage, Message } from '../client';
import Paper from 'material-ui/Paper';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

interface MessageFormProps {
  channelId: number;
  setShouldReload: (shouldReload: boolean) => void;
}

interface MessageFormState {
  body?: string;
}

export class MessageForm extends React.Component<MessageFormProps, MessageFormState> {
  constructor(props: MessageFormProps) {
    super(props);
    this.state = {
      body: ''
    };
    this.handleTextAreaChange = this.handleTextAreaChange.bind(this);
    this.handleFormSubmit = this.handleFormSubmit.bind(this);
  }

  public render() {
    return (
      <Paper style={{
        padding: '30px',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}>
        <TextField
          multiLine={true}
          fullWidth={true}
          hintText='Write your message'
          value={this.state.body}
          onChange={this.handleTextAreaChange} />
        <RaisedButton
          label='Send'
          primary={true}
          onClick={this.handleFormSubmit} />
      </Paper>
    );
  }

  private handleTextAreaChange(event: React.FormEvent<HTMLTextAreaElement>) {
    event.preventDefault();
    this.setState({ body: event.currentTarget.value });
  }

  private handleFormSubmit(event: React.FormEvent<HTMLTextAreaElement>) {
    event.preventDefault();
    const payload = {
      body: this.state.body,
      user_id: 1,
    } as Message;
    postMessage(this.props.channelId, payload)
      .then(() => {
        this.setState({ body: '' });
        this.props.setShouldReload(true);
      })
      .catch(err => {
        console.log(err);
      });
  }
}
