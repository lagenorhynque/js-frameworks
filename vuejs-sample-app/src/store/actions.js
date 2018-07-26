import {
  SET_MESSAGES,
  GET_CHANNELS
} from './mutation-types'

const getMessagePath = channelId => `http://localhost:8080/api/channels/${channelId}/messages`

async function fetchGetMessages(channelId) {
  const response = await fetch(getMessagePath(channelId))
  const json = await response.json()
  return json.data
}

export default {
  [GET_CHANNELS]({ commit }) {
    async function fetchApi() {
      const response = await fetch('http://localhost:8080/api/channels')
      const json = await response.json()
      commit(GET_CHANNELS, json.data)
    }
    fetchApi()
  },
  async GET_MESSAGES({ commit }, channelId) {
    const messages = await fetchGetMessages(channelId)
    commit(SET_MESSAGES, messages)
  },
  async POST_MESSAGES({ commit }, { channelId, message }) {
    const response = await fetch(getMessagePath(channelId), {
      method: 'POST',
      body: JSON.stringify({
        'body': message,
        'user_id': 1
      }),
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
    const json = await response.json()
    if (response.ok) {
      const messages = await fetchGetMessages(channelId)
      commit(SET_MESSAGES, messages)
    }
  }
}
