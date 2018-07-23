import Axios, { AxiosInstance, AxiosResponse, CancelToken } from 'axios';

const baseURL = 'http://localhost:8080/api'

const instance: AxiosInstance = Axios.create({
  baseURL,
  timeout: 10000
});

export interface Channel {
  id?: number;
  name?: string;
}

export interface Message {
  id?: number;
  body?: string;
  user_id?: number;
  user_name?: string;
  user_avatar?: string;
  channel_id?: number;
  channel_name?: string;
  date?: string;
}

export const fetchChannels = (params = {}, cancelToken: CancelToken = null): Promise<AxiosResponse<{ data: Channel[] }>> => {
  return instance.get(`/channels`, {
    params,
    cancelToken
  });
};

export const fetchChannelDetail = (channelId: number, params = {}, cancelToken: CancelToken = null): Promise<AxiosResponse<{ data: Channel }>> => {
  return instance.get(`/channels/${channelId}`, {
    params,
    cancelToken
  });
};

export const fetchMessages = (channelId: number, params = {}, cancelToken: CancelToken = null): Promise<AxiosResponse<{ data: Message[] }>> => {
  return instance.get(`/channels/${channelId}/messages`, {
    params,
    cancelToken
  });
};

export const postMessage = (channelId: number, payload: Message, cancelToken: CancelToken = null): Promise<AxiosResponse<Message>> => {
  return instance.post(`/channels/${channelId}/messages`, payload, {
    cancelToken
  });
};
